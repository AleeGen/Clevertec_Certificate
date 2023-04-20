package ru.clevertec.ecl.service;

import java.util.List;

public interface BaseService<T,R> {

    List<R> findAll();

    R findById(Long id);

    R save(T t);

    R update(T t);

    void delete(Long id);

}