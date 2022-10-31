package com.dreekde.sportal.controller;

import com.dreekde.sportal.model.dto.ExceptionDTO;
import com.dreekde.sportal.model.exceptions.AuthenticationException;
import com.dreekde.sportal.model.exceptions.BadRequestException;
import com.dreekde.sportal.model.exceptions.MethodNotAllowedException;
import com.dreekde.sportal.model.exceptions.NotFoundException;
import com.dreekde.sportal.model.exceptions.UnauthorizedException;
import com.dreekde.sportal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    protected static final String REMOTE_ADDRESS = "REMOTE_ADDRESS";
    protected static final String NOT_LOGGED = "You are not logged!";
    protected static final String WRONG_CREDENTIAL = "Wrong credential!";
    protected static final String ALREADY_LOGGED = "You already logged!";
    protected static final String LOGOUT = "Logout success!";
    protected static final String NOT_ALLOWED = "Not allowed operation!";
    protected static final String UNAUTHORIZED = "Not authorized!";
    protected static final String NOT_FOUND = "File not found!";
    protected static final String DIRECTORY = "uploads";

    Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    private UserService userService;

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    private ExceptionDTO handleMethodNotAllowed(Exception message) {
        return creatExceptionDTO(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

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
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setStatus(httpStatus.value());
        exceptionDTO.setDateTime(LocalDateTime.now());
        exceptionDTO.setMessage(message.getMessage());
        logger.error(exceptionDTO.getMessage(), message.getMessage());
        return exceptionDTO;
    }

    protected void loginUser(HttpServletRequest request, long id) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED, true);
        session.setAttribute(USER_ID, id);
        session.setAttribute(REMOTE_ADDRESS, request.getRemoteAddr());
    }

    protected long getLoggedUserId(HttpSession session) {
        if (session.getAttribute(USER_ID) == null) {
            throw new AuthenticationException(NOT_LOGGED);
        }
        return (long) session.getAttribute(USER_ID);
    }

    protected boolean isLogged(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return !session.isNew()
                && session.getAttribute(LOGGED) != null
                && (boolean) session.getAttribute(LOGGED)
                && request.getRemoteAddr().equals(session.getAttribute(REMOTE_ADDRESS));
    }

    protected void validateAdmin(HttpSession session) {
        long loggedUserId = getLoggedUserId(session);
        if (loggedUserId > 0) {
            if (!userService.userIsAdmin(loggedUserId)) {
                throw new AuthenticationException(UNAUTHORIZED);
            }
        }
    }

}