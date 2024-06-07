package com.joaoldantas.fitapp.dto.response.util;

import lombok.Builder;

@Builder
public record UsuarioClassificacaoDTO(
        int classificacao,
        UsuarioReferenciaDTO usuario
) {
}
