package com.dimitris.cryptoanalyze.service.util;

import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilTest {

    private static Stream<Arguments> provideInputsAndOutputFor_test_timestampInBetween() {
        return Stream.of(
                Arguments.of(
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:30:00"),
                        TimeTestUtil.toEpochMilli("2022-01-01 01:00:00"),
                        true),
                Arguments.of(
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:30:00"),
                        TimeTestUtil.toEpochMilli("2022-01-01 01:10:00"),
                        true),
                Arguments.of(
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:30:00"),
                        TimeTestUtil.toEpochMilli("2022-01-01 01:30:00"),
                        false),
                Arguments.of(
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:30:00"),
                        TimeTestUtil.toEpochMilli("2022-01-01 01:40:00"),
                        false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_timestampInBetween")
    public void test_timestampInBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime, long timestamp, boolean expected) {
        assertEquals(TimeUtil.timestampInBetween(fromDateTime, toDateTime, timestamp), expected);
    }

    private static Stream<Arguments> provideInputsAndOutputFor_test_timestampNewerThan() {
        return Stream.of(
                Arguments.of(
                        TimeTestUtil.toEpochMilli("2022-01-01 00:59:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        false),
                Arguments.of(
                        TimeTestUtil.toEpochMilli("2022-01-01 01:00:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        true),
                Arguments.of(
                        TimeTestUtil.toEpochMilli("2022-01-01 01:30:00"),
                        TimeTestUtil.toLtd("2022-01-01 01:00:00"),
                        true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInputsAndOutputFor_test_timestampNewerThan")
    public void test_timestampNewerThan(long timestamp, LocalDateTime dateTime, boolean expected) {
        assertEquals(TimeUtil.timestampNewerThan(timestamp, dateTime), expected);
    }
}
