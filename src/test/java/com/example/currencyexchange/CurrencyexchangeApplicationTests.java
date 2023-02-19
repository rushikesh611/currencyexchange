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
    public void testFetchExchangeRates() throws Exception {
        Map<String, Double> exchangeRates = exchangeService.fetchExchangeRates("2020-01-01", Arrays.asList("AED", "CAD", "EUR", "INR", "JPY"));
        System.out.println(exchangeRates);
    }

    @Test
    public void testGetData() throws Exception {
        String data = exchangeService.getData("INR");
        System.out.println(data);
    }
}
