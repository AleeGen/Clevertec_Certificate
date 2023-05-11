package ru.clevertec.ecl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.exception.UtilException;
import ru.clevertec.ecl.exception.response.AbstractErrorResponse;
import ru.clevertec.ecl.exception.response.ServiceErrorResponse;
import ru.clevertec.ecl.exception.response.UtilErrorResponse;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<ServiceErrorResponse> handleException(ServiceException e) {
        return getResponse(new ServiceErrorResponse(), e);
    }

    @ExceptionHandler
    public ResponseEntity<UtilErrorResponse> handleException(UtilException e) {
        return getResponse(new UtilErrorResponse(), e);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(Objects.toString(e.getDetailMessageArguments()[1]), HttpStatus.BAD_REQUEST);
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