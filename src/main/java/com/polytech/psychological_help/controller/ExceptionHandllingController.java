package com.polytech.psychological_help.controller;

import com.polytech.psychological_help.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandllingController {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handleEntityNotFoundException(EntityNotFoundException ex, HandlerMethod hm) {
        log.error("handleEntityNotFoundException : {}. method: {}", ex.getMessage(), hm.getMethod().getName());
        return new ApiError(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ApiError> handleNotValidArgumentException(MethodArgumentNotValidException ex, HandlerMethod hm) {
        log.error("handleNotValidArgumentException : {}. method: {}", ex.getMessage(), hm.getMethod().getName());
        return ex.getBindingResult().getAllErrors().stream()
                .map(err -> new ApiError(err.getDefaultMessage(),
                        LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(Exception ex, HandlerMethod hm) {
        log.error("handleInternalServerError : {}. method: {}", ex.getMessage(), hm.getMethod().getName());
        return new ApiError(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTokenNotValidException(Exception ex, HandlerMethod hm) {
        log.error("handleTokenNotValidException : {}. method: {}", ex.getMessage(), hm.getMethod().getName());
        return new ApiError(ex.getMessage(), LocalDateTime.now());
    }
}
