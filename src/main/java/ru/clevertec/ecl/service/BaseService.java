package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

import java.util.List;

public interface BaseService<T, R> {

    List<R> findAll(EntityFilter filter, Pageable pageable);

    R findById(Long id);

    R save(T t);

    R update(T t);

    void delete(Long id);

}