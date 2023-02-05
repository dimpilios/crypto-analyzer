package com.dimitris.cryptoanalyze.service.enums;

/**
 * Enumeration of cryptos
 */
public enum CryptoEnum {

    btc("btc"),
    doge("doge"),
    eth("eth"),
    ltc("ltc"),
    xrp("xrp");

    private final String code;

    CryptoEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CryptoEnum{" +
                "code='" + code + '\'' +
                "} " + super.toString();
    }
}
