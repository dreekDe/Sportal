package com.dreekde.sportal.service.impl;

import com.dreekde.sportal.model.dto.user.UserRegisterDTO;
import com.dreekde.sportal.model.dto.user.UserWithoutPasswordDTO;
import com.dreekde.sportal.model.entities.User;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.repositories.UserRepository;
import com.dreekde.sportal.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * @author Desislava Tencheva
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String INVALID_DATA = " cannot be shorter than 2 or longer than 15 characters!";
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

    public UserWithoutPasswordDTO register(UserRegisterDTO userRegisterDTO) {
        validationUserRegisterDTO(userRegisterDTO);
        User user = modelMapper.map(userRegisterDTO, User.class);
        System.out.println(user.getFirstName());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        if (user.getId() == 1) {
            user.setAdmin(true);
            userRepository.save(user);
        }
        System.out.println("tuka e");
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    private void validationUserRegisterDTO(UserRegisterDTO userRegisterDTO) {
        if(userRepository.existsByUsername(userRegisterDTO.getUsername())||
                userRepository.existsByEmail(userRegisterDTO.getEmail())){
            throw new BadRequestException(USERNAME_OR_EMAIL_ALREADY_EXIST);
        }
        validationInputText(userRegisterDTO.getUsername(), "Username");
        validationInputText(userRegisterDTO.getFirstName(), "First name");
        validationInputText(userRegisterDTO.getLastName(), "Last name");
        validationPassword(userRegisterDTO.getPassword(), userRegisterDTO.getConfirmPassword());
        validationEmail(userRegisterDTO.getEmail());
        validationAge(userRegisterDTO.getDateOfBirth());
    }

    private void validationAge(LocalDate dateOfBirth) {
        if (LocalDate.now().getYear() - dateOfBirth.getYear() < 18) {
            throw new BadRequestException(INVALID_AGE);
        }
    }

    private void validationEmail(String email) {
        String regex = "^[\\w]{2,}@[\\w]{1,}.[\\w]{2,3}$";
        if(!Pattern.compile(regex).matcher(email).matches()){
            throw new BadRequestException(INVALID_EMAIL);
        }
    }

    private void validationPassword(String password, String confirmEmail) {
        if (!password.equals(confirmEmail) || password.length() < 8) {
            throw new BadRequestException(PASSWORDS_MISMATCH);
        }
    }

    private void validationInputText(String input, String message) {
        if (input == null || input.length() >= 15 || input.length() < 2) {
            throw new BadRequestException(message + INVALID_DATA);
        }
    }
}