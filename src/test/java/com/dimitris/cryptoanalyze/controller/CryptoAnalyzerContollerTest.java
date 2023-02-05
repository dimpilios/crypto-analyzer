package com.dimitris.cryptoanalyze.controller;

import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsListDto;
import com.dimitris.cryptoanalyze.service.CryptoAnalyzer;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.enums.CryptoMetricEnum;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.exception.CryptoNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CryptoAnalyzerContollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoAnalyzer analyzer;

    @Test
    void test_sortCryptosByNormalizedRangeDesc_timePeriodProvided() throws Exception {
        String requestAsJsonString = "{\n" +
                "  \"fromDateTime\": \"2022-01-01 00:00:00\",\n" +
                "  \"toDateTime\": \"2022-01-02 00:00:00\"\n" +
                "}";

        CryptoMetricsListDto expectedResponse = new CryptoMetricsListDto(List.of(
                new CryptoMetricDto(CryptoEnum.xrp.getCode(), new BigDecimal("1.75")),
                new CryptoMetricDto(CryptoEnum.btc.getCode(), new BigDecimal("0.57143")),
                new CryptoMetricDto(CryptoEnum.doge.getCode(), new BigDecimal("0.36")),
                new CryptoMetricDto(CryptoEnum.eth.getCode(), new BigDecimal("0.24444")),
                new CryptoMetricDto(CryptoEnum.ltc.getCode(), new BigDecimal("0.21782"))
        ));

        when(analyzer.getCryptosSortedByNormalizedRangeDesc(any())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/sort/by/normalized/desc")
                                .content(requestAsJsonString)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.cryptos[0].crypto").value(CryptoEnum.xrp.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[0].value").value("1.75")
                ).andExpect(
                        jsonPath("$.cryptos[1].crypto").value(CryptoEnum.btc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[1].value").value("0.57143")
                ).andExpect(
                        jsonPath("$.cryptos[2].crypto").value(CryptoEnum.doge.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[2].value").value("0.36")
                ).andExpect(
                        jsonPath("$.cryptos[3].crypto").value(CryptoEnum.eth.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[3].value").value("0.24444")
                ).andExpect(
                        jsonPath("$.cryptos[4].crypto").value(CryptoEnum.ltc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[4].value").value("0.21782")
                );

        verify(analyzer).getCryptosSortedByNormalizedRangeDesc(any());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_sortCryptosByNormalizedRangeDesc_timePeriodNotProvided() throws Exception {
        CryptoMetricsListDto expectedResponse = new CryptoMetricsListDto(List.of(
                new CryptoMetricDto(CryptoEnum.xrp.getCode(), new BigDecimal("1.75")),
                new CryptoMetricDto(CryptoEnum.btc.getCode(), new BigDecimal("0.57143")),
                new CryptoMetricDto(CryptoEnum.doge.getCode(), new BigDecimal("0.36")),
                new CryptoMetricDto(CryptoEnum.eth.getCode(), new BigDecimal("0.24444")),
                new CryptoMetricDto(CryptoEnum.ltc.getCode(), new BigDecimal("0.21782"))
        ));

        when(analyzer.getCryptosSortedByNormalizedRangeDesc(any())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/sort/by/normalized/desc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.cryptos[0].crypto").value(CryptoEnum.xrp.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[0].value").value("1.75")
                ).andExpect(
                        jsonPath("$.cryptos[1].crypto").value(CryptoEnum.btc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[1].value").value("0.57143")
                ).andExpect(
                        jsonPath("$.cryptos[2].crypto").value(CryptoEnum.doge.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[2].value").value("0.36")
                ).andExpect(
                        jsonPath("$.cryptos[3].crypto").value(CryptoEnum.eth.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[3].value").value("0.24444")
                ).andExpect(
                        jsonPath("$.cryptos[4].crypto").value(CryptoEnum.ltc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[4].value").value("0.21782")
                );

        verify(analyzer).getCryptosSortedByNormalizedRangeDesc(any());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_sortCryptosByNormalizedRangeDesc_timePeriodStartProvidedOnly() throws Exception {
        String requestAsJsonString = "{\n" +
                "  \"fromDateTime\": \"2022-01-01 00:00:00\",\n" +
                "  \"toDateTime\": null\n" +
                "}";

        CryptoMetricsListDto expectedResponse = new CryptoMetricsListDto(List.of(
                new CryptoMetricDto(CryptoEnum.xrp.getCode(), new BigDecimal("1.75")),
                new CryptoMetricDto(CryptoEnum.btc.getCode(), new BigDecimal("0.57143")),
                new CryptoMetricDto(CryptoEnum.doge.getCode(), new BigDecimal("0.36")),
                new CryptoMetricDto(CryptoEnum.eth.getCode(), new BigDecimal("0.24444")),
                new CryptoMetricDto(CryptoEnum.ltc.getCode(), new BigDecimal("0.21782"))
        ));

        when(analyzer.getCryptosSortedByNormalizedRangeDesc(any())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/sort/by/normalized/desc")
                                .content(requestAsJsonString)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.cryptos[0].crypto").value(CryptoEnum.xrp.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[0].value").value("1.75")
                ).andExpect(
                        jsonPath("$.cryptos[1].crypto").value(CryptoEnum.btc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[1].value").value("0.57143")
                ).andExpect(
                        jsonPath("$.cryptos[2].crypto").value(CryptoEnum.doge.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[2].value").value("0.36")
                ).andExpect(
                        jsonPath("$.cryptos[3].crypto").value(CryptoEnum.eth.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[3].value").value("0.24444")
                ).andExpect(
                        jsonPath("$.cryptos[4].crypto").value(CryptoEnum.ltc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[4].value").value("0.21782")
                );

        verify(analyzer).getCryptosSortedByNormalizedRangeDesc(any());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_sortCryptosByNormalizedRangeDesc_timePeriodEndProvidedOnly() throws Exception {
        String requestAsJsonString = "{\n" +
                "  \"fromDateTime\": null,\n" +
                "  \"toDateTime\": \"2022-01-01 00:00:00\"\n" +
                "}";

        CryptoMetricsListDto expectedResponse = new CryptoMetricsListDto(List.of(
                new CryptoMetricDto(CryptoEnum.xrp.getCode(), new BigDecimal("1.75")),
                new CryptoMetricDto(CryptoEnum.btc.getCode(), new BigDecimal("0.57143")),
                new CryptoMetricDto(CryptoEnum.doge.getCode(), new BigDecimal("0.36")),
                new CryptoMetricDto(CryptoEnum.eth.getCode(), new BigDecimal("0.24444")),
                new CryptoMetricDto(CryptoEnum.ltc.getCode(), new BigDecimal("0.21782"))
        ));

        when(analyzer.getCryptosSortedByNormalizedRangeDesc(any())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/sort/by/normalized/desc")
                                .content(requestAsJsonString)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.cryptos[0].crypto").value(CryptoEnum.xrp.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[0].value").value("1.75")
                ).andExpect(
                        jsonPath("$.cryptos[1].crypto").value(CryptoEnum.btc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[1].value").value("0.57143")
                ).andExpect(
                        jsonPath("$.cryptos[2].crypto").value(CryptoEnum.doge.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[2].value").value("0.36")
                ).andExpect(
                        jsonPath("$.cryptos[3].crypto").value(CryptoEnum.eth.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[3].value").value("0.24444")
                ).andExpect(
                        jsonPath("$.cryptos[4].crypto").value(CryptoEnum.ltc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[4].value").value("0.21782")
                );

        verify(analyzer).getCryptosSortedByNormalizedRangeDesc(any());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_sortCryptosByNormalizedRangeDesc_timePeriodProvidedWithBothFieldsNull() throws Exception {
        String requestAsJsonString = "{\n" +
                "  \"fromDateTime\": null,\n" +
                "  \"toDateTime\": null\n" +
                "}";

        CryptoMetricsListDto expectedResponse = new CryptoMetricsListDto(List.of(
                new CryptoMetricDto(CryptoEnum.xrp.getCode(), new BigDecimal("1.75")),
                new CryptoMetricDto(CryptoEnum.btc.getCode(), new BigDecimal("0.57143")),
                new CryptoMetricDto(CryptoEnum.doge.getCode(), new BigDecimal("0.36")),
                new CryptoMetricDto(CryptoEnum.eth.getCode(), new BigDecimal("0.24444")),
                new CryptoMetricDto(CryptoEnum.ltc.getCode(), new BigDecimal("0.21782"))
        ));

        when(analyzer.getCryptosSortedByNormalizedRangeDesc(any())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/sort/by/normalized/desc")
                                .content(requestAsJsonString)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.cryptos[0].crypto").value(CryptoEnum.xrp.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[0].value").value("1.75")
                ).andExpect(
                        jsonPath("$.cryptos[1].crypto").value(CryptoEnum.btc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[1].value").value("0.57143")
                ).andExpect(
                        jsonPath("$.cryptos[2].crypto").value(CryptoEnum.doge.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[2].value").value("0.36")
                ).andExpect(
                        jsonPath("$.cryptos[3].crypto").value(CryptoEnum.eth.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[3].value").value("0.24444")
                ).andExpect(
                        jsonPath("$.cryptos[4].crypto").value(CryptoEnum.ltc.getCode())
                ).andExpect(
                        jsonPath("$.cryptos[4].value").value("0.21782")
                );

        verify(analyzer).getCryptosSortedByNormalizedRangeDesc(any());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_getMetricsForCrypto() throws Exception {
        Map<CryptoMetricEnum, BigDecimal> metrics = new HashMap<>();
        metrics.put(CryptoMetricEnum.OLDEST_PRICE, new BigDecimal("17.12345678"));
        metrics.put(CryptoMetricEnum.NEWEST_PRICE, new BigDecimal("22"));
        metrics.put(CryptoMetricEnum.MIN_PRICE, new BigDecimal("14"));
        metrics.put(CryptoMetricEnum.MAX_PRICE, new BigDecimal("22"));
        metrics.put(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, new BigDecimal("0.57143"));

        Map<String, BigDecimal> expectedMetricsMap = metrics.entrySet()
                .stream()
                .collect(Collectors.toMap((entry) -> entry.getKey().getCode(), Map.Entry::getValue));
        CryptoMetricsDto expectedResponse = new CryptoMetricsDto(expectedMetricsMap);

        when(analyzer.getMetricsForCrypto(any(), anyString())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/metrics/btc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );

        verify(analyzer).getMetricsForCrypto(any(), anyString());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_getMetricsForCrypto_cryptoNotSupported() throws Exception {
        when(analyzer.getMetricsForCrypto(any(), anyString())).thenThrow(new CryptoNotFoundException("Crypto not found"));

        mockMvc
                .perform(
                        post("/api/crypto/analyze/metrics/ttt")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        jsonPath("$.errorMessage").value("Crypto not found")
                );

        verify(analyzer).getMetricsForCrypto(any(), anyString());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_getCryptoWithHighestNormalizedRange() throws Exception {
        CryptoMetricDto expectedResponse = new CryptoMetricDto("btc", new BigDecimal("124"));
        when(analyzer.getCryptoWithHighestNormalizedRange(any())).thenReturn(expectedResponse);

        mockMvc
                .perform(
                        post("/api/crypto/analyze/crypto/with/highest/normalized")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        jsonPath("$.crypto").value("btc")
                ).andExpect(
                        jsonPath("$.value").value("124")
                );

        verify(analyzer).getCryptoWithHighestNormalizedRange(any());
        verifyNoMoreInteractions(analyzer);
    }

    @Test
    void test_getCryptoWithHighestNormalizedRange_internalError() throws Exception {
        when(analyzer.getCryptoWithHighestNormalizedRange(any())).thenThrow(new CryptoInternalException("Internal error"));

        mockMvc
                .perform(
                        post("/api/crypto/analyze/crypto/with/highest/normalized")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isInternalServerError()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        jsonPath("$.errorMessage").value("Internal error")
                );

        verify(analyzer).getCryptoWithHighestNormalizedRange(any());
        verifyNoMoreInteractions(analyzer);
    }
}
