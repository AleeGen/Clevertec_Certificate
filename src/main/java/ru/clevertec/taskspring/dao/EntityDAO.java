package ru.clevertec.taskspring.dao;

import java.util.List;
import java.util.Optional;

public interface EntityDAO<T> {

    List<T> findAll();

    Optional<T> findById(int id);

    boolean save(T t);

    boolean update(T t);

    boolean delete(int id);

}