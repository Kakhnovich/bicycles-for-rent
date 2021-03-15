package com.epam.jwd.rent.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T> {
    Optional<List<T>> findAll();
}
