package com.slava.learn.currency_exchanger.DAO;

import com.slava.learn.currency_exchanger.DTO.CurrencyDTO;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<CurrencyDTO>{
    Optional<CurrencyDTO> findByCode(String code);
}
