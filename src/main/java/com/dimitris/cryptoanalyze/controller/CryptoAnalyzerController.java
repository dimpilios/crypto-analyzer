package com.dimitris.cryptoanalyze.controller;

import com.dimitris.cryptoanalyze.controller.dto.*;
import com.dimitris.cryptoanalyze.service.CryptoAnalyzer;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.exception.CryptoNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller providing REST endpoints through which user interacts with the application
 */
@RestController
@RequestMapping(path = "/api/crypto/analyze")
public class CryptoAnalyzerController {

    private static final Logger logger = LoggerFactory.getLogger(CryptoAnalyzerController.class);

    private final CryptoAnalyzer cryptoAnalyzer;

    @Autowired
    public CryptoAnalyzerController(CryptoAnalyzer cryptoAnalyzer) {
        this.cryptoAnalyzer = cryptoAnalyzer;
    }

    /**
     * Returns a list of cryptos sorted by normalized price range in descending order
     *
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @return A list of mappings between crypto and its normalized range value (no mappings exist for cryptos for
     * which no values are found)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    @Operation(summary = "Returns a list of cryptos sorted by normalized price range in descending order. Normalized range " +
            "is also returned with each crypto. Cryptos for which data do not exist are not present in the list. An " +
            "optional time period can be specified to restrict results. If not provided, search is not restricted and if " +
            "any of its fields are not provided, searching is restricted to only the other field. Time period fields must " +
            "conform to pattern \"yyyy-MM-dd HH:mm:ss\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
                    @Content(examples = {
                            @ExampleObject(value = "{\n" +
                                    "  \"fromDateTime\": \"2023-01-31 20:03:44\",\n" +
                                    "  \"toDateTime\": \"2023-02-29 23:19:01\"\n" +
                                    "}")})
            }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated list of cryptos",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CryptoMetricsListDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error during crypto data loading, manipulations or calculations",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @PostMapping("/sort/by/normalized/desc")
    public CryptoMetricsListDto sortCryptosByNormalizedRangeDesc(
            @RequestBody Optional<TimePeriodRequestDto> timePeriodRequestDto
    ) throws CryptoInternalException {
        logger.debug("INSIDE sortCryptosByNormalizedRangeDesc(). Params: {}, {}", timePeriodRequestDto);
        CryptoMetricsListDto response = cryptoAnalyzer.getCryptosSortedByNormalizedRangeDesc(timePeriodRequestDto);
        logger.debug("EXITING sortCryptosByNormalizedRangeDesc()");
        return response;
    }

    /**
     * Returns a mapping of metrics to their corresponding values for the specified crypto
     *
     * @param crypto               The crypto for which the metric values must be found
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @return A mapping of each metric to corresponding value (empty map if no values found for crypto)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     * @throws CryptoNotFoundException Requested crypto is not supported
     */
    @Operation(summary = "Returns a mapping of metrics to their corresponding values for the specified crypto. Mapping is " +
            "empty if data do not exist for specified crypto. Metrics returned are oldest,newest,minimum,maximum and " +
            "normalized range. An " + "optional time period can be specified to restrict results. If not provided, search is " +
            "not restricted and if " + "any of its fields are not provided, searching is restricted to only the other field. " +
            "Time period fields must " + "conform to pattern \"yyyy-MM-dd HH:mm:ss\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
                    @Content(examples = {
                            @ExampleObject(value = "{\n" +
                                    "  \"fromDateTime\": \"2023-01-31 20:03:44\",\n" +
                                    "  \"toDateTime\": \"2023-02-29 23:19:01\"\n" +
                                    "}")})
            }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated metrics for crypto",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CryptoMetricsDto.class))}),
            @ApiResponse(responseCode = "404", description = "Requested crypto is not supported",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error during crypto data loading, manipulations or calculations",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
    })
    @PostMapping("/metrics/{crypto}")
    public CryptoMetricsDto getMetricsForCrypto(
            @PathVariable("crypto") String crypto, @RequestBody Optional<TimePeriodRequestDto> timePeriodRequestDto
    ) throws CryptoInternalException, CryptoNotFoundException {
        logger.debug("INSIDE getMetricsForCrypto(). Params: {}, {}", timePeriodRequestDto, crypto);
        CryptoMetricsDto response = cryptoAnalyzer.getMetricsForCrypto(timePeriodRequestDto, crypto);
        logger.debug("EXITING getMetricsForCrypto()");
        return response;
    }

    /**
     * Returns the crypto with the highest normalized price range
     *
     * @param timePeriodRequestDto Optional time period calculation applies to. If omitted, calculation has no time
     *                             restriction. It contains two optional fields representing start and end time points.
     *                             If any of them is omitted, calculation will be restricted only to the
     *                             other point in time. If both are omitted, calculation has no time restriction.
     * @return A mapping of the crypto to its normalized range value (if no crypto data found, mapping contains null as values)
     * @throws CryptoInternalException Internal error during crypto data loading, manipulations or calculations
     */
    @Operation(summary = "Returns the crypto with the highest normalized range. Its normalized range value is also returned. " +
            "Response contains null values if crypto data do not exist. An optional time period can be specified to restrict " +
            "results. If not provided, search is not restricted and if any of its fields are not provided, searching is " +
            "restricted to only the other field. Time period fields must conform to pattern \"yyyy-MM-dd HH:mm:ss\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
                    @Content(examples = {
                            @ExampleObject(value = "{\n" +
                                    "  \"fromDateTime\": \"2023-01-31 20:03:44\",\n" +
                                    "  \"toDateTime\": \"2023-02-29 23:19:01\"\n" +
                                    "}")})
            }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found crypto",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CryptoMetricDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error during crypto data loading, manipulations or calculations",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
    })
    @PostMapping("/crypto/with/highest/normalized")
    public CryptoMetricDto getCryptoWithHighestNormalizedRange(
            @RequestBody Optional<TimePeriodRequestDto> timePeriodRequestDto
    ) throws CryptoInternalException {
        logger.debug("INSIDE getCryptoWithHighestNormalizedRange(). Params: {}", timePeriodRequestDto);
        CryptoMetricDto response = cryptoAnalyzer.getCryptoWithHighestNormalizedRange(timePeriodRequestDto);
        logger.debug("EXITING getCryptoWithHighestNormalizedRange()");
        return response;
    }
}
