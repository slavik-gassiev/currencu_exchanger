package com.slava.learn.currency_exchanger.DAO;

import com.slava.learn.currency_exchanger.entity.Currency;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency>{
    Optional<Currency> findByCode(String code);
}
