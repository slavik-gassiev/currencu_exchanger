package com.slava.learn.currency_exchanger.Utils;

import com.slava.learn.currency_exchanger.DTO.CurrencyRequestDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeCurrencyRequestDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeRequestDTO;
import com.slava.learn.currency_exchanger.exeptions.InvalidParameterException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {
    private static Set<String> currencyCodes;

    public static void validate(CurrencyRequestDTO currencyRequestDTO) {
        String name = currencyRequestDTO.getName();
        String code = currencyRequestDTO.getCode();
        String sign = currencyRequestDTO.getSign();

        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Missing parameter - name");
        }

        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Missing parameter - code" + code);
        }

        if (sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Missing parameter - sign");
        }
        validateCurrencyCode(code);
    }

    public static void validate(ExchangeCurrencyRequestDTO exchangeCurrencyRequestDTO) {
        String baseCurrencyCode = exchangeCurrencyRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeCurrencyRequestDTO.getTargetCurrencyCode();
        BigDecimal rate = exchangeCurrencyRequestDTO.getRate();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - baseCurrencyCode");
        }
        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - targetCurrencyCode");
        }

        if (rate == null) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("Invalid parameter - rate must be non-negative");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    private static void validate(ExchangeRequestDTO exchangeRequestDTO) {
        String baseCurrencyCode = exchangeRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDTO.getTargetCurrencyCode();
        BigDecimal amount = exchangeRequestDTO.getAmount();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - from");
        }
        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - to");
        }
        if (amount == null) {
            throw new InvalidParameterException("Missing parameter - amount");
        }
        if (amount.compareTo(new BigDecimal("0.0")) < 0) {
            throw new InvalidParameterException("Invalid parameter - amount must be non-negative");
        }
    }

    public static void validateCurrencyCode(String code) {
        if(code.length() != 3) {
            throw new InvalidParameterException("Currency code must contain exactly 3 letters");
        }

        if (currencyCodes == null) {
            Set<Currency> currencies = Currency.getAvailableCurrencies();
            currencyCodes = currencies.stream()
                    .map(Currency::getCurrencyCode)
                    .collect(Collectors.toSet());
        }

        if(!currencyCodes.contains(code)) {
            throw new InvalidParameterException(
                    "Currency code must be in ISO 4217 format"
            );
        }
    }
}
