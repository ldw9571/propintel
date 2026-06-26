package com.propintel.infra.molit;

import com.propintel.infra.molit.dto.MolitTransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MolitApiClient {

    @Value("${molit.api-key:test-key}")
    private String apiKey;

    @Value("${molit.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    public List<MolitTransactionDto> fetchTransactions(String dealYmd) {
        try {
            Map<?, ?> response = webClientBuilder.build()
                    .get()
                    .uri(baseUrl + "/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev"
                            + "?serviceKey=" + apiKey
                            + "&DEAL_YMD=" + dealYmd
                            + "&pageNo=1&numOfRows=1000")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // 실제 파싱 로직 (API 응답 구조에 따라 수정)
            log.info("국토부 API 응답 수신: dealYmd={}", dealYmd);
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("국토부 API 호출 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}