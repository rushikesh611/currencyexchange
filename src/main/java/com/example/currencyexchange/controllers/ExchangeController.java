package com.example.currencyexchange.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @GetMapping("/getData")
    public ResponseEntity<String> getData(@RequestParam String currency) throws IOException, InterruptedException {
        return ResponseEntity.ok(exchangeService.getData(currency));
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Double>> fetchDataByDate(@RequestParam(required = false) String date) throws InterruptedException, ExecutionException{
        if(date == null) {
            date = LocalDate.now().toString();
        }
        List<String> currencies = Arrays.asList("AED", "CAD", "EUR", "INR", "JPY");

        Map<String, Double> exchangeRates = new ConcurrentHashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(currencies.size());
        
        String finalDate = date;
        List<Callable<Map.Entry<String, Double>>> tasks = currencies.stream().map(currency -> {
            return (Callable<Map.Entry<String, Double>>)() -> {
        
                   try{
                    Double rate = exchangeService.fetchExchangeRates(finalDate, Collections.singletonList(currency)).get(currency);
                    return new AbstractMap.SimpleEntry<>(currency, rate);
                   } catch(IOException | InterruptedException e) {
                       e.printStackTrace();
                       return null;
                   }
                };
        }).collect(Collectors.toList());

        List<Future<Map.Entry<String, Double>>> futures = executorService.invokeAll(tasks, 10, TimeUnit.SECONDS);

        for(Future<Map.Entry<String, Double>> future : futures) {
            Map.Entry<String, Double> entry = future.get();
            if(entry != null) {
                exchangeRates.put(entry.getKey(), entry.getValue());
            }
        }
        executorService.shutdown();
        return ResponseEntity.ok().header("timeout", "true").body(exchangeRates);
        // return ResponseEntity.ok(exchangeRates);
    }
}

