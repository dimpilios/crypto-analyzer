package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsDto;
import com.dimitris.cryptoanalyze.controller.dto.CryptoMetricsListDto;
import com.dimitris.cryptoanalyze.controller.dto.TimePeriodRequestDto;
import com.dimitris.cryptoanalyze.service.CryptoAnalyzer;
import com.dimitris.cryptoanalyze.service.CryptoMetricsCalculator;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.enums.CryptoMetricEnum;
import com.dimitris.cryptoanalyze.service.enums.SortOrder;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.exception.CryptoNotFoundException;
import com.dimitris.cryptoanalyze.service.model.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for calling calculations according to endpoint requests and mapping results
 * to endpoint responses
 */
@Service
public class CryptoAnalyzerImpl implements CryptoAnalyzer {

    private final CryptoMetricsCalculator cryptoMetricsCalculator;

    /**
     * @param cryptoMetricsCalculator A metrics calculator to be called for calculations
     */
    @Autowired
    public CryptoAnalyzerImpl(CryptoMetricsCalculator cryptoMetricsCalculator) {
        this.cryptoMetricsCalculator = cryptoMetricsCalculator;
    }

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
    @Override
    public CryptoMetricsListDto getCryptosSortedByNormalizedRangeDesc(Optional<TimePeriodRequestDto> timePeriodRequestDto)
            throws CryptoInternalException {
        List<Map.Entry<CryptoEnum, BigDecimal>> entries = cryptoMetricsCalculator.sortCryptosByMetric(
                CryptoMetricEnum.NORMALIZED_PRICE_RANGE, SortOrder.DESC, convertToTimePeriod(timePeriodRequestDto));
        List<CryptoMetricDto> cryptoMetricDtoList = entries
                .stream()
                .map(entry -> new CryptoMetricDto(entry.getKey().getCode(), entry.getValue()))
                .collect(Collectors.toList());
        return new CryptoMetricsListDto(cryptoMetricDtoList);
    }

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
    @Override
    public CryptoMetricsDto getMetricsForCrypto(Optional<TimePeriodRequestDto> timePeriodRequestDto, String crypto)
            throws CryptoInternalException, CryptoNotFoundException {
        if (Arrays.stream(CryptoEnum.values()).noneMatch(e -> e.getCode().equals(crypto))) {
            throw new CryptoNotFoundException("Crypto " + crypto + " not supported.");
        }
        Map<CryptoMetricEnum, BigDecimal> metrics = cryptoMetricsCalculator.calculateMetrics(
                CryptoEnum.valueOf(crypto), convertToTimePeriod(timePeriodRequestDto));
        Map<String, BigDecimal> metricsMap = metrics.entrySet()
                .stream()
                .collect(Collectors.toMap((entry) -> entry.getKey().getCode(), Map.Entry::getValue));
        return new CryptoMetricsDto(metricsMap);
    }

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
    @Override
    public CryptoMetricDto getCryptoWithHighestNormalizedRange(Optional<TimePeriodRequestDto> timePeriodRequestDto)
            throws CryptoInternalException {
        Optional<Map.Entry<CryptoEnum, BigDecimal>> cryptoMetric = cryptoMetricsCalculator.findCryptoWithHighestNormalizedRange(
                convertToTimePeriod(timePeriodRequestDto));
        if (cryptoMetric.isPresent()) {
            return new CryptoMetricDto(cryptoMetric.get().getKey().getCode(), cryptoMetric.get().getValue());
        } else {
            return new CryptoMetricDto(null, null);
        }
    }

    /**
     * DTO-2-Entity converter, converts a {@link TimePeriodRequestDto} to {@link TimePeriod}
     *
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @return
     */
    private TimePeriod convertToTimePeriod(Optional<TimePeriodRequestDto> timePeriodRequestDto) {
        Optional<LocalDateTime> fromDateTime = timePeriodRequestDto.isPresent() ? timePeriodRequestDto.get().getFromDateTime() : Optional.empty();
        Optional<LocalDateTime> toDateTime = timePeriodRequestDto.isPresent() ? timePeriodRequestDto.get().getToDateTime() : Optional.empty();
        return new TimePeriod(fromDateTime, toDateTime);
    }

}
