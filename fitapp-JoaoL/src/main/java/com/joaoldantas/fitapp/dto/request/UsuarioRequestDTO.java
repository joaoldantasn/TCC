package com.joaoldantas.fitapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UsuarioRequestDTO(
        @NotBlank(message = "Nome não pode estar vazio!") String nome,
        @NotBlank(message = "Email não pode estar vazio!")
        @Email(message = "Formato de email inválido!") String email,
        @NotBlank(message = "Senha não pode estar vazia!") String senha,
        @Pattern(regexp = "^\\+?[0-9\\s-]*$", message = "Formato de telefone inválido!")
        String telefone,
        double altura,
        double peso,
        String foto_perfil
) {
}
