package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.CryptoMetricsCalculator;
import com.dimitris.cryptoanalyze.service.CryptoValuesManager;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.enums.CryptoMetricEnum;
import com.dimitris.cryptoanalyze.service.enums.SortOrder;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;
import com.dimitris.cryptoanalyze.service.util.CryptoMetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for performing calculations of high level metrics on crypto values
 */
@Service
public class CryptoMetricsCalculatorImpl implements CryptoMetricsCalculator {

    private final CryptoValuesManager cryptoValuesManager;

    /**
     * @param cryptoValuesManager A crypto values manager from which stored crypto values can be retrieved
     */
    @Autowired
    public CryptoMetricsCalculatorImpl(CryptoValuesManager cryptoValuesManager) {
        this.cryptoValuesManager = cryptoValuesManager;
    }

    /**
     * Calculates all metrics (oldest/newest/min/max/normalized) for each crypto
     *
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    @Override
    public Map<CryptoEnum, Map<CryptoMetricEnum, BigDecimal>> calculateMetricsPerCrypto(TimePeriod timePeriod) throws CryptoInternalException {
        Map<CryptoEnum, Map<CryptoMetricEnum, BigDecimal>> metricsPerCrypto = new HashMap<>();
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            Map<CryptoMetricEnum, BigDecimal> metrics = calculateMetrics(cryptoEnum, timePeriod);
            metricsPerCrypto.put(cryptoEnum, metrics);
        }
        return metricsPerCrypto;
    }

    /**
     * Calculates all metrics (oldest/newest/min/max/normalized) for the specified crypto
     *
     * @param cryptoEnum The crypto
     * @param timePeriod Time period calculation applies to. It contains two optional fields representing start and end
     *                   time points. If any of them is omitted, calculation will be restricted only to the other point
     *                   in time. If both are omitted, calculation has no time restriction.
     * @return
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    @Override
    public Map<CryptoMetricEnum, BigDecimal> calculateMetrics(CryptoEnum cryptoEnum, TimePeriod timePeriod) throws CryptoInternalException {
        Map<CryptoMetricEnum, BigDecimal> metrics = new HashMap<>();
        for (CryptoMetricEnum cryptoMetricEnum : CryptoMetricEnum.values()) {
            Optional<BigDecimal> metricValue = calculateMetric(cryptoEnum, cryptoMetricEnum, timePeriod);
            if (metricValue.isPresent()) {
                metrics.put(cryptoMetricEnum, metricValue.get());
            }
        }
        return metrics;
    }

    /**
     * Calculates the specified metric (oldest/newest/min/max/normalized) for the specified crypto
     *
     * @param cryptoEnum       The crypto
     * @param cryptoMetricEnum The metric
     * @param timePeriod       Time period calculation applies to. It contains two optional fields representing start and end
     *                         time points. If any of them is omitted, calculation will be restricted only to the other point
     *                         in time. If both are omitted, calculation has no time restriction.
     * @return
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    @Override
    public Optional<BigDecimal> calculateMetric(
            CryptoEnum cryptoEnum, CryptoMetricEnum cryptoMetricEnum, TimePeriod timePeriod
    ) throws CryptoInternalException {
        Optional<Set<CryptoValue>> cryptoValues = cryptoValuesManager.getValues(cryptoEnum);
        if (cryptoValues.isEmpty()) {
            throw new CryptoInternalException("Error calculating metric. No values found for crypto");
        }
        switch (cryptoMetricEnum) {
            case OLDEST_PRICE:
                return CryptoMetricsUtil.calculateOldestPrice(cryptoValues.get(), timePeriod);
            case NEWEST_PRICE:
                return CryptoMetricsUtil.calculateNewestPrice(cryptoValues.get(), timePeriod);
            case MIN_PRICE:
                return CryptoMetricsUtil.calculateMinPrice(cryptoValues.get(), timePeriod);
            case MAX_PRICE:
                return CryptoMetricsUtil.calculateMaxPrice(cryptoValues.get(), timePeriod);
            case NORMALIZED_PRICE_RANGE:
                return CryptoMetricsUtil.calculateNormalizedPriceRange(cryptoValues.get(), timePeriod);
            default:
                throw new CryptoInternalException("Error calculating non-supported metric " + cryptoMetricEnum.getCode());
        }
    }

    /**
     * Calculates a list of cryptos sorted by the specified metric in the specified order type
     *
     * @param cryptoMetricEnum The metric
     * @param sortOrder        The order type
     * @param timePeriod       Time period calculation applies to. It contains two optional fields representing start and end
     *                         time points. If any of them is omitted, calculation will be restricted only to the other point
     *                         in time. If both are omitted, calculation has no time restriction.
     * @return
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    @Override
    public List<Map.Entry<CryptoEnum, BigDecimal>> sortCryptosByMetric(
            CryptoMetricEnum cryptoMetricEnum, SortOrder sortOrder, TimePeriod timePeriod
    ) throws CryptoInternalException {
        Map<CryptoEnum, BigDecimal> metricPerCrypto = new HashMap<>();
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            Optional<BigDecimal> metricValue = calculateMetric(cryptoEnum, cryptoMetricEnum, timePeriod);
            if (metricValue.isPresent()) {
                metricPerCrypto.put(cryptoEnum, metricValue.get());
            }
        }

        List<Map.Entry<CryptoEnum, BigDecimal>> sortedEntries = metricPerCrypto.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
        if (sortOrder == SortOrder.DESC) {
            Collections.reverse(sortedEntries);
        }

        return sortedEntries;
    }

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
    @Override
    public Optional<Map.Entry<CryptoEnum, BigDecimal>> findCryptoWithHighestNormalizedRange(TimePeriod timePeriod) throws CryptoInternalException {
        List<Map.Entry<CryptoEnum, BigDecimal>> cryptosSortedByNormalizedRangeDesc =
                sortCryptosByMetric(CryptoMetricEnum.NORMALIZED_PRICE_RANGE, SortOrder.DESC, timePeriod);
        return cryptosSortedByNormalizedRangeDesc.isEmpty() ? Optional.empty() : Optional.of(cryptosSortedByNormalizedRangeDesc.get(0));
    }
}
