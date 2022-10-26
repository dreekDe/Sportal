package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.user.UserDeleteDTO;
import com.dreekde.sportal.model.dto.user.UserEditDTO;
import com.dreekde.sportal.model.dto.user.UserEditPasswordDTO;
import com.dreekde.sportal.model.dto.user.UserLoginDTO;
import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.model.exceptions.AuthenticationException;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.MethodNotAllowedException;
import com.dreekde.sportal.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{uid}")
    public UserWithoutPasswordDTO getUsersById(@PathVariable long uid) {
        return userService.getUserById(uid);
    }

    @GetMapping()
    public List<UserWithoutPasswordDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public UserWithoutPasswordDTO register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.register(userRegisterDTO);
    }

    @PostMapping("/auth")
    public UserWithoutPasswordDTO login(@RequestBody UserLoginDTO userLoginDTO,
                                        HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.isNew() || !isLogged(request)) {
            UserWithoutPasswordDTO user = userService.login(userLoginDTO);
            loginUser(request, user.getId());
            return user;
        }
        if (getLoggedUserId(session) > 0) {
            throw new BadRequestException(ALREADY_LOGGED);
        }
        throw new BadRequestException(WRONG_CREDENTIAL);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return LOGOUT;
    }

    @DeleteMapping()
    public long deleteUser(@RequestBody UserDeleteDTO userDeleteDTO,
                           HttpServletRequest request) {
        long userId = getLoggedUserId(request.getSession());
        if (userId == userDeleteDTO.getId() || isAdmin(userId)) {
            return userService.deleteUser(userDeleteDTO);
        }
        if (userId != userDeleteDTO.getId()) {
            throw new MethodNotAllowedException(NOT_ALLOWED);
        }
        throw new AuthenticationException(UNAUTHORIZED);
    }

    @PutMapping("/pass")
    public long changePassword(@RequestBody UserEditPasswordDTO userEditPasswordDTO,
                               HttpServletRequest request) {
        validationEditUser(request, userEditPasswordDTO.getId());
        long userId = userService.changePassword(userEditPasswordDTO).getId();
        request.getSession().invalidate();
        return userId;
    }

    @PutMapping("/edit")
    public long editUser(@RequestBody UserEditDTO userEditDTO, HttpServletRequest request) {
        validationEditUser(request, userEditDTO.getId());
        long userId = userService.editUser(userEditDTO);
        request.getSession().invalidate();
        return userId;
    }
}