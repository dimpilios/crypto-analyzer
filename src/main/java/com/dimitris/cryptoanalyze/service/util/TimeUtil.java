package com.dimitris.cryptoanalyze.service.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Utility class providing time operations
 */
public class TimeUtil {

    /**
     * Returns whether a specified timestamp belongs to a specified time period. Returns true if timestamp
     * equals period start point but returns false if it equals period end point
     * @param fromDateTime The time period start point
     * @param toDateTime The time period end point
     * @param timestamp The timestamp in Epoch millis
     * @return
     */
    public static boolean timestampInBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime, long timestamp) {
        Instant fromInstant = fromDateTime.toInstant(ZoneOffset.UTC);
        Instant toInstant = toDateTime.toInstant(ZoneOffset.UTC);
        Instant valueInstant = Instant.ofEpochMilli(timestamp);
        return valueInstant.equals(fromInstant) || (valueInstant.isAfter(fromInstant) && valueInstant.isBefore(toInstant));
    }

    /**
     * Returns whether a specified timestamp is newer than a specified point in time. Returns true if timestamp
     * equals time point
     * @param timestamp The timestamp in Epoch millis
     * @param dateTime The point in time
     * @return
     */
    public static boolean timestampNewerThan(long timestamp, LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        Instant timestampInstant = Instant.ofEpochMilli(timestamp);
        return timestampInstant.equals(instant) || timestampInstant.isAfter(instant);
    }
}
