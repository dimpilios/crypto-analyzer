package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CryptoValuesCsvReaderTest {

    private static final CryptoValuesCsvReader reader = new CryptoValuesCsvReader();

    private static final List<CryptoValue> expectedValues = List.of(
            new CryptoValue(1641009600000L, new BigDecimal("46813.21")),
            new CryptoValue(1641020400000L, new BigDecimal("46979.61")),
            new CryptoValue(1641031200000L, new BigDecimal("47143.98")),
            new CryptoValue(1641034800000L, new BigDecimal("46871.09")),
            new CryptoValue(1641045600000L, new BigDecimal("47023.24")),
            new CryptoValue(1641081600000L, new BigDecimal("47722.66")),
            new CryptoValue(1641160800000L, new BigDecimal("47017.98"))
    );

    @Test
    public void test_read_ok() throws CryptoInternalException {
        List<CryptoValue> values = reader.read(CryptoEnum.btc, "prices\\test_ok.csv");
        assertEquals(values, expectedValues);
    }

    @Test
    public void test_read_header_missing_from_first_line() throws CryptoInternalException {
        Throwable throwable = assertThrows(CryptoInternalException.class,
                () -> reader.read(CryptoEnum.btc, "prices\\test_header_missing.csv"));
        assertEquals(throwable.getMessage(), "Error reading csv file. Header line is missing");
    }

    @Test
    public void test_read_column_missing() throws CryptoInternalException {
        Throwable throwable = assertThrows(CryptoInternalException.class,
                () -> reader.read(CryptoEnum.btc, "prices\\test_column_missing.csv"));
        assertEquals(throwable.getMessage(), "Error reading csv file. Entries with missing columns found");
    }

    @Test
    public void test_read_column_value_blank() throws CryptoInternalException {
        Throwable throwable = assertThrows(CryptoInternalException.class,
                () -> reader.read(CryptoEnum.btc, "prices\\test_column_value_blank.csv"));
        assertEquals(throwable.getMessage(), "Error reading csv file. Entries with missing columns found");
    }
}
