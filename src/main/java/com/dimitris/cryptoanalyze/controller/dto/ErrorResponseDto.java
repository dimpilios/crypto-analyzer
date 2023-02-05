package com.dimitris.cryptoanalyze.controller.dto;

import java.util.Objects;

/**
 * DTO class used for exception responses
 */
public class ErrorResponseDto {

    private final String errorMessage;

    public ErrorResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponseDto that = (ErrorResponseDto) o;
        return Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }

    @Override
    public String toString() {
        return "ErrorResponseDto{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
