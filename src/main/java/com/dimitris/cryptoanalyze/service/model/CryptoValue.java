package com.dimitris.cryptoanalyze.service.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a crypto value. It consists of a timestamp in Epoch millis and a decimal price value
 */
public class CryptoValue {

    private final long timestamp;

    private final BigDecimal price;

    public CryptoValue(long timestamp, BigDecimal price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoValue that = (CryptoValue) o;
        return timestamp == that.timestamp && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, price);
    }

    @Override
    public String toString() {
        return "CryptoValue{" +
                "timestamp=" + timestamp +
                ", price=" + price +
                '}';
    }
}
