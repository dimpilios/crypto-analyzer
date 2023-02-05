package com.dimitris.cryptoanalyze.service.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a period between two points in time
 */
public class TimePeriod {

    private final Optional<LocalDateTime> fromDateTime;

    private final Optional<LocalDateTime> toDateTime;

    public TimePeriod(Optional<LocalDateTime> fromDateTime, Optional<LocalDateTime> toDateTime) {
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    public Optional<LocalDateTime> getFromDateTime() {
        return fromDateTime;
    }

    public Optional<LocalDateTime> getToDateTime() {
        return toDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return Objects.equals(fromDateTime, that.fromDateTime) && Objects.equals(toDateTime, that.toDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromDateTime, toDateTime);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "fromDateTime=" + fromDateTime +
                ", toDateTime=" + toDateTime +
                '}';
    }
}
