package com.slava.learn.currency_exchanger.DAO;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T>{
    Optional<T> findById(Long id);
    List<T> findAll();
    T save(T entity);
    Optional<T> update(T entity);
    void delete(Long id);
}
