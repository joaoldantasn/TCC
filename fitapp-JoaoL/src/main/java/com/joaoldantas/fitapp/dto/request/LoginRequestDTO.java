package com.joaoldantas.fitapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Formato de email inválido!") String email,
        @NotBlank(message = "Senha não pode estar vazia!") String senha
) {
}
