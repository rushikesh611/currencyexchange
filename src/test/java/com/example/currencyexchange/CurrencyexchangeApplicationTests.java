package com.example.currencyexchange;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.currencyexchange.services.ExchangeService;

@SpringBootTest
class CurrencyexchangeApplicationTests {
    @Autowired
    private ExchangeService exchangeService;

    @Test 
    public void testGetData() throws Exception {
        String data = exchangeService.getData("INR");
        assert data.equals("INR, 2020-01-01, 0.014");
    }

    @Test
    public void testFetchExchangeRates() throws Exception {
        Map<String, Double> exchangeRates = exchangeService.fetchExchangeRates("2020-01-01", Arrays.asList("AED", "CAD", "EUR", "INR", "JPY"));
        assert exchangeRates.get("AED") == 0.27;
        assert exchangeRates.get("CAD") == 0.73;
        assert exchangeRates.get("EUR") == 1.11;
        assert exchangeRates.get("INR") == 0.014;
        assert exchangeRates.get("JPY") == 0.009;
    }
}
