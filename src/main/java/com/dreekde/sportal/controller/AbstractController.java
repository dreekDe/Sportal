package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.ExceptionDTO;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

/**
 * @author Desislava Tencheva
 */
public abstract class AbstractController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ExceptionDTO notFoundException(Exception message) {
        return creatExceptionDTO(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ExceptionDTO unauthorizedException(Exception message) {
        return creatExceptionDTO(message,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ExceptionDTO badRequestException(Exception message) {
        return creatExceptionDTO(message,HttpStatus.BAD_REQUEST);
    }

    private ExceptionDTO creatExceptionDTO(Exception message, HttpStatus httpStatus) {
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setStatus(httpStatus.value());
        exceptionDTO.setDateTime(LocalDateTime.now());
        exceptionDTO.setMessage(message.getMessage());
        return exceptionDTO;
    }
}
