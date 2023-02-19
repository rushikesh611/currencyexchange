package com.example.currencyexchange.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Exchange {
    private String currencyCode;
    private Double rate;
}
