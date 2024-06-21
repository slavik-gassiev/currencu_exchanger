package com.slava.learn.currency_exchanger.DAO;

import com.slava.learn.currency_exchanger.entity.ExchangeCurrency;

import java.util.Optional;

public interface ExchangeCurrencyDAO extends CrudDAO<ExchangeCurrency>{
    Optional<ExchangeCurrency> findByCodes(String baseCurrencyId, String targetCurrencyId);
}

