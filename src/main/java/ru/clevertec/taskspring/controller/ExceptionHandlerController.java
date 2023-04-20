package ru.clevertec.taskspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.taskspring.exception.ServiceException;
import ru.clevertec.taskspring.exception.response.AbstractErrorResponse;
import ru.clevertec.taskspring.exception.response.ServiceErrorResponse;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<ServiceErrorResponse> handleException(ServiceException e) {
        return getResponse(new ServiceErrorResponse(), e);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T extends AbstractErrorResponse> ResponseEntity<T> getResponse(T error, Exception e) {
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}