package com.joaoldantas.fitapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joaoldantas.fitapp.dto.response.util.UsuarioDesafioDTO;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record UsuarioDTO(
        String nome,
        String email,
        String telefone,
        Double altura,
        Double peso,
        String tipo_conta,
        List<UsuarioDesafioDTO> desafios,
        List<UsuarioDesafioDTO> desafios_criados,
        String foto_perfil
) {
}
