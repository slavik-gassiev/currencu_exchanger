package com.slava.learn.currency_exchanger.service;

import com.slava.learn.currency_exchanger.DAO.*;
import com.slava.learn.currency_exchanger.DTO.ExchangeCurrencyRequestDTO;
import com.slava.learn.currency_exchanger.entity.Currency;
import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;
import com.slava.learn.currency_exchanger.exeptions.InvalidParameterException;
import com.slava.learn.currency_exchanger.exeptions.NotFoundException;

public class ExchangeRateService {
    private final CurrencyDAO currencyDAO = new JdbcCurrencyDAO();
    private final ExchangeCurrencyDAO exchangeDAO = new JdbcExchangeCurrencyDAO();

    public ExchangeCurrency save(ExchangeCurrencyRequestDTO exchangeCurrencyRequestDTO) {
        String baseCurrencyCode = exchangeCurrencyRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeCurrencyRequestDTO.getTargetCurrencyCode();

        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode).
                orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode).
                orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        ExchangeCurrency exchangeCurrency =
                new ExchangeCurrency(baseCurrency, targetCurrency, exchangeCurrencyRequestDTO.getRate());

        return exchangeDAO.save(exchangeCurrency);
    }

    public ExchangeCurrency update(ExchangeCurrencyRequestDTO exchangeCurrencyRequestDTO) {
        String baseCurrencyCode = exchangeCurrencyRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeCurrencyRequestDTO.getTargetCurrencyCode();

        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode).
                orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode).
                orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        ExchangeCurrency exchangeCurrency =
                new ExchangeCurrency(baseCurrency, targetCurrency, exchangeCurrencyRequestDTO.getRate());

        return exchangeDAO.update(exchangeCurrency)
                .orElseThrow(() -> new NotFoundException(
                        "Failed to update exchange rate '" + baseCurrencyCode + "' - '" + targetCurrencyCode + "', no such exchange rate found"
                ));
    }
}


