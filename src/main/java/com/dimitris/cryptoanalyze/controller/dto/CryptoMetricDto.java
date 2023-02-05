package com.dimitris.cryptoanalyze.controller.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO class used for responses, representing a crypto and the value of a metric calculated on this
 * crypto price values
 */
public class CryptoMetricDto {

    private final String crypto;

    private final BigDecimal value;

    public CryptoMetricDto(String crypto, BigDecimal value) {
        this.crypto = crypto;
        this.value = value;
    }

    public String getCrypto() {
        return crypto;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoMetricDto that = (CryptoMetricDto) o;
        return Objects.equals(crypto, that.crypto) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crypto, value);
    }

    @Override
    public String toString() {
        return "CryptoMetricDto{" +
                "crypto='" + crypto + '\'' +
                ", value=" + value +
                '}';
    }
}
