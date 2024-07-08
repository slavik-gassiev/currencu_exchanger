package com.slava.learn.currency_exchanger.service;

import com.slava.learn.currency_exchanger.DAO.ExchangeCurrencyDAO;
import com.slava.learn.currency_exchanger.DAO.JdbcExchangeCurrencyDAO;
import com.slava.learn.currency_exchanger.DTO.ExchangeRequestDTO;
import com.slava.learn.currency_exchanger.DTO.ExchangeResponseDTO;
import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;
import com.slava.learn.currency_exchanger.exeptions.NotFoundException;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

import static com.slava.learn.currency_exchanger.Utils.MappingUtils.convertToDTO;

public class ExchangeService {
    private final ExchangeCurrencyDAO exchangeDAO = new JdbcExchangeCurrencyDAO();

    public ExchangeResponseDTO exchange(ExchangeRequestDTO exchangeRequestDTO) {
        ExchangeCurrency exchangeCurrency = findExchangeRate(exchangeRequestDTO)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Exchange rate '%s' - '%s' not found in the database",
                                exchangeRequestDTO.getBaseCurrencyCode(), exchangeRequestDTO.getTargetCurrencyCode()))
                );

        BigDecimal amount = exchangeRequestDTO.getAmount();
        BigDecimal convertedAmount = amount
                .multiply(exchangeCurrency.getRate().setScale(2, RoundingMode.HALF_EVEN));

        return new ExchangeResponseDTO(
                convertToDTO(exchangeCurrency.getBaseCurrency()),
                convertToDTO(exchangeCurrency.getTargetCurrency()),
                exchangeCurrency.getRate(),
                amount,
                convertedAmount
        );
    }

    private Optional<ExchangeCurrency> findExchangeRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeCurrency> exchangeCurrency = findByDirectRate(exchangeRequestDTO);

        if (exchangeCurrency.isEmpty()) {
            exchangeCurrency = findByIndirectRate(exchangeRequestDTO);
        }
        if (exchangeCurrency.isEmpty()) {
            exchangeCurrency = findByCrossRate(exchangeRequestDTO);
        }

        return exchangeCurrency;
    }

    private Optional<ExchangeCurrency> findByDirectRate(ExchangeRequestDTO exchangeRequestDTO) {
        return exchangeDAO.findByCodes(exchangeRequestDTO.getBaseCurrencyCode(), exchangeRequestDTO.getTargetCurrencyCode());
    }

    private Optional<ExchangeCurrency> findByIndirectRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeCurrency> exchangeCurrency =
                exchangeDAO.findByCodes(exchangeRequestDTO.getTargetCurrencyCode(), exchangeRequestDTO.getBaseCurrencyCode());
        if (exchangeCurrency.isEmpty()){
            return Optional.empty();
        }

        ExchangeCurrency indirectExchangeRate = exchangeCurrency.get();
        BigDecimal rate = BigDecimal.ONE.divide(indirectExchangeRate.getRate(), MathContext.DECIMAL64)
                .setScale(6, RoundingMode.HALF_EVEN);

        ExchangeCurrency directExchangeRate = new ExchangeCurrency(
                indirectExchangeRate.getTargetCurrency(),
                indirectExchangeRate.getBaseCurrency(),
                rate
        );
        return Optional.of(directExchangeRate);
    }

    private Optional<ExchangeCurrency> findByCrossRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeCurrency> usdToBaseOptional =
                exchangeDAO.findByCodes("USD", exchangeRequestDTO.getBaseCurrencyCode());
        Optional<ExchangeCurrency> usdToTargetOptional =
                exchangeDAO.findByCodes("USD", exchangeRequestDTO.getTargetCurrencyCode());

        if (usdToBaseOptional.isEmpty()|| usdToTargetOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeCurrency usdToBase = usdToBaseOptional.get();
        ExchangeCurrency usdToTarget = usdToTargetOptional.get();

        BigDecimal rate =
                usdToTarget.getRate().divide(usdToBase.getRate(), MathContext.DECIMAL64)
                        .setScale(6, RoundingMode.HALF_EVEN);

        ExchangeCurrency directExchangeRate = new ExchangeCurrency(
                usdToBase.getTargetCurrency(),
                usdToTarget.getTargetCurrency(),
                rate
        );
        return Optional.of(directExchangeRate);
    }
}
