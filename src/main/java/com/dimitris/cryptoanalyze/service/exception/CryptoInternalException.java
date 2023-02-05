package com.dimitris.cryptoanalyze.service.exception;

/**
 * Exception class used to wrap exceptions regarding internal calculations
 */
public class CryptoInternalException extends Exception {

    public CryptoInternalException(String msg) {
        super(msg);
    }
}
