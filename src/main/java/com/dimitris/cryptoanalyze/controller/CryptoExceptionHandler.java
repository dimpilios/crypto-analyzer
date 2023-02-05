package com.dimitris.cryptoanalyze.controller;

import com.dimitris.cryptoanalyze.controller.dto.ErrorResponseDto;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.exception.CryptoNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class handles at Controller level any checked exception thrown from lower level services
 */
@Hidden
@RestControllerAdvice()
public class CryptoExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CryptoExceptionHandler.class);

    /**
     * Handles {@link CryptoInternalException} thrown from lower level services. Logs exception message and
     * stack trace, and wraps exception into special exception dto {@link ErrorResponseDto} which returns to user
     *
     * @param cryptoInternalException Exception thrown by low level services during crypto data loading,
     *                                manipulations or calculations
     * @return A special response for indicating error
     */
    @ExceptionHandler(value = {CryptoInternalException.class})
    protected ResponseEntity handleInternalException(CryptoInternalException cryptoInternalException) {
        logger.error("Exception occurred", cryptoInternalException);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(cryptoInternalException.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles {@link CryptoNotFoundException} thrown from lower level services. Logs exception message and
     * stack trace, and wraps exception into special exception dto {@link ErrorResponseDto} which returns to user
     *
     * @param cryptoNotFoundException Exception thrown by low level services if requested crypto is not supported
     * @return A special response for indicating error
     */
    @ExceptionHandler(value = {CryptoNotFoundException.class})
    protected ResponseEntity handleCryptoNotFoundException(CryptoNotFoundException cryptoNotFoundException) {
        logger.error("Exception occurred", cryptoNotFoundException);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(cryptoNotFoundException.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}
