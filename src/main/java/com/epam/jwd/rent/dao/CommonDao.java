package com.epam.jwd.rent.dao;

import java.util.List;
import java.util.Optional;

/**
 * Common data access class
 * @param <T> one of models class
 * @author Elmax19
 * @version 1.0
 */
public interface CommonDao<T> {

    /**
     * method to get all objects fro database
     * @return list of model objects
     */
    Optional<List<T>> findAll();

    /**
     * method to get one of objects by it's id
     * @param id place in database
     * @return sought object
     */
    Optional<T> findById(int id);

    /**
     * method that returns count of table pages by count of rows in one table
     * @param n count of raws
     * @return count of pages
     */
    int getCountOfPages(int n);

    /**
     * method that returns objects of some page
     * @param column column name of sorting
     * @param n page number
     * @return list of sought objects
     */
    Optional<List<T>> findByPageNumber(String column, int n);
}
