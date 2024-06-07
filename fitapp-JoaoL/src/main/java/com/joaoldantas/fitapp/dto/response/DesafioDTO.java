package com.joaoldantas.fitapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joaoldantas.fitapp.dto.response.util.UsuarioClassificacaoDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioReferenciaDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioTreinoFeitoReferenciaDTO;
import com.joaoldantas.fitapp.util.TipoDesafio;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record DesafioDTO(
        String nome,
        String descricao,
        String imagem,
        String created_At,
        String start_date,
        String end_date,
        String premiacao,
        TipoDesafio tipo_desafio,
        List<UsuarioClassificacaoDTO> ranking,
        List<UsuarioReferenciaDTO> participantes,
        List<UsuarioTreinoFeitoReferenciaDTO> treinos_postados,
        UsuarioReferenciaDTO criador,
        boolean finalizado
) {
}
