package com.epam.jwd.rent.service;

import com.epam.jwd.rent.model.Order;
import com.epam.jwd.rent.model.OrderDto;
import java.util.List;
import java.util.Optional;

/**
 * Common service class
 * @param <T> one of Dto classes
 *
 * @author Elmax19
 * @version 1.0
 */
public interface CommonService<T> {
    /**
     * method that transform result from Dto class method and return it
     * @return all Dto objects
     */
    Optional<List<T>> findAll();
}
