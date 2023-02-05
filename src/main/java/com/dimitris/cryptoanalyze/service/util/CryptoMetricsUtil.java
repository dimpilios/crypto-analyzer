package com.dimitris.cryptoanalyze.service.util;

import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Utility class providing metric calculations on crypto price values
 */
public class CryptoMetricsUtil {

    /**
     * Calculates oldest price
     * @param values A set of crypto price values provided
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    public static Optional<BigDecimal> calculateOldestPrice(Set<CryptoValue> values, TimePeriod timePeriod) {
        Stream<CryptoValue> stream = getValuesStreamInPeriod(values, timePeriod);
        Optional<CryptoValue> cryptoValue = stream.min(Comparator.comparingLong(CryptoValue::getTimestamp));
        return cryptoValue.isPresent() ? Optional.of(cryptoValue.get().getPrice()) : Optional.empty();
    }

    /**
     * Calculates newest price
     * @param values A set of crypto price values provided
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    public static Optional<BigDecimal> calculateNewestPrice(Set<CryptoValue> values, TimePeriod timePeriod) {
        Stream<CryptoValue> stream = getValuesStreamInPeriod(values, timePeriod);
        Optional<CryptoValue> cryptoValue = stream.max(Comparator.comparingLong(CryptoValue::getTimestamp));
        return cryptoValue.isPresent() ? Optional.of(cryptoValue.get().getPrice()) : Optional.empty();
    }

    /**
     * Calculates minimum price
     * @param values A set of crypto price values provided
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    public static Optional<BigDecimal> calculateMinPrice(Set<CryptoValue> values, TimePeriod timePeriod) {
        Stream<CryptoValue> stream = getValuesStreamInPeriod(values, timePeriod);
        Optional<CryptoValue> cryptoValue = stream.min(Comparator.comparing(CryptoValue::getPrice));
        return cryptoValue.isPresent() ? Optional.of(cryptoValue.get().getPrice()) : Optional.empty();
    }

    /**
     * Calculates maximum price
     * @param values A set of crypto price values provided
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    public static Optional<BigDecimal> calculateMaxPrice(Set<CryptoValue> values, TimePeriod timePeriod) {
        Stream<CryptoValue> stream = getValuesStreamInPeriod(values, timePeriod);
        Optional<CryptoValue> cryptoValue = stream.max(Comparator.comparing(CryptoValue::getPrice));
        return cryptoValue.isPresent() ? Optional.of(cryptoValue.get().getPrice()) : Optional.empty();
    }

    /**
     * Calculates normalized price range
     * @param values A set of crypto price values provided
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    public static Optional<BigDecimal> calculateNormalizedPriceRange(Set<CryptoValue> values, TimePeriod timePeriod) {
        Stream<CryptoValue> stream = getValuesStreamInPeriod(values, timePeriod);
        Optional<CryptoValue> cryptoValueWithMinPrice = stream.min(Comparator.comparing(CryptoValue::getPrice));

        stream = getValuesStreamInPeriod(values, timePeriod);
        Optional<CryptoValue> cryptoValueWithMaxPrice = stream.max(Comparator.comparing(CryptoValue::getPrice));

        if (cryptoValueWithMinPrice.isEmpty() || cryptoValueWithMaxPrice.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal min = cryptoValueWithMinPrice.get().getPrice();
        BigDecimal max = cryptoValueWithMaxPrice.get().getPrice();
        BigDecimal normalized = max.subtract(min).divide(min, new MathContext(5, RoundingMode.HALF_UP));
        return Optional.of(normalized);
    }

    /**
     * Create and return a new {@link Stream} of {@link CryptoValue} values filtered by a specified time period
     * Used by calculation methods as a starting point on which to execute further calculations
     * @param values A set of crypto price values provided
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    private static Stream<CryptoValue> getValuesStreamInPeriod(Set<CryptoValue> values, TimePeriod timePeriod) {
        Stream<CryptoValue> stream = values.stream();
        Optional<LocalDateTime> fromDateTime = timePeriod.getFromDateTime();
        Optional<LocalDateTime> toDateTime = timePeriod.getToDateTime();
        if (fromDateTime.isPresent() && toDateTime.isPresent()) {
            stream = stream.filter(value -> TimeUtil.timestampInBetween(fromDateTime.get(), toDateTime.get(), value.getTimestamp()));
        } else if (fromDateTime.isPresent() && toDateTime.isEmpty()) {
            stream = stream.filter(value -> TimeUtil.timestampNewerThan(value.getTimestamp(), fromDateTime.get()));
        } else if (fromDateTime.isEmpty() && toDateTime.isPresent()) {
            stream = stream.filter(value -> !TimeUtil.timestampNewerThan(value.getTimestamp(), toDateTime.get()));
        }
        return stream;
    }


}
