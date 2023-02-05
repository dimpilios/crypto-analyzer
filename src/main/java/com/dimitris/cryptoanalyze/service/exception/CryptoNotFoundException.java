package com.dimitris.cryptoanalyze.service.exception;

/**
 * Exception class used to wrap exception occurring when specified crypto is not supported
 */
public class CryptoNotFoundException extends Exception {

    public CryptoNotFoundException(String msg) {
        super(msg);
    }
}
