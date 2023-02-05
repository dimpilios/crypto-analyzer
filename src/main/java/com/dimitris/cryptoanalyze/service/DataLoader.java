package com.dimitris.cryptoanalyze.service;

import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;

/**
 * Service interface abstracting loading of crypto values data into the application
 */
public interface DataLoader {

    /**
     * Loads initial data. Aimed at being used at some starting point in application execution
     *
     * @throws CryptoInternalException Internal error during loading crypto data from file
     */
    void loadInitialData() throws CryptoInternalException;

    /**
     * Loads data. Aimed at being used at any point in time during application execution
     *
     * @throws CryptoInternalException Internal error during loading crypto data from file
     */
    void loadData() throws CryptoInternalException;
}
