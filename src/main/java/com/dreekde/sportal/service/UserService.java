package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.user.UserLoginDTO;
import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface UserService {

    boolean userIsAdmin(long id);

    List<UserWithoutPasswordDTO> getAllUsers();

    UserWithoutPasswordDTO getUserById(long id);

    UserWithoutPasswordDTO login(UserLoginDTO userLoginDTO);

    UserWithoutPasswordDTO register(UserRegisterDTO userRegisterDTO);
}
