package com.slava.learn.currency_exchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCurrencyDTO {
    Long id;
    Long base_currency_id;
    Long target_currency_id;
    BigDecimal rate;

    public ExchangeCurrencyDTO(Long base_currency_id, Long target_currency_id, BigDecimal rate) {
        this.base_currency_id = base_currency_id;
        this.target_currency_id = target_currency_id;
        this.rate = rate;
    }

}
