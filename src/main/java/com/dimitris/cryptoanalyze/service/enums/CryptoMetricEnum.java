package com.dimitris.cryptoanalyze.service.enums;

/**
 * Enumeration of metrics upon crypto price series
 */
public enum CryptoMetricEnum {

    OLDEST_PRICE("oldest"),
    NEWEST_PRICE("newest"),
    MIN_PRICE("min"),
    MAX_PRICE("max"),
    NORMALIZED_PRICE_RANGE("normalized range");

    private final String code;

    CryptoMetricEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CryptoMetricEnum{" +
                "code='" + code + '\'' +
                "} " + super.toString();
    }
}
