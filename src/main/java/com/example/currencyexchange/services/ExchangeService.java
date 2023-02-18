package com.example.currencyexchange.services;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExchangeService {
    Map<String, Double> fetchExchangeRates(String date, List<String> currencies);

    String getData() throws IOException;
}
