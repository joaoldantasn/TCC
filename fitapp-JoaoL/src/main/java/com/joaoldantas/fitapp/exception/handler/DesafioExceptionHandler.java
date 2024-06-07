package com.joaoldantas.fitapp.exception.handler;

import com.joaoldantas.fitapp.exception.DesafioFinalizadoException;
import com.joaoldantas.fitapp.exception.DesafioNotFoundException;
import com.joaoldantas.fitapp.exception.errorResponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DesafioExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(DesafioNotFoundException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMensagem());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(DesafioFinalizadoException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.LOCKED.value(), exc.getMensagem());
        return new ResponseEntity<>(error, HttpStatus.LOCKED);
    }

}
