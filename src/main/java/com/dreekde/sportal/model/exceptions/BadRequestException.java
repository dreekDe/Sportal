package com.dreekde.sportal.model.exceptions;

/**
 * @author Desislava Tencheva
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}