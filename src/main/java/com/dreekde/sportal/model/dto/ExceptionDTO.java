package com.dreekde.sportal.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Desislava Tencheva
 */
@Data
public class ExceptionDTO {

    private int status;
    private LocalDateTime dateTime;
    private String message;
}
