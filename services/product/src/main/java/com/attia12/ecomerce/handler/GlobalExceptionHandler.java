package com.attia12.ecomerce.handler;


import com.attia12.ecomerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(ProductPurchaseException.class)
    public ResponseEntity<String> handler(ProductPurchaseException exp)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exp.getMessage());

    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException exp)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exp.getMessage());

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException exp)
    {
        var errors=new HashMap<String,String>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName=((FieldError)error).getField();
                    var errorMessage=error.getDefaultMessage();
                    errors.put(fieldName,errorMessage);
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));

    }

}
