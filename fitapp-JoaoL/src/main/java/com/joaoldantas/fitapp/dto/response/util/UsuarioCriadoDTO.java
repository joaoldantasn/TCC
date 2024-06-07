package com.joaoldantas.fitapp.dto.response.util;

import lombok.Builder;

@Builder
public record UsuarioCriadoDTO(
        String nome,
        String email,
        String mensagem
) {
}
