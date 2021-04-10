package com.epam.jwd.rent.exception;

/**
 * Custom exception for situations when there isn't such entity
 * @see RuntimeException
 * @author Elmax19
 * @version 1.0
 */
public class NoSuchEntityException extends RuntimeException{
    /**
     * class constructor
     * @param message received message
     */
    public NoSuchEntityException(String message) {
        super(message);
    }
}
