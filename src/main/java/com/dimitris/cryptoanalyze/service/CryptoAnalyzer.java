package com.dimitris.cryptoanalyze.service;

import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsListDto;
import com.dimitris.cryptoanalyze.controller.dto.TimePeriodRequestDto;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.exception.CryptoNotFoundException;

import java.util.Optional;

/**
 * Service interface abstracting calls of high level calculations according to endpoint requests and mapping results
 * to endpoint responses
 */
public interface CryptoAnalyzer {

    /**
     * Calculates cryptos list sorted by normalized range in descending order
     *
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @return A list of mappings between crypto and its normalized range value (no mappings exist for cryptos for
     * which no values are found)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    CryptoMetricsListDto getCryptosSortedByNormalizedRangeDesc(Optional<TimePeriodRequestDto> timePeriodRequestDto)
            throws CryptoInternalException;

    /**
     * Calculates metrics for specified crypto
     *
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @param crypto               The crypto
     * @return A mapping of each metric to corresponding value (empty map if no values found for crypto)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     * @throws CryptoNotFoundException Requested crypto is not supported
     */
    CryptoMetricsDto getMetricsForCrypto(Optional<TimePeriodRequestDto> timePeriodRequestDto, String crypto)
            throws CryptoInternalException, CryptoNotFoundException;

    /**
     * Finds crypto with highest normalized range
     *
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @return A mapping of the crypto to its normalized range value (if no crypto data found, mapping contains null as values)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    CryptoMetricDto getCryptoWithHighestNormalizedRange(Optional<TimePeriodRequestDto> timePeriodRequestDto)
            throws CryptoInternalException;
}
