package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.user.UserDeleteDTO;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    private static final String WRONG_CREDENTIAL = "Wrong credential!";
    private static final String ALREADY_LOGGED = "You already logged!";
    private static final String LOGOUT = "Logout success!";
    private static final String NOT_LOGGED = "You are not logged!";
    private static final String NOT_ALLOWED = "Not allowed operation!";
    private static final String UNAUTHORIZED = "Not authorized!";

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

    @DeleteMapping("/{uid}")
    public long deleteUser(@PathVariable long uid, @RequestBody UserDeleteDTO userDeleteDTO,
                           HttpServletRequest request) {
        long userId = getLoggedUserId(request.getSession());
        if (userId <= 0) {
            throw new BadRequestException(NOT_LOGGED);
        }
        if (userId == uid || isAdmin(userId)) {
            return userService.deleteUser(userDeleteDTO, uid);
        }
        if (userId != uid) {
            throw new MethodNotAllowedException(NOT_ALLOWED);
        }
        throw new AuthenticationException(UNAUTHORIZED);
    }

    @PutMapping("/{uid}")
    public long changePassword(@PathVariable long uid,
                               @RequestBody UserEditPasswordDTO userEditPasswordDTO,
                               HttpServletRequest request) {
        long userId = getLoggedUserId(request.getSession());
        if (userId <= 0) {
            throw new BadRequestException(NOT_LOGGED);
        }
        if (userId != uid) {
            throw new MethodNotAllowedException(NOT_ALLOWED);
        }
        userId = userService.changePassword(userEditPasswordDTO, uid).getId();
        request.getSession().invalidate();
        return userId;
    }
}