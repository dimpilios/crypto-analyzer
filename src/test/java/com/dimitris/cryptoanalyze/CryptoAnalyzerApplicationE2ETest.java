package com.dimitris.cryptoanalyze;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CryptoAnalyzerApplicationE2ETest {

    private static final String btcFilePathProperty = "data.file.path.btc";
    private static final String dogeFilePathProperty = "data.file.path.doge";
    private static final String ethFilePathProperty = "data.file.path.eth";
    private static final String ltcFilePathProperty = "data.file.path.ltc";
    private static final String xrpFilePathProperty = "data.file.path.xrp";
    private static final String btcFilePath = "prices\\BTC_values_test.csv";

    private static final String dogeFilePath = "prices\\DOGE_values_test.csv";
    private static final String ethFilePath = "prices\\ETH_values_test.csv";
    private static final String ltcFilePath = "prices\\LTC_values_test.csv";
    private static final String xrpFilePath = "prices\\XRP_values_test.csv";

    static {
        System.setProperty("data.file.path.property.format", "data.file.path");
        System.setProperty(btcFilePathProperty, btcFilePath);
        System.setProperty(dogeFilePathProperty, dogeFilePath);
        System.setProperty(ethFilePathProperty, ethFilePath);
        System.setProperty(ltcFilePathProperty, ltcFilePath);
        System.setProperty(xrpFilePathProperty, xrpFilePath);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_sortCryptosByNormalizedRangeDesc_timePeriodProvided() throws Exception {
        String requestAsJsonString = "{\n" +
                "  \"fromDateTime\": \"2022-01-01 00:00:00\",\n" +
                "  \"toDateTime\": \"2022-01-02 00:00:00\"\n" +
                "}";

        mockMvc
                .perform(
                        post("/api/crypto/analyze/sort/by/normalized/desc")
                                .content(requestAsJsonString)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.cryptos[0].crypto").exists()
                ).andExpect(
                        jsonPath("$.cryptos[0].value").exists()
                ).andExpect(
                        jsonPath("$.cryptos[1].crypto").exists()
                ).andExpect(
                        jsonPath("$.cryptos[1].value").exists()
                ).andExpect(
                        jsonPath("$.cryptos[2].crypto").exists()
                ).andExpect(
                        jsonPath("$.cryptos[2].value").exists()
                ).andExpect(
                        jsonPath("$.cryptos[3].crypto").exists()
                ).andExpect(
                        jsonPath("$.cryptos[3].value").exists()
                ).andExpect(
                        jsonPath("$.cryptos[4].crypto").exists()
                ).andExpect(
                        jsonPath("$.cryptos[4].value").exists()
                );
    }

    @Test
    void test_getMetricsForCrypto() throws Exception {
        mockMvc
                .perform(
                        post("/api/crypto/analyze/metrics/btc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void test_getMetricsForCrypto_cryptoNotSupported() throws Exception {
        mockMvc
                .perform(
                        post("/api/crypto/analyze/metrics/ttt")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        jsonPath("$.errorMessage").value("Crypto " + "ttt" + " not supported.")
                );
    }

    @Test
    void test_getCryptoWithHighestNormalizedRange() throws Exception {
        mockMvc
                .perform(
                        post("/api/crypto/analyze/crypto/with/highest/normalized")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        jsonPath("$.crypto").exists()
                ).andExpect(
                        jsonPath("$.value").exists()
                );
    }
}
