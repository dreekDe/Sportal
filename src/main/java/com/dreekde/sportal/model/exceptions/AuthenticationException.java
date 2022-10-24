package com.dreekde.sportal.model.exceptions;

/**
 * @author Desislava Tencheva
 */public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String message) {
        super(message);
    }
}