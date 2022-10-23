package com.dreekde.sportal.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author Desislava Tencheva
 */
@Data
public class UserRegisterDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
