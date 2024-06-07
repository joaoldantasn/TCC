package com.joaoldantas.fitapp.dto.response.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record UsuarioDesafioDTO(
        String nome_desafio,
        Double pontuacao,
        boolean finalizado
) {
}
