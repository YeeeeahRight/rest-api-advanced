package com.epam.esm.service.validator;

/**
 * The interface Validator.
 *
 * @param <T> the type of object to validate
 */
public interface Validator<T> {
    /**
     * Validates object and returns true/false result.
     *
     * @param item the object to validate
     * @return true - if valid and false - if not valid
     */
    boolean isValid(T item);
}
