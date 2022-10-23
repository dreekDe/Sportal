package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.user.UserLoginDTO;
import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.model.entities.User;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.exceptions.UnauthorizedException;
import com.dreekde.sportal.model.repositories.UserRepository;
import com.dreekde.sportal.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Desislava Tencheva
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found!";
    private static final String WRONG_CREDENTIAL = "Wrong credentials!";
    private static final String INVALID_DATA = "Name can not be shorter than 2 or longer than 15 symbols!";
    private static final String INVALID_PASSWORD = "Password can not be shorter then 8 symbols!";
    private static final String PASSWORDS_MISMATCH = "Passwords mismatch!";
    private static final String INVALID_AGE = "Invalid registration age";
    private static final String USERNAME_OR_EMAIL_ALREADY_EXIST = "Username or email already exist!";
    private static final String INVALID_EMAIL = "Invalid email!";

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public List<UserWithoutPasswordDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().filter(User::isActive)
                .map(user -> modelMapper.map(user, UserWithoutPasswordDTO.class))
                .collect(Collectors.toList());
    }

    public UserWithoutPasswordDTO getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }
    public UserWithoutPasswordDTO login(UserLoginDTO userLoginDTO) {
        if (!isValidName(userLoginDTO.getUsername())
                || !isValidPassword(userLoginDTO.getPassword())) {
            throw new BadRequestException(WRONG_CREDENTIAL);
        }
        User user = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> new UnauthorizedException(WRONG_CREDENTIAL));
        if (!bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(WRONG_CREDENTIAL);
        }
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    public UserWithoutPasswordDTO register(UserRegisterDTO userRegisterDTO) {
        validationUserRegisterDTO(userRegisterDTO);
        User user = modelMapper.map(userRegisterDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        if (user.getId() == 1) {
            user.setAdmin(true);
            userRepository.save(user);
        }
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    private void validationUserRegisterDTO(UserRegisterDTO userRegisterDTO) {
        if (!isValidName(userRegisterDTO.getUsername())
                || !isValidName(userRegisterDTO.getFirstName())
                || !isValidName(userRegisterDTO.getLastName())) {
            throw new BadRequestException(INVALID_DATA);
        }
        if (!isValidPassword(userRegisterDTO.getPassword())) {
            throw new BadRequestException(INVALID_PASSWORD);
        }
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            throw new BadRequestException(PASSWORDS_MISMATCH);
        }
        if (!isValidEmail(userRegisterDTO.getEmail())) {
            throw new BadRequestException(INVALID_EMAIL);
        }
        if (!isValidAge(userRegisterDTO.getDateOfBirth())) {
            throw new BadRequestException(INVALID_AGE);
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername()) ||
                userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new BadRequestException(USERNAME_OR_EMAIL_ALREADY_EXIST);
        }
    }

    private boolean isValidAge(LocalDate dateOfBirth) {
        return LocalDate.now().getYear() - dateOfBirth.getYear() >= 18;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w]{2,}@[\\w]{1,}.[\\w]{2,3}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    private boolean isValidName(String input) {
        return input != null && input.length() >= 2 && input.length() < 15;
    }
}