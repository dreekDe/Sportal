package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.user.UserLoginDTO;
import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Desislava Tencheva
 */
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController{

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{uid}")
    public UserWithoutPasswordDTO getUsersById(@PathVariable long uid){
        return userService.getUserById(uid);
    }
    @GetMapping()
    public List<UserWithoutPasswordDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping()
    public UserWithoutPasswordDTO register (@RequestBody UserRegisterDTO userRegisterDTO){
        return userService.register(userRegisterDTO);
    }

    @PostMapping("/auth")
    public UserWithoutPasswordDTO login(@RequestBody UserLoginDTO userLoginDTO) {
        //todo
        return userService.login(userLoginDTO);
    }
}
