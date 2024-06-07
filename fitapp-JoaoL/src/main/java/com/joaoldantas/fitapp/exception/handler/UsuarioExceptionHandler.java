package com.joaoldantas.fitapp.exception.handler;

import com.joaoldantas.fitapp.exception.SenhaInvalidaException;
import com.joaoldantas.fitapp.exception.UsuarioCommandoInvalidoException;
import com.joaoldantas.fitapp.exception.UsuarioNotFoundException;
import com.joaoldantas.fitapp.exception.errorResponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsuarioExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UsuarioNotFoundException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMensagem());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(SenhaInvalidaException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMensagem());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UsuarioCommandoInvalidoException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), exc.getMensagem());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
