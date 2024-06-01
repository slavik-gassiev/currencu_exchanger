package com.slava.learn.currency_exchanger.DTO;

import java.math.BigDecimal;

public class ExchangeCurrencyDTO {
    int id;
    int base_currency_id;
    int target_currency_id;
    BigDecimal rate;
}
