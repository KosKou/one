package com.example.one.web;

import com.example.one.exception.ErrorCode;
import com.example.one.webdto.response.BaseWebResponse;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class ExceptionHandlerRestControllerTest {

    @InjectMocks
    private ExceptionHandlerRestController controller;

    @Test
    public void handleEntityNotFoundException() {
//        Mockito.when(controller.handleEntityNotFoundException())
//                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(BaseWebResponse.error(ErrorCode.ENTITY_NOT_FOUND)));
//        TestObserver testObserver = controller.handleNoSuchElementException();
    }

    @Test
    public void handleLazyInitializationException() {
    }

    @Test
    public void handleNoSuchElementException() {
    }
}