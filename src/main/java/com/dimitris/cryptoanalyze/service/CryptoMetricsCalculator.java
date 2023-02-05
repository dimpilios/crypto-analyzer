package com.dimitris.cryptoanalyze.service;

import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.enums.CryptoMetricEnum;
import com.dimitris.cryptoanalyze.service.enums.SortOrder;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface abstracting calculations of high level metrics on crypto values
 */
public interface CryptoMetricsCalculator {

    /**
     * Calculates all metrics (oldest/newest/min/max/normalized) for each crypto
     *
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    Map<CryptoEnum, Map<CryptoMetricEnum, BigDecimal>> calculateMetricsPerCrypto(TimePeriod timePeriod) throws CryptoInternalException;

    /**
     * Calculates all metrics (oldest/newest/min/max/normalized) for the specified crypto
     *
     * @param cryptoEnum The crypto
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return A mapping of each metric to its value
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    Map<CryptoMetricEnum, BigDecimal> calculateMetrics(CryptoEnum cryptoEnum, TimePeriod timePeriod) throws CryptoInternalException;

    /**
     * Calculates the specified metric (oldest/newest/min/max/normalized) for the specified crypto
     *
     * @param cryptoEnum       The crypto
     * @param cryptoMetricEnum The metric
     * @param timePeriod       Time period calculation applies to. It contains two optional fields representing start and end
     *                         time points. If any of them is omitted, calculation will be restricted only to the other point
     *                         in time. If both are omitted, calculation has no time restriction.
     * @return The metric value
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    Optional<BigDecimal> calculateMetric(
            CryptoEnum cryptoEnum, CryptoMetricEnum cryptoMetricEnum, TimePeriod timePeriod) throws CryptoInternalException;

    /**
     * Calculates a list of cryptos sorted by the specified metric in the specified order type
     *
     * @param cryptoMetricEnum The metric
     * @param sortOrder        The order type
     * @param timePeriod       Time period calculation applies to. It contains two optional fields representing start and end
     *                         time points. If any of them is omitted, calculation will be restricted only to the other point
     *                         in time. If both are omitted, calculation has no time restriction.
     * @return A list of mappings between crypto and metric value
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    List<Map.Entry<CryptoEnum, BigDecimal>> sortCryptosByMetric(
            CryptoMetricEnum cryptoMetricEnum, SortOrder sortOrder, TimePeriod timePeriod
    ) throws CryptoInternalException;

    /**
     * Calculates the crypto with the highest normalized range
     *
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return An optional mapping of the crypto to its normalized range value (empty if no crypto data found, this
     * can happen if no crypto data exist in specified time period)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    Optional<Map.Entry<CryptoEnum, BigDecimal>> findCryptoWithHighestNormalizedRange(TimePeriod timePeriod) throws CryptoInternalException;
}
