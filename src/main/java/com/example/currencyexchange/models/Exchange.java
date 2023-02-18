package com.example.currencyexchange.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Data
public class Exchange {
    private String currencyCode;
    private Double rate;
}
