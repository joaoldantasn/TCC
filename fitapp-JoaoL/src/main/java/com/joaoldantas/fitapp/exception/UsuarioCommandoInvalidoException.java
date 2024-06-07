package com.joaoldantas.fitapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@AllArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class UsuarioCommandoInvalidoException extends RuntimeException {
    private String mensagem;
}
