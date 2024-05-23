package com.otus.network.controller;

import com.otus.network.exception.NoConnectionException;
import com.otus.network.exception.NotExecuteQueryException;
import com.otus.network.exception.NotFoundException;
import com.otus.network.exception.ValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ResponseWrapperAdvice {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<String> resourceNotFoundException(NotFoundException ex, WebRequest request) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotExecuteQueryException.class, NoConnectionException.class})
    public ResponseEntity<String> dbException(Exception ex, WebRequest request) {
        return new ResponseEntity<String>(ex.getClass().toString(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {ValidateException.class})
    public ResponseEntity<String> validateException(Exception ex, WebRequest request) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
