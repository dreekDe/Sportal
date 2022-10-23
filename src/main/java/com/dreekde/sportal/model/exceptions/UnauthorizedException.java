package com.dreekde.sportal.model.exceptions;

/**
 * @author Desislava Tencheva
 */
public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }
}
