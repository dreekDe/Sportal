package com.dreekde.sportal.model.dto.user;

import lombok.Data;

/**
 * @author Desislava Tencheva
 */
@Data
public class UserEditPasswordDTO {

    private long id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
