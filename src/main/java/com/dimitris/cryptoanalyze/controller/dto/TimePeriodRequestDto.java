package com.dimitris.cryptoanalyze.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * DTO class used for end user request, representing a period between two points in time
 * User must specify time points in the format "yyyy-MM-dd HH:mm:ss"
 */
public class TimePeriodRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Optional<LocalDateTime> fromDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Optional<LocalDateTime> toDateTime;

    public Optional<LocalDateTime> getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(Optional<LocalDateTime> fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public Optional<LocalDateTime> getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(Optional<LocalDateTime> toDateTime) {
        this.toDateTime = toDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriodRequestDto that = (TimePeriodRequestDto) o;
        return Objects.equals(fromDateTime, that.fromDateTime) && Objects.equals(toDateTime, that.toDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromDateTime, toDateTime);
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "fromDateTime=" + fromDateTime +
                ", toDateTime=" + toDateTime +
                '}';
    }
}
