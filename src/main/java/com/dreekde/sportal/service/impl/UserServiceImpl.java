package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.user.UserDeleteDTO;
import com.dreekde.sportal.model.dto.user.UserEditDTO;
import com.dreekde.sportal.model.dto.user.UserEditPasswordDTO;
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
import java.time.LocalDateTime;
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

    @Override
    public long editUser(UserEditDTO userEditDTO) {
        String password = userEditDTO.getPassword().trim();
        String username = userEditDTO.getUsername().trim();
        String email = userEditDTO.getEmail().trim();
        long userId = userEditDTO.getId();
        validationUserCredential(password, userId);
        validationNames(username, userEditDTO.getFirstName(), userEditDTO.getLastName());
        if (userRepository.existsByUsernameAndIdNot(username, userId)) {
            throw new BadRequestException(USERNAME_OR_EMAIL_ALREADY_EXIST);
        }
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new BadRequestException(USERNAME_OR_EMAIL_ALREADY_EXIST);
        }
        validationEmail(email);
        validationAge(userEditDTO.getDateOfBirth());
        User user = modelMapper.map(userEditDTO, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserWithoutPasswordDTO changePassword(UserEditPasswordDTO userEditPasswordDTO) {
        if (!isValidPassword(userEditPasswordDTO.getNewPassword().trim())) {
            throw new BadRequestException(INVALID_PASSWORD);
        }
        String password = userEditPasswordDTO.getOldPassword().trim();
        long userId = userEditPasswordDTO.getId();
        User user = validationUserCredential(password, userId);
        matchingPasswords(userEditPasswordDTO.getNewPassword().trim(),
                userEditPasswordDTO.getConfirmPassword().trim());
        user.setPassword(bCryptPasswordEncoder.encode(userEditPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    @Override
    public long deleteUser(UserDeleteDTO userDeleteDTO) {
        String password = userDeleteDTO.getPassword().trim();
        if (!isValidPassword(password)) {
            throw new BadRequestException(WRONG_CREDENTIAL);
        }
        matchingPasswords(password, userDeleteDTO.getConfirmPassword().trim());
        User user = userRepository.findById(userDeleteDTO.getId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        String deleteMessage = String.valueOf(LocalDateTime.now());
        user.setFirstName(deleteMessage);
        user.setLastName(deleteMessage);
        user.setUsername(deleteMessage);
        user.setEmail(deleteMessage);
        user.setActive(false);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public boolean userIsAdmin(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return user.isAdmin();
    }

    @Override
    public List<UserWithoutPasswordDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().filter(User::isActive)
                .map(user -> modelMapper.map(user, UserWithoutPasswordDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserWithoutPasswordDTO getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    @Override
    public UserWithoutPasswordDTO login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername().trim();
        String password = userLoginDTO.getPassword().trim();
        if (!isValidName(username) || !isValidPassword(password)) {
            throw new BadRequestException(WRONG_CREDENTIAL);
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException(WRONG_CREDENTIAL));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(WRONG_CREDENTIAL);
        }
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    @Override
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

    @Override
    public User getUser(long id) {
        return userRepository.getUserById(true, id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    private void validationUserRegisterDTO(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername().trim();
        String password = userRegisterDTO.getPassword().trim();
        validationNames(username,
                userRegisterDTO.getFirstName(),
                userRegisterDTO.getLastName());
        if (!isValidPassword(password)) {
            throw new BadRequestException(INVALID_PASSWORD);
        }
        matchingPasswords(userRegisterDTO.getConfirmPassword().trim(), password);
        validationEmail(userRegisterDTO.getEmail().trim());
        validationAge(userRegisterDTO.getDateOfBirth());
        if (userRepository.existsByUsername(username) ||
                userRepository.existsByEmail(userRegisterDTO.getEmail().trim())) {
            throw new BadRequestException(USERNAME_OR_EMAIL_ALREADY_EXIST);
        }
    }

    private User validationUserCredential(String password, long userId) {
        if (isValidPassword(password)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                throw new UnauthorizedException(WRONG_CREDENTIAL);
            }
            return user;
        }
        throw new BadRequestException(WRONG_CREDENTIAL);
    }

    private void validationNames(String username, String firstName, String lastName) {
        if (!isValidName(username) || !isValidName(firstName) || !isValidName(lastName)) {
            throw new BadRequestException(INVALID_DATA);
        }
    }

    private void matchingPasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException(PASSWORDS_MISMATCH);
        }
    }

    private void validationAge(LocalDate dateOfBirth) {
        if (LocalDate.now().getYear() - dateOfBirth.getYear() < 18) {
            throw new BadRequestException(INVALID_AGE);
        }
    }

    private void validationEmail(String email) {
        String regex = "^[\\w]{2,}@[\\w]{1,}.[\\w]{2,3}$";
        if (!Pattern.compile(regex).matcher(email).matches()) {
            throw new BadRequestException(INVALID_EMAIL);
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    private boolean isValidName(String input) {
        return input != null && input.length() >= 2 && input.length() < 15;
    }
}