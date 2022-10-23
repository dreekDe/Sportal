package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;

/**
 * @author Desislava Tencheva
 */
public interface UserService {

    UserWithoutPasswordDTO register(UserRegisterDTO userRegisterDTO);
}
