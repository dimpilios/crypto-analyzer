package com.dimitris.cryptoanalyze.controller.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * DTO class used for responses, representing a mapping between metrics and their values, for a specific crypto
 */
public class CryptoMetricsDto {

    private final Map<String, BigDecimal> metrics;

    public CryptoMetricsDto(Map<String, BigDecimal> metrics) {
        this.metrics = metrics;
    }

    public Map<String, BigDecimal> getMetrics() {
        return metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoMetricsDto that = (CryptoMetricsDto) o;
        return Objects.equals(metrics, that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metrics);
    }

    @Override
    public String toString() {
        return "CryptoMetricsDto{" +
                "metrics=" + metrics +
                '}';
    }
}
