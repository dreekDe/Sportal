package com.dreekde.sportal.model.dto.user;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class UserDeleteDTO {

    private long id;
    private String password;
    private String confirmPassword;
}
