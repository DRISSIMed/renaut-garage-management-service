package com.renault.exception;

import com.renault.dto.ErrorResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GarageExceptionAdvice {
    @ExceptionHandler(MaxVehiculeExceedException.class)
    public ResponseEntity<ErrorResponseDto> handleVehicleExceed(MaxVehiculeExceedException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(ex.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value())));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(ex.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value())));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponseDto(ex.getMessage(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }
}
