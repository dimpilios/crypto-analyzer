package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.CryptoValuesManager;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.enums.CryptoMetricEnum;
import com.dimitris.cryptoanalyze.service.enums.SortOrder;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;
import com.dimitris.cryptoanalyze.service.util.TimeTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CryptoMetricsCalculatorImplTest {

    // normalized: 0.57143
    private static final Optional<Set<CryptoValue>> cryptoValues = Optional.of(Set.of(
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 00:00:05"), new BigDecimal("17.12345678")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 02:45:00"), new BigDecimal("19.1234")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 16:21:05"), new BigDecimal("16")),

            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-02 05:30:55"), new BigDecimal("14")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-02 17:00:50"), new BigDecimal("21.0001")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-02 23:34:00"), new BigDecimal("19")),

            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-03 03:00:00"), new BigDecimal("17")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-03 06:34:05"), new BigDecimal("21")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-03 13:00:00"), new BigDecimal("22"))
    ));

    // normalized: 0.36
    private static final Optional<Set<CryptoValue>> cryptoValues2 = Optional.of(Set.of(
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 00:00:05"), new BigDecimal("25")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 02:45:00"), new BigDecimal("34"))
    ));

    // normalized: 0.24444
    private static final Optional<Set<CryptoValue>> cryptoValues3 = Optional.of(Set.of(
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 00:00:05"), new BigDecimal("56")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 02:45:00"), new BigDecimal("45"))
    ));

    // normalized: 0.21782
    private static final Optional<Set<CryptoValue>> cryptoValues4 = Optional.of(Set.of(
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 00:00:05"), new BigDecimal("101")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 02:45:00"), new BigDecimal("123"))
    ));

    // normalized: 1.75
    private static final Optional<Set<CryptoValue>> cryptoValues5 = Optional.of(Set.of(
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 00:00:05"), new BigDecimal("4")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 02:45:00"), new BigDecimal("11"))
    ));

    @Mock
    private CryptoValuesManager manager;

    @InjectMocks
    private CryptoMetricsCalculatorImpl calculator;

    private static Stream<Arguments> provideInputsAndOutputFor_test_calculateMetric_valuesExist() {
        return Stream.of(
                Arguments.of(CryptoMetricEnum.OLDEST_PRICE, Optional.of(new BigDecimal("17.12345678"))),
                Arguments.of(CryptoMetricEnum.NEWEST_PRICE, Optional.of(new BigDecimal("22"))),
                Arguments.of(CryptoMetricEnum.MIN_PRICE, Optional.of(new BigDecimal("14"))),
                Arguments.of(CryptoMetricEnum.MAX_PRICE, Optional.of(new BigDecimal("22"))),
                Arguments.of(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, Optional.of(new BigDecimal("0.57143")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_calculateMetric_valuesExist")
    public void test_calculateMetric_valuesExist(CryptoMetricEnum cryptoMetricEnum, Optional<BigDecimal> expectedMetric) throws CryptoInternalException {
        when(manager.getValues(CryptoEnum.btc)).thenReturn(cryptoValues);
        Optional<BigDecimal> metric = calculator.calculateMetric(CryptoEnum.btc, cryptoMetricEnum, new TimePeriod(Optional.empty(), Optional.empty()));

        verify(manager).getValues(CryptoEnum.btc);
        verifyNoMoreInteractions(manager);
        assertEquals(metric, expectedMetric);
    }

    @Test
    public void test_calculateMetric_valuesDoNotExist() {
        when(manager.getValues(CryptoEnum.btc)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(CryptoInternalException.class,
                () -> calculator.calculateMetric(CryptoEnum.btc, CryptoMetricEnum.OLDEST_PRICE, new TimePeriod(Optional.empty(), Optional.empty())));
        assertEquals(throwable.getMessage(), "Error calculating metric. No values found for crypto");
    }

    @Test
    public void test_calculateMetrics_valuesExist() throws CryptoInternalException {
        when(manager.getValues(CryptoEnum.btc)).thenReturn(cryptoValues);

        Map<CryptoMetricEnum, BigDecimal> expectedMetrics = new HashMap<>();
        expectedMetrics.put(CryptoMetricEnum.OLDEST_PRICE, new BigDecimal("17.12345678"));
        expectedMetrics.put(CryptoMetricEnum.NEWEST_PRICE, new BigDecimal("22"));
        expectedMetrics.put(CryptoMetricEnum.MIN_PRICE, new BigDecimal("14"));
        expectedMetrics.put(CryptoMetricEnum.MAX_PRICE, new BigDecimal("22"));
        expectedMetrics.put(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, new BigDecimal("0.57143"));

        Map<CryptoMetricEnum, BigDecimal> metrics = calculator.calculateMetrics(CryptoEnum.btc, new TimePeriod(Optional.empty(), Optional.empty()));
        assertEquals(metrics, expectedMetrics);
    }

    @Test
    public void test_calculateMetrics_valuesDoNotExist() throws CryptoInternalException {
        when(manager.getValues(CryptoEnum.btc)).thenReturn(cryptoValues);
        TimePeriod timePeriod = new TimePeriod(
                Optional.of(TimeTestUtil.toLtd("2022-01-11 00:00:00")),
                Optional.of(TimeTestUtil.toLtd("2022-01-12 00:00:00")));

        Map<CryptoMetricEnum, BigDecimal> metrics = calculator.calculateMetrics(CryptoEnum.btc, timePeriod);
        assertTrue(metrics.isEmpty());
    }

    @Test
    public void test_calculateMetricsPerCrypto() throws CryptoInternalException {
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            when(manager.getValues(cryptoEnum)).thenReturn(cryptoValues);
        }

        Map<CryptoEnum, Map<CryptoMetricEnum, BigDecimal>> expectedMetrics = new HashMap<>();
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            Map<CryptoMetricEnum, BigDecimal> map = new HashMap<>();
            map.put(CryptoMetricEnum.OLDEST_PRICE, new BigDecimal("17.12345678"));
            map.put(CryptoMetricEnum.NEWEST_PRICE, new BigDecimal("22"));
            map.put(CryptoMetricEnum.MIN_PRICE, new BigDecimal("14"));
            map.put(CryptoMetricEnum.MAX_PRICE, new BigDecimal("22"));
            map.put(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, new BigDecimal("0.57143"));
            expectedMetrics.put(cryptoEnum, map);
        }

        Map<CryptoEnum, Map<CryptoMetricEnum, BigDecimal>> metrics =
                calculator.calculateMetricsPerCrypto(new TimePeriod(Optional.empty(), Optional.empty()));
        assertEquals(metrics, expectedMetrics);
    }

    @Test
    public void test_sortCryptosByMetric_normalizedDesc() throws CryptoInternalException {
        when(manager.getValues(CryptoEnum.btc)).thenReturn(cryptoValues);
        when(manager.getValues(CryptoEnum.doge)).thenReturn(cryptoValues2);
        when(manager.getValues(CryptoEnum.eth)).thenReturn(cryptoValues3);
        when(manager.getValues(CryptoEnum.ltc)).thenReturn(cryptoValues4);
        when(manager.getValues(CryptoEnum.xrp)).thenReturn(cryptoValues5);

        Map<CryptoEnum, BigDecimal> map = new HashMap<>();
        map.put(CryptoEnum.btc, new BigDecimal("0.57143"));
        map.put(CryptoEnum.doge, new BigDecimal("0.36"));
        map.put(CryptoEnum.eth, new BigDecimal("0.24444"));
        map.put(CryptoEnum.ltc, new BigDecimal("0.21782"));
        map.put(CryptoEnum.xrp, new BigDecimal("1.75"));
        List<Map.Entry<CryptoEnum, BigDecimal>> expectedCryptos = map.entrySet().stream().collect(Collectors.toList());

        List<Map.Entry<CryptoEnum, BigDecimal>> cryptos = calculator.sortCryptosByMetric(
                CryptoMetricEnum.NORMALIZED_PRICE_RANGE, SortOrder.DESC, new TimePeriod(Optional.empty(), Optional.empty()));

        assertEquals(cryptos.stream().collect(Collectors.toSet()), expectedCryptos.stream().collect(Collectors.toSet()));
    }

    @Test
    public void test_findCryptoWithHighestNormalizedRange() throws CryptoInternalException {
        when(manager.getValues(CryptoEnum.btc)).thenReturn(cryptoValues);
        when(manager.getValues(CryptoEnum.doge)).thenReturn(cryptoValues2);
        when(manager.getValues(CryptoEnum.eth)).thenReturn(cryptoValues3);
        when(manager.getValues(CryptoEnum.ltc)).thenReturn(cryptoValues4);
        when(manager.getValues(CryptoEnum.xrp)).thenReturn(cryptoValues5);

        Map<CryptoEnum, BigDecimal> map = new HashMap<>();
        map.put(CryptoEnum.xrp, new BigDecimal("1.75"));
        Optional<Map.Entry<CryptoEnum, BigDecimal>> expectedCrypto = Optional.of(map.entrySet().stream().collect(Collectors.toList()).get(0));

        Optional<Map.Entry<CryptoEnum, BigDecimal>> crypto = calculator.findCryptoWithHighestNormalizedRange(new TimePeriod(Optional.empty(), Optional.empty()));
        assertEquals(crypto, expectedCrypto);
    }

}
