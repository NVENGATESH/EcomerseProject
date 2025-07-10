package com.ecommerce.project.eception;

import com.ecommerce.project.payload.ApiResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class MyGlobelException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> responce = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err->{
            String fildname=((FieldError)err).getField();
            String message=err.getDefaultMessage();
            responce.put(fildname,message);
        });
        return new ResponseEntity<Map<String,String>>(responce, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String>myResourceNotFoundException(ResourceNotFoundException e){
//        String message=e.getMessage();
//        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
//    }
//
//
//    @ExceptionHandler(ApiException.class)
//    public ResponseEntity<String>myApiException(ApiException e){
//        String message=e.getMessage();
//        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponce>myResourceNotFoundException(ResourceNotFoundException e){
        String message=e.getMessage();
        ApiResponce responce=new ApiResponce(message,false);
        return new ResponseEntity<>(responce,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponce>myApiException(ApiException e){
        String message=e.getMessage();
        ApiResponce responce=new ApiResponce(message,false);
        return new ResponseEntity<>(responce,HttpStatus.BAD_REQUEST);
    }
}


