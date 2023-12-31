package org.me.cursoSpringBoot.exceptions.handler;

import org.me.cursoSpringBoot.exceptions.ExceptionResponse;
import org.me.cursoSpringBoot.exceptions.InvalidJwtAuthenticationException;
import org.me.cursoSpringBoot.exceptions.RequiredObjectIsNullException;
import org.me.cursoSpringBoot.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest webRequest){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                                                                    exception.getMessage(),
                                                                    webRequest.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception exception, WebRequest webRequest){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception exception, WebRequest webRequest){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationExceptions(Exception exception, WebRequest webRequest){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}
