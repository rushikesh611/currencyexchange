package com.example.currencyexchange.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExchangeServiceImpl implements ExchangeService{

    @Override
    public Map<String, Double> fetchExchangeRates(String date, List<String> currencies) throws IOException, InterruptedException {
        Map<String, Double> exchangeRates = new HashMap<>();

        for(String currency : currencies) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.apilayer.com/fixer/convert?to=USD&from="+currency+"&amount=1"))
                    .header("apiKey", "")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            // fetch exchange rate for each currency from the response
            JsonNode jsonNode = new ObjectMapper().readTree(response.body());
            Double rate = jsonNode.get("result").asDouble();
            exchangeRates.put(currency, rate);
        }
        return exchangeRates;
    }


    @Override
    public String getData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://api.apilayer.com/fixer/convert?to=USD&from=EUR&amount=1"))
		.header("apiKey", "")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    


}
