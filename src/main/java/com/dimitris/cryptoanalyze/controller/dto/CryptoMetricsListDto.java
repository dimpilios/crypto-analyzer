package com.dimitris.cryptoanalyze.controller.dto;

import java.util.List;
import java.util.Objects;

/**
 * DTO class used for responses, representing a list of {@link CryptoMetricDto}, ie a list of cryptos and their
 * values for a specified metric
 */
public class CryptoMetricsListDto {

    private final List<CryptoMetricDto> cryptos;

    public CryptoMetricsListDto(List<CryptoMetricDto> cryptos) {
        this.cryptos = cryptos;
    }

    public List<CryptoMetricDto> getCryptos() {
        return cryptos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoMetricsListDto that = (CryptoMetricsListDto) o;
        return Objects.equals(cryptos, that.cryptos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cryptos);
    }

    @Override
    public String toString() {
        return "CryptoMetricsListDto{" +
                "cryptos=" + cryptos +
                '}';
    }
}
