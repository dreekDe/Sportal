package com.dreekde.sportal.service;

import com.dreekde.sportal.model.dto.user.UserDeleteDTO;
import com.dreekde.sportal.model.dto.user.UserEditDTO;
import com.dreekde.sportal.model.dto.user.UserEditPasswordDTO;
import com.dreekde.sportal.model.dto.user.UserLoginDTO;
import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.model.entities.User;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
public interface UserService {

    User getUser(long id);

    UserWithoutPasswordDTO changePassword(UserEditPasswordDTO userEditPasswordDTO, long id);

    long userDelete(UserDeleteDTO userDeleteDTO);

    long adminDelete(UserDeleteDTO userDeleteDTO, long id);

    boolean userIsAdmin(long id);

    List<UserWithoutPasswordDTO> getAllUsers();

    UserWithoutPasswordDTO getUserById(long id);

    UserWithoutPasswordDTO login(UserLoginDTO userLoginDTO);

    UserWithoutPasswordDTO register(UserRegisterDTO userRegisterDTO);

    long editUser(UserEditDTO userEditDTO);
}
