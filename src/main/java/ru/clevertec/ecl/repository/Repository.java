package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<K extends Serializable, E extends BaseEntity<K>> {

    List<E> findAll();

    Optional<E> findById(K id);

    E save(E t);

    E update(E t);

    void delete(E e);

}