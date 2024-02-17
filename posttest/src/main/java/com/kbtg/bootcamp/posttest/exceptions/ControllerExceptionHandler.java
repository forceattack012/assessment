package com.kbtg.bootcamp.posttest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleBadRequest(MethodArgumentNotValidException methodArgumentNotValidException, WebRequest webRequest){

        List<String> errors = methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(f -> f.getField() + " " + f.getDefaultMessage())
                .toList();

        String message = String.join(",", errors);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {InternalServerException.class})
    public ResponseEntity<ApiErrorResponse> handleInternalServerException(InternalServerException exception, WebRequest webRequest){
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException exception, WebRequest webRequest){
        ApiErrorResponse apiErrorResponse =  new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }
}
