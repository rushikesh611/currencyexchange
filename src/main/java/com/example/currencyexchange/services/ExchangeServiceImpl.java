package com.example.currencyexchange.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExchangeServiceImpl implements ExchangeService{

    private final String BASE_URL = "https://api.apilayer.com/exchangerates_data/";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;


    public ExchangeServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("Content-Type", "application/json");
        this.headers.set("apiKey", "VidfAtuWPAScMaYmDdjHGvHamwU9XT6W");
    }

    @Override
    public Map<String, Double> fetchExchangeRates(String date, List<String> currencies) {
        Map<String, Double> exchangeRates = new HashMap<>();

        for(String currency : currencies) {
            String url = BASE_URL + "/historical/?date=" + date + "&currencies=" + currency;
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class);
            Double rate = response.getBody().get("quotes").get("USD" + currency).asDouble();
            exchangeRates.put(currency, rate);   
        }
        return exchangeRates;
    }


    @Override
    public String getData() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=AUD&to=CAD&amount=1")
        .get()
        .addHeader("X-RapidAPI-Key", "b866136d8amsh17b6f6096063acbp1fe8d2jsn20a2cb9a599d")
        .addHeader("X-RapidAPI-Host", "currency-converter5.p.rapidapi.com")
        .build();
        Response response = client.newCall(request).execute();
    return response.body().string();
    }

    


}
