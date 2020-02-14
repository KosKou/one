package com.example.one.web;

import com.example.one.exception.ErrorCode;
import com.example.one.webdto.response.BaseWebResponse;
import org.hibernate.LazyInitializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerRestController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseWebResponse> handleEntityNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseWebResponse.error(ErrorCode.ENTITY_NOT_FOUND));
    }

    @ExceptionHandler(LazyInitializationException.class)
    public ResponseEntity<BaseWebResponse> handleLazyInitializationException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseWebResponse.error(ErrorCode.LAZY_INITIALIZATION));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseWebResponse> handleNoSuchElementException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseWebResponse.error(ErrorCode.NO_SUCH_ELEMENT));
    }
}
