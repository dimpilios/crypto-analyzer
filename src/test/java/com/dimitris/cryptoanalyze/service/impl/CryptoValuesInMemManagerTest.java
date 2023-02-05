package com.dimitris.cryptoanalyze.service.impl;

import com.dimitris.cryptoanalyze.service.enums.CryptoEnum;
import com.dimitris.cryptoanalyze.service.model.CryptoValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CryptoValuesInMemManagerTest {

    @Test
    public void test_initialization() {
        CryptoValuesInMemManager manager = new CryptoValuesInMemManager();
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            assertTrue(manager.getValues(cryptoEnum).isEmpty());
        }
    }

    @Test
    public void test_addValues_noCurrentValuesExistForCrypto() {
        CryptoValuesInMemManager manager = new CryptoValuesInMemManager();
        Set<CryptoValue> cryptoValues = Set.of(new CryptoValue(1234L, new BigDecimal("1300")));
        manager.addValues(CryptoEnum.btc, cryptoValues);
        assertEquals(manager.getValues(CryptoEnum.btc).get(), cryptoValues);
    }

    @Test
    public void test_addValues_currentValuesExistForCrypto_noDuplicateCandidates() {
        CryptoValuesInMemManager manager = new CryptoValuesInMemManager();
        Set<CryptoValue> cryptoValues1 = Set.of(new CryptoValue(1234L, new BigDecimal("1300")));
        Set<CryptoValue> cryptoValues2 = Set.of(new CryptoValue(5678L, new BigDecimal("1900")));
        Set<CryptoValue> cryptoValuesAggregate = new HashSet<>(cryptoValues1);
        cryptoValuesAggregate.addAll(cryptoValues2);

        manager.addValues(CryptoEnum.btc, cryptoValues1);
        manager.addValues(CryptoEnum.btc, cryptoValues2);
        assertEquals(manager.getValues(CryptoEnum.btc).get(), cryptoValuesAggregate);
    }

    @Test
    public void test_addValues_currentValuesExistForCrypto_duplicateCandidates() {
        CryptoValuesInMemManager manager = new CryptoValuesInMemManager();
        Set<CryptoValue> cryptoValues1 = Set.of(new CryptoValue(1234L, new BigDecimal("1300")));
        Set<CryptoValue> cryptoValues2 = new HashSet<>(cryptoValues1);

        manager.addValues(CryptoEnum.btc, cryptoValues1);
        manager.addValues(CryptoEnum.btc, cryptoValues2);
        assertEquals(manager.getValues(CryptoEnum.btc).get(), cryptoValues1);
    }

    @Test
    public void test_getValues() {
        CryptoValuesInMemManager manager = new CryptoValuesInMemManager();
        for (CryptoEnum cryptoEnum : CryptoEnum.values()) {
            assertTrue(manager.getValues(cryptoEnum).isEmpty());
        }
    }
}
