package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.CryptoValuesFileReader;
import com.dimitris.cryptoanalyze.service.CryptoValuesManager;
import com.dimitris.cryptoanalyze.service.DataLoader;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for loading of crypto values data from filesystem into the application
 */
@Service
public class FileDataLoader implements DataLoader {

    /**
     * System property specifying the format of file paths system properties used to load data for cryptos
     */
    private static String DATA_FILE_PATH_PROPERTY_FORMAT;

    private final CryptoValuesFileReader cryptoValuesReader;

    private final CryptoValuesManager cryptoValuesManager;

    /**
     * @param cryptoValuesReader  A reader for reading crypto values from filesystem
     * @param cryptoValuesManager A manager for storing and manipulating crypto values data
     */
    @Autowired
    public FileDataLoader(CryptoValuesFileReader cryptoValuesReader, CryptoValuesManager cryptoValuesManager) {
        this.cryptoValuesReader = cryptoValuesReader;
        this.cryptoValuesManager = cryptoValuesManager;
        DATA_FILE_PATH_PROPERTY_FORMAT = System.getProperty("data.file.path.property.format");
    }

    /**
     * Loads initial data. Aimed at being used at some starting point in application execution
     *
     * @throws CryptoInternalException Internal error during loading crypto data from file
     */
    @PostConstruct
    @Override
    public void loadInitialData() throws CryptoInternalException {
        loadData();
    }

    /**
     * Loads data. Aimed at being used at any point in time during application execution.
     * It retrieves file paths as system properties of the form &quot;[DATA_FILE_PATH_PROPERTY_FORMAT].crypto&quot;,
     * where 'crypto' stands for the crypto code
     *
     * @throws CryptoInternalException Internal error during loading crypto data from file
     */
    @Override
    public void loadData() throws CryptoInternalException {
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            String filePath = System.getProperty(DATA_FILE_PATH_PROPERTY_FORMAT + "." + cryptoEnum.getCode());
            if (filePath == null || filePath.isBlank() || filePath.isEmpty()) {
                throw new CryptoInternalException("Error retrieving file path parameter for crypto " + cryptoEnum.getCode());
            }
            List<CryptoValue> cryptoValues = cryptoValuesReader.read(cryptoEnum, filePath);
            cryptoValuesManager.addValues(cryptoEnum, cryptoValues.stream().collect(Collectors.toSet()));
        }
    }

    /**
     * Loads data for a specified crypto from a specified file. Aimed at being used at any point in time during
     * application execution.
     *
     * @param cryptoEnum
     * @param filePath
     * @throws CryptoInternalException Internal error during loading crypto data from file
     */
    public void loadData(CryptoEnum cryptoEnum, String filePath) throws CryptoInternalException {
        if (filePath == null || filePath.isBlank() || filePath.isEmpty()) {
            throw new CryptoInternalException("Error, file path for retrieving data for crypto " +
                    cryptoEnum.getCode() + " is null");
        }
        List<CryptoValue> cryptoValues = cryptoValuesReader.read(cryptoEnum, filePath);
        cryptoValuesManager.addValues(cryptoEnum, cryptoValues.stream().collect(Collectors.toSet()));
    }
}
