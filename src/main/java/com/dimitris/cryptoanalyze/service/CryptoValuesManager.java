package com.dimitris.cryptoanalyze.service;

import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;

import java.util.Optional;
import java.util.Set;

/**
 * Service interface abstracting operations of storing and manipulating crypto values
 */
public interface CryptoValuesManager {

    /**
     * Store a set of crypto values for a specified crypto
     * @param cryptoEnum The crypto
     * @param cryptoValues The crypto values
     */
    void addValues(CryptoEnum cryptoEnum, Set<CryptoValue> cryptoValues);

    /**
     * Get the stored values of the specified crypto
     * @param cryptoEnum The crypto
     * @return A set of crypto values or empty if there are no values for crypto
     */
    Optional<Set<CryptoValue>> getValues(CryptoEnum cryptoEnum);
}
