package com.dreekde.sportal.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Desislava Tencheva
 */
@Data
public class UserEditDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String password;
}
