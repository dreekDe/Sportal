package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.ExceptionDTO;
import com.dreekde.sportal.model.exceptions.AuthenticationException;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.exceptions.UnauthorizedException;
import com.dreekde.sportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @author Desislava Tencheva
 */
public abstract class AbstractController {

    protected static final String LOGGED = "LOGGED";
    protected static final String USER_ID = "USER_ID";
    private static final String REMOTE_ADDRESS = "REMOTE_ADDRESS";

    @Autowired
    private UserService userService;

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDTO handleNotAuthorized(Exception message) {
        return creatExceptionDTO(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ExceptionDTO notFoundException(Exception message) {
        return creatExceptionDTO(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ExceptionDTO unauthorizedException(Exception message) {
        return creatExceptionDTO(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ExceptionDTO badRequestException(Exception message) {
        return creatExceptionDTO(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleAllOthers(Exception exception) {
        return creatExceptionDTO(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionDTO creatExceptionDTO(Exception message, HttpStatus httpStatus) {
        message.printStackTrace(); //todo logger
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setStatus(httpStatus.value());
        exceptionDTO.setDateTime(LocalDateTime.now());
        exceptionDTO.setMessage(message.getMessage());
        return exceptionDTO;
    }

    public boolean isAdmin(long id) {
        return userService.userIsAdmin(id);
    }

    public void loginUser(HttpServletRequest request, long id) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED, true);
        session.setAttribute(USER_ID, id);
        session.setAttribute(REMOTE_ADDRESS, request.getRemoteAddr());
    }

    public long getLoggedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (isLogged(request, session)) {
            return (long) session.getAttribute(USER_ID);
        }
        return 0;//TODO
    }

    private boolean isLogged(HttpServletRequest request, HttpSession session) {
        return !session.isNew()
                && session.getAttribute(LOGGED) != null
                && (boolean) session.getAttribute(LOGGED)
                && request.getRemoteAddr().equals(session.getAttribute(REMOTE_ADDRESS));
    }
}