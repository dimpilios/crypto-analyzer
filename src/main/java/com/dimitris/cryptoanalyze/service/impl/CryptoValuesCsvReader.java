package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.CryptoValuesFileReader;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service for reading crypto values from a CSV file
 */
@Service
public class CryptoValuesCsvReader implements CryptoValuesFileReader {

    /**
     * Reads crypto values for the specified crypto from the specified csv file.
     * Empty lines are ignored but a specific header line should exist as the first line.
     * The header line should be &quot;timestamp,symbol,price&quot;
     *
     * @param cryptoEnum The crypto
     * @param filePath   The file containing crypto values
     * @return
     * @throws CryptoInternalException Internal error during reading crypto data from file
     */
    @Override
    public List<CryptoValue> read(CryptoEnum cryptoEnum, String filePath) throws CryptoInternalException {

        List<CryptoValue> cryptoValues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if (!br.readLine().equals("timestamp,symbol,price")) {
                throw new CryptoInternalException("Error reading csv file. Header line is missing");
            }

            while ((line = br.readLine()) != null) {
                if (line.isEmpty() || line.isBlank()) {
                    continue;
                }
                // split by a comma separator
                String[] split = line.split(",");

                if (split.length != 3 || Arrays.stream(split).anyMatch(s -> s.isEmpty() || s.isBlank())) {
                    throw new CryptoInternalException("Error reading csv file. Entries with missing columns found");
                }

                long timestamp = Long.valueOf(split[0]);
                BigDecimal price = new BigDecimal(split[2]);
                cryptoValues.add(new CryptoValue(timestamp, price));
            }
        } catch (IOException e) {
            throw new CryptoInternalException(e.getMessage());
        }
        return cryptoValues;
    }
}
