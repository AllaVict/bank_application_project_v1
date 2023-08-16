package com.bank.http.handler;

import com.bank.core.validation.CoreError;
import com.bank.core.validation.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleControllerViolationException(ConstraintViolationException e){
        List<CoreError> errors = e.getConstraintViolations().stream()
                .map(exception -> exception.getPropertyPath() + ": " + exception.getMessage())
                .map(message -> new CoreError(message))
                .toList();
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
