package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import com.dimitris.cryptoanalyze.service.CryptoValuesManager;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for storing and manipulating crypto values in memory. Internally it uses a {@link HashMap}
 * and maps each crypto to a {@link HashSet} of {@link CryptoValue}, so that it contains no duplicates
 */
@Service
public class CryptoValuesInMemManager implements CryptoValuesManager {

    private final Map<CryptoEnum, Set<CryptoValue>> values = new HashMap<>();

    /**
     * Store a set of crypto values for a specified crypto
     * @param cryptoEnum The crypto
     * @param newValues The crypto values
     */
    @Override
    public void addValues(CryptoEnum cryptoEnum, Set<CryptoValue> newValues) {
        if (!values.containsKey(cryptoEnum)) {
            values.put(cryptoEnum, new HashSet<>());
        }
        Set<CryptoValue> cryptoValues = values.get(cryptoEnum);
        cryptoValues.addAll(newValues);
    }

    /**
     * Get the stored values of the specified crypto
     * @param cryptoEnum The crypto
     * @return A set of crypto values or empty if there are no values for crypto
     */
    @Override
    public Optional<Set<CryptoValue>> getValues(CryptoEnum cryptoEnum) {
        return values.get(cryptoEnum) != null ? Optional.of(values.get(cryptoEnum)) : Optional.empty();
    }
}
