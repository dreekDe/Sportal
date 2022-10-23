package com.dreekde.sportal.model.exceptions;

/**
 * @author Desislava Tencheva
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException (String message){
        super(message);
    }
}
