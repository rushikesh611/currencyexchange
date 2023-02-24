package com.example.currencyexchange.controllers;

import com.example.currencyexchange.services.ExchangeService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {

    }

    @GetMapping("/getData")
    public ResponseEntity<String> getData(@RequestParam String currency) throws IOException, InterruptedException {
        return ResponseEntity.ok(exchangeService.getData(currency));
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Double>> fetchDataByDate(@RequestParam(required = false) String date) throws InterruptedException, ExecutionException, IOException {
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

        List<Future<Map.Entry<String, Double>>> futures = executorService.invokeAll(tasks, 20, TimeUnit.SECONDS);

        for(Future<Map.Entry<String, Double>> future : futures) {
            Map.Entry<String, Double> entry = future.get();
            if(entry != null) {
                exchangeRates.put(entry.getKey(), entry.getValue());
            }
        }

        Workbook workbook;
        Sheet sheet;
        File file = new File("results.xlsx");

        if(!file.exists()){
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Exchange Rates");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("BASE_CURRENCY");
            headerRow.createCell(1).setCellValue("TARGET_CURRENCY");
            headerRow.createCell(2).setCellValue("EXCHANGE_RATE");
            headerRow.createCell(3).setCellValue("CREATED_TS");
        } else {
            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
        }

        int lastRowIndex = sheet.getLastRowNum();
        for (int i = 0; i < currencies.size(); i++) {
            Row newRow = sheet.createRow(lastRowIndex + i + 1);
            Double rate = exchangeRates.get(currencies.get(i));
            if (rate != null) {
                newRow.createCell(0).setCellValue("USD");
                newRow.createCell(1).setCellValue(currencies.get(i));
                newRow.createCell(2).setCellValue(rate);
                newRow.createCell(3).setCellValue(LocalDateTime.now().toString());
            }
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();

        executorService.shutdown();
        return ResponseEntity.ok().header("timeout", "true").body(exchangeRates);
    }
}

