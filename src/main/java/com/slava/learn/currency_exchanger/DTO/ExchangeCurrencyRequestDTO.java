package com.slava.learn.currency_exchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCurrencyRequestDTO {
    private Long id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;

    private BigDecimal rate;

    public ExchangeCurrencyRequestDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

}
