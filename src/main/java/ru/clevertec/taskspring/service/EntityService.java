package ru.clevertec.taskspring.service;

import java.util.List;

public interface EntityService<T> {

    List<T> findAll();

    T findById(int id);

    void save(T t);

    void update(T t);

    void delete(int id);

}