package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsListDto;
import com.dimitris.cryptoanalyze.controller.dto.TimePeriodRequestDto;
import com.dimitris.cryptoanalyze.service.CryptoMetricsCalculator;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.enums.CryptoMetricEnum;
import com.dimitris.cryptoanalyze.service.enums.SortOrder;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.exception.CryptoNotFoundException;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CryptoAnalyzerImplTest {

    @Mock
    private CryptoMetricsCalculator calculator;

    @InjectMocks
    private CryptoAnalyzerImpl analyzer;

    @Test
    public void test_getCryptosSortedByNormalizedRangeDesc() throws CryptoInternalException {
        TimePeriod timePeriod = new TimePeriod(Optional.empty(), Optional.empty());
        TimePeriodRequestDto timePeriodRequestDto = new TimePeriodRequestDto();
        timePeriodRequestDto.setFromDateTime(timePeriod.getFromDateTime());
        timePeriodRequestDto.setToDateTime(timePeriod.getToDateTime());

        Map<CryptoEnum, BigDecimal> map = new HashMap<>();
        map.put(CryptoEnum.btc, new BigDecimal("0.57143"));
        map.put(CryptoEnum.doge, new BigDecimal("0.36"));
        map.put(CryptoEnum.eth, new BigDecimal("0.24444"));
        map.put(CryptoEnum.ltc, new BigDecimal("0.21782"));
        map.put(CryptoEnum.xrp, new BigDecimal("1.75"));
        List<Map.Entry<CryptoEnum, BigDecimal>> cryptos = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
        Collections.reverse(cryptos);

        when(calculator.sortCryptosByMetric(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, SortOrder.DESC, timePeriod)).thenReturn(cryptos);

        CryptoMetricsListDto expectedResponse = new CryptoMetricsListDto(List.of(
                new CryptoMetricDto(CryptoEnum.xrp.getCode(), new BigDecimal("1.75")),
                new CryptoMetricDto(CryptoEnum.btc.getCode(), new BigDecimal("0.57143")),
                new CryptoMetricDto(CryptoEnum.doge.getCode(), new BigDecimal("0.36")),
                new CryptoMetricDto(CryptoEnum.eth.getCode(), new BigDecimal("0.24444")),
                new CryptoMetricDto(CryptoEnum.ltc.getCode(), new BigDecimal("0.21782"))
        ));

        CryptoMetricsListDto response = analyzer.getCryptosSortedByNormalizedRangeDesc(Optional.of(timePeriodRequestDto));
        assertEquals(response, expectedResponse);

        verify(calculator).sortCryptosByMetric(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, SortOrder.DESC, timePeriod);
        verifyNoMoreInteractions(calculator);
    }

    @Test
    public void test_getMetricsForCrypto() throws CryptoInternalException, CryptoNotFoundException {
        TimePeriod timePeriod = new TimePeriod(Optional.empty(), Optional.empty());
        TimePeriodRequestDto timePeriodRequestDto = new TimePeriodRequestDto();
        timePeriodRequestDto.setFromDateTime(timePeriod.getFromDateTime());
        timePeriodRequestDto.setToDateTime(timePeriod.getToDateTime());
        String crypto = CryptoEnum.btc.getCode();

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

        when(calculator.calculateMetrics(CryptoEnum.valueOf(crypto), timePeriod)).thenReturn(metrics);

        CryptoMetricsDto response = analyzer.getMetricsForCrypto(Optional.of(timePeriodRequestDto), crypto);
        verify(calculator).calculateMetrics(CryptoEnum.valueOf(crypto), timePeriod);
        verifyNoMoreInteractions(calculator);
        assertEquals(response, expectedResponse);
    }

    @Test
    public void test_getMetricsForCrypto_crypto_not_supported() {
        String crypto = "non-existent-crypto";
        Throwable throwable = assertThrows(CryptoNotFoundException.class,
                () -> analyzer.getMetricsForCrypto(Optional.empty(), crypto));
        assertEquals(throwable.getMessage(), "Crypto " + crypto + " not supported.");
    }

    @Test
    public void test_getCryptoWithHighestNormalizedRange() throws CryptoInternalException {
        TimePeriod timePeriod = new TimePeriod(Optional.empty(), Optional.empty());
        TimePeriodRequestDto timePeriodRequestDto = new TimePeriodRequestDto();
        timePeriodRequestDto.setFromDateTime(timePeriod.getFromDateTime());
        timePeriodRequestDto.setToDateTime(timePeriod.getToDateTime());

        Map<CryptoEnum, BigDecimal> map = new HashMap<>();
        map.put(CryptoEnum.xrp, new BigDecimal("1.75"));
        Optional<Map.Entry<CryptoEnum, BigDecimal>> crypto = Optional.of(map.entrySet().stream().collect(Collectors.toList()).get(0));

        when(calculator.findCryptoWithHighestNormalizedRange(timePeriod)).thenReturn(crypto);

        CryptoMetricDto expectedResponse = new CryptoMetricDto(crypto.get().getKey().getCode(), crypto.get().getValue());
        CryptoMetricDto response = analyzer.getCryptoWithHighestNormalizedRange(Optional.of(timePeriodRequestDto));

        verify(calculator).findCryptoWithHighestNormalizedRange(timePeriod);
        verifyNoMoreInteractions(calculator);
        assertEquals(response, expectedResponse);
    }

    @Test
    public void test_getCryptoWithHighestNormalizedRange_cryptoNotFound() throws CryptoInternalException {
        TimePeriod timePeriod = new TimePeriod(Optional.empty(), Optional.empty());
        TimePeriodRequestDto timePeriodRequestDto = new TimePeriodRequestDto();
        timePeriodRequestDto.setFromDateTime(timePeriod.getFromDateTime());
        timePeriodRequestDto.setToDateTime(timePeriod.getToDateTime());

        when(calculator.findCryptoWithHighestNormalizedRange(timePeriod)).thenReturn(Optional.empty());

        CryptoMetricDto expectedResponse = new CryptoMetricDto(null, null);
        CryptoMetricDto response = analyzer.getCryptoWithHighestNormalizedRange(Optional.of(timePeriodRequestDto));

        verify(calculator).findCryptoWithHighestNormalizedRange(timePeriod);
        verifyNoMoreInteractions(calculator);
        assertEquals(response, expectedResponse);
    }


}
