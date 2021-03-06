/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.webservices.restfulwebservices.exception;

import it.apedano.webservices.restfulwebservices.user.UserNotFoundException;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Alessandro
 */
/*
Specialization of {@link Component @Component} for classes that declare
 * {@link ExceptionHandler @ExceptionHandler}, {@link InitBinder @InitBinder}, or
 * {@link ModelAttribute @ModelAttribute} methods to be shared across
 * multiple {@code @Controller} classes.
 */
@ControllerAdvice
@RestController
public class CustomizedResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class) //handles all exceptions
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class) //handles all exceptions
    public final ResponseEntity<Object> handleUserNotFoundExceptions(Exception ex, WebRequest request) throws Exception {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @Override //this method is called whenever an argument in a validation rule is not valid (name of birthdate in the User class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                "Validation failed",
                ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(" - "))); //details of what went wrong
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
