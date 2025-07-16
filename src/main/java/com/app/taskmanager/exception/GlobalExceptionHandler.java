package com.app.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleDeserializationError(HttpMessageNotReadableException ex){
        if(ex.getMessage()!= null && ex.getMessage().contains("java.time.LocalDate")){
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid date format for 'dueDate'. Expected format: yyyy-MM-dd"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid request"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message" , ex.getMessage()));
    }
}
