package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.CryptoValuesFileReader;
import com.dimitris.cryptoanalyze.service.CryptoValuesManager;
import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.exception.CryptoInternalException;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileDataLoaderTest {

    private static final String btcFilePathProperty = "data.file.path.btc";
    private static final String dogeFilePathProperty = "data.file.path.doge";
    private static final String ethFilePathProperty = "data.file.path.eth";
    private static final String ltcFilePathProperty = "data.file.path.ltc";
    private static final String xrpFilePathProperty = "data.file.path.xrp";
    private static final String btcFilePath = "btc.csv";
    private static final String dogeFilePath = "doge.csv";
    private static final String ethFilePath = "eth.csv";
    private static final String ltcFilePath = "ltc.csv";
    private static final String xrpFilePath = "xrp.csv";

    static {
        System.setProperty("data.file.path.property.format", "data.file.path");
        System.setProperty(btcFilePathProperty, btcFilePath);
        System.setProperty(dogeFilePathProperty, dogeFilePath);
        System.setProperty(ethFilePathProperty, ethFilePath);
        System.setProperty(ltcFilePathProperty, ltcFilePath);
        System.setProperty(xrpFilePathProperty, xrpFilePath);
    }

    @Mock
    private CryptoValuesFileReader cryptoValuesReader;

    @Mock
    private CryptoValuesManager cryptoValuesManager;

    @InjectMocks
    private FileDataLoader fileDataLoader;

    @Test
    public void test_loadInitialData() throws CryptoInternalException {
        fileDataLoader.loadInitialData();
    }

    @Test
    public void test_loadData_happyCase() throws CryptoInternalException {
        List<CryptoValue> cryptoValues = new ArrayList<>();
        Set<CryptoValue> cryptoValueSet = cryptoValues.stream().collect(Collectors.toSet());

        when(cryptoValuesReader.read(CryptoEnum.btc, btcFilePath)).thenReturn(cryptoValues);
        when(cryptoValuesReader.read(CryptoEnum.doge, dogeFilePath)).thenReturn(cryptoValues);
        when(cryptoValuesReader.read(CryptoEnum.eth, ethFilePath)).thenReturn(cryptoValues);
        when(cryptoValuesReader.read(CryptoEnum.ltc, ltcFilePath)).thenReturn(cryptoValues);
        when(cryptoValuesReader.read(CryptoEnum.xrp, xrpFilePath)).thenReturn(cryptoValues);

        fileDataLoader.loadData();

        verify(cryptoValuesReader).read(CryptoEnum.btc, btcFilePath);
        verify(cryptoValuesReader).read(CryptoEnum.doge, dogeFilePath);
        verify(cryptoValuesReader).read(CryptoEnum.eth, ethFilePath);
        verify(cryptoValuesReader).read(CryptoEnum.ltc, ltcFilePath);
        verify(cryptoValuesReader).read(CryptoEnum.xrp, xrpFilePath);

        verify(cryptoValuesManager).addValues(CryptoEnum.btc, cryptoValueSet);
        verify(cryptoValuesManager).addValues(CryptoEnum.doge, cryptoValueSet);
        verify(cryptoValuesManager).addValues(CryptoEnum.eth, cryptoValueSet);
        verify(cryptoValuesManager).addValues(CryptoEnum.ltc, cryptoValueSet);
        verify(cryptoValuesManager).addValues(CryptoEnum.xrp, cryptoValueSet);

        verifyNoMoreInteractions(cryptoValuesReader, cryptoValuesManager);
    }

    @Test
    public void test_loadData_filePathNull() throws CryptoInternalException {
        System.setProperty(btcFilePathProperty, "");
        Throwable throwable = assertThrows(CryptoInternalException.class, () -> fileDataLoader.loadData());
        assertEquals(throwable.getMessage(), "Error retrieving file path parameter for crypto " + CryptoEnum.btc.getCode());
    }

}
