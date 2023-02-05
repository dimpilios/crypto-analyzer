package com.dimitris.cryptoanalyze.service;

import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;

import java.util.List;

/**
 * Service interface abstracting operations of reading crypto values from filesystem
 */
public interface CryptoValuesFileReader {

    /**
     * Reads crypto values for the specified crypto from the specified file
     *
     * @param cryptoEnum The crypto
     * @param filePath   The file containing crypto values
     * @return A list of crypto values
     * @throws CryptoInternalException Internal error during reading crypto data from file
     */
    List<CryptoValue> read(CryptoEnum cryptoEnum, String filePath) throws CryptoInternalException;
}
