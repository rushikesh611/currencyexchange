package com.example.currencyexchange.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.currencyexchange.services.ExchangeService;

@RestController
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Double>> fetchExchangeRates(@RequestParam(required = false) String date){
        if(date == null) {
            date = LocalDate.now().toString();
        }
        List<String> currencies = Arrays.asList("AED", "CAD", "EUR", "INR", "JPY");
        Map<String, Double> exchangeRates = null;
        try {
            exchangeRates = exchangeService.fetchExchangeRates(date, currencies);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(exchangeRates);
    }

    @GetMapping("/getData")
    public ResponseEntity<String> getData() throws IOException, InterruptedException {
        return ResponseEntity.ok(exchangeService.getData());
    }
}

