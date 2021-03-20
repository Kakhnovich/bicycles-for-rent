package com.epam.jwd.rent.dao;

import com.epam.jwd.rent.model.Order;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T> {

    Optional<List<T>> findAll();

    Optional<T> create(T entity);

    Optional<T> findById(int id);

    int getCountOfPages(int n);

    Optional<List<T>> findByPageNumber(String column, int n);
}
