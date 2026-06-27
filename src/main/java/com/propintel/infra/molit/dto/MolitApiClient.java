package com.propintel.infra.molit;

import com.propintel.infra.molit.dto.MolitTransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MolitApiClient {

    @Value("${molit.api-key}")
    private String apiKey;

    @Value("${molit.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    public List<MolitTransactionDto> fetchTransactions(String dealYmd) {
        return fetchTransactions(dealYmd, "11680");
    }

    public List<MolitTransactionDto> fetchTransactions(String dealYmd, String lawdCd) {
        try {
            String xml = webClientBuilder.build()
                    .get()
                    .uri(baseUrl + "/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev"
                            + "?serviceKey=" + apiKey
                            + "&DEAL_YMD=" + dealYmd
                            + "&LAWD_CD=" + lawdCd
                            + "&pageNo=1&numOfRows=1000")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("국토부 API 응답 수신: dealYmd={}, lawdCd={}", dealYmd, lawdCd);
            return parseXml(xml);

        } catch (Exception e) {
            log.error("국토부 API 호출 실패: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<MolitTransactionDto> parseXml(String xml) throws Exception {
        List<MolitTransactionDto> result = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
        );

        NodeList items = doc.getElementsByTagName("item");
        log.info("파싱된 건수: {}", items.getLength());

        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            result.add(new MolitTransactionDto(
                    getText(item, "aptNm"),       // 아파트명
                    getText(item, "umdNm"),       // 법정동
                    getText(item, "excluUseAr"),  // 전용면적
                    getText(item, "floor"),        // 층
                    getText(item, "dealAmount"),   // 거래금액
                    getText(item, "dealYear"),     // 년
                    getText(item, "dealMonth"),    // 월
                    getText(item, "dealDay")       // 일
            ));
        }
        return result;
    }

    private String getText(Element el, String tag) {
        NodeList nl = el.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        return nl.item(0).getTextContent().trim();
    }
}