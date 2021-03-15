package com.epam.jwd.rent.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T> {

    Optional<List<T>> findAll();

    Optional<T> create(T entity);

    Optional<T> findById(int id);

}
