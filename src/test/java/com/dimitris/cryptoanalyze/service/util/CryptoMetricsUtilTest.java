package com.dimitris.cryptoanalyze.service.util;

import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoMetricsUtilTest {

    private static final Set<CryptoValue> cryptoValues = Set.of(
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 00:00:05"), new BigDecimal("17.12345678")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 02:45:00"), new BigDecimal("19.1234")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-01 16:21:05"), new BigDecimal("16")),

            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-02 05:30:55"), new BigDecimal("14")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-02 17:00:50"), new BigDecimal("21.0001")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-02 23:34:00"), new BigDecimal("19")),

            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-03 03:00:00"), new BigDecimal("17")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-03 06:34:05"), new BigDecimal("21")),
            new CryptoValue(TimeTestUtil.toEpochMilli("2022-01-03 13:00:00"), new BigDecimal("22"))
    );

    private static Stream<Arguments> provideInputsAndOutputFor_test_calculateOldestPrice() {
        return Stream.of(
                Arguments.of(cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("17.12345678"))),
                Arguments.of(
                        new HashSet<>(),
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 01:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-01 01:10:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00")), Optional.empty()),
                        Optional.of(new BigDecimal("14"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("17.12345678"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.empty()),
                        Optional.of(new BigDecimal("17.12345678")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_calculateOldestPrice")
    public void test_calculateOldestPrice(Set<CryptoValue> cryptoValues, TimePeriod timePeriod, Optional<BigDecimal> expected) {
        assertEquals(CryptoMetricsUtil.calculateOldestPrice(cryptoValues, timePeriod), expected);
    }

    private static Stream<Arguments> provideInputsAndOutputFor_test_calculateNewestPrice() {
        return Stream.of(
                Arguments.of(cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("16"))),
                Arguments.of(
                        new HashSet<>(),
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 01:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-01 01:10:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00")), Optional.empty()),
                        Optional.of(new BigDecimal("22"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("16"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.empty()),
                        Optional.of(new BigDecimal("22")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_calculateNewestPrice")
    public void test_calculateNewestPrice(Set<CryptoValue> cryptoValues, TimePeriod timePeriod, Optional<BigDecimal> expected) {
        assertEquals(CryptoMetricsUtil.calculateNewestPrice(cryptoValues, timePeriod), expected);
    }

    private static Stream<Arguments> provideInputsAndOutputFor_test_calculateMinPrice() {
        return Stream.of(
                Arguments.of(cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("16"))),
                Arguments.of(
                        new HashSet<>(),
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 01:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-01 01:10:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00")), Optional.empty()),
                        Optional.of(new BigDecimal("14"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("16"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.empty()),
                        Optional.of(new BigDecimal("14")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_calculateMinPrice")
    public void test_calculateMinPrice(Set<CryptoValue> cryptoValues, TimePeriod timePeriod, Optional<BigDecimal> expected) {
        assertEquals(CryptoMetricsUtil.calculateMinPrice(cryptoValues, timePeriod), expected);
    }

    private static Stream<Arguments> provideInputsAndOutputFor_test_calculateMaxPrice() {
        return Stream.of(
                Arguments.of(cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("19.1234"))),
                Arguments.of(
                        new HashSet<>(),
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 01:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-01 01:10:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00")), Optional.empty()),
                        Optional.of(new BigDecimal("22"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("19.1234"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.empty()),
                        Optional.of(new BigDecimal("22")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_calculateMaxPrice")
    public void test_calculateMaxPrice(Set<CryptoValue> cryptoValues, TimePeriod timePeriod, Optional<BigDecimal> expected) {
        assertEquals(CryptoMetricsUtil.calculateMaxPrice(cryptoValues, timePeriod), expected);
    }

    private static Stream<Arguments> provideInputsAndOutputFor_test_calculateNormalPrice() {
        return Stream.of(
                Arguments.of(cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("0.19521"))),
                Arguments.of(
                        new HashSet<>(),
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 00:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-01 01:00:00")), Optional.of(TimeTestUtil.toLtd("2022-01-01 01:10:00"))),
                        Optional.empty()),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00")), Optional.empty()),
                        Optional.of(new BigDecimal("0.57143"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.of(TimeTestUtil.toLtd("2022-01-02 00:00:00"))),
                        Optional.of(new BigDecimal("0.19521"))),
                Arguments.of(
                        cryptoValues,
                        new TimePeriod(Optional.empty(), Optional.empty()),
                        Optional.of(new BigDecimal("0.57143")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_calculateNormalPrice")
    public void test_calculateNormalPrice(Set<CryptoValue> cryptoValues, TimePeriod timePeriod, Optional<BigDecimal> expected) {
        assertEquals(CryptoMetricsUtil.calculateNormalizedPriceRange(cryptoValues, timePeriod), expected);
    }


}
