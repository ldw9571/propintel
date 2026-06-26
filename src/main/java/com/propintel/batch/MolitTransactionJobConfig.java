package com.propintel.batch;

import com.propintel.domain.complex.entity.Complex;
import com.propintel.domain.complex.entity.Transaction;
import com.propintel.domain.complex.repository.ComplexRepository;
import com.propintel.domain.complex.repository.TransactionRepository;
import com.propintel.infra.molit.MolitApiClient;
import com.propintel.infra.molit.dto.MolitTransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MolitTransactionJobConfig {

    private final JobRepository             jobRepository;
    private final PlatformTransactionManager txManager;
    private final MolitApiClient            molitApiClient;
    private final ComplexRepository         complexRepository;
    private final TransactionRepository     transactionRepository;

    @Bean
    public Job molitTransactionJob() {
        return new JobBuilder("molitTransactionJob", jobRepository)
                .start(molitTransactionStep())
                .build();
    }

    @Bean
    public Step molitTransactionStep() {
        return new StepBuilder("molitTransactionStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    YearMonth target = YearMonth.now().minusMonths(1);
                    String dealYmd   = target.getYear()
                            + String.format("%02d", target.getMonthValue());

                    log.info("국토부 실거래가 수집 시작: {}", dealYmd);

                    List<MolitTransactionDto> dtos =
                            molitApiClient.fetchTransactions(dealYmd);

                    int saved = 0;
                    for (MolitTransactionDto dto : dtos) {
                        complexRepository.findByNameAndAddress(
                                dto.aptName(), dto.umdNm()
                        ).ifPresent(complex -> {
                            transactionRepository.save(
                                    Transaction.builder()
                                            .complex(complex)
                                            .type(Transaction.TransactionType.SALE)
                                            .areaSqm((int) Double.parseDouble(dto.excluUseAr()))
                                            .floor(Integer.parseInt(dto.floor()))
                                            .price(Long.parseLong(
                                                    dto.dealAmount().replace(",", "")))
                                            .dealDate(LocalDate.of(
                                                    Integer.parseInt(dto.dealYear()),
                                                    Integer.parseInt(dto.dealMonth()),
                                                    Integer.parseInt(dto.dealDay())))
                                            .build()
                            );
                        });
                        saved++;
                    }
                    log.info("국토부 수집 완료: {}건", saved);
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }
}