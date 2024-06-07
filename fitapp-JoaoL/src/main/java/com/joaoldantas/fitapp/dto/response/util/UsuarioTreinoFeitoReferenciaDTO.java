package com.joaoldantas.fitapp.dto.response.util;

import com.joaoldantas.fitapp.dto.response.ExercicioDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record UsuarioTreinoFeitoReferenciaDTO(
        String nome_treino,
        String data_treino,
        String descricao,
        String usuario,
        Double pontuacao,
        List<ExercicioDTO> exercicios_feitos
) {
}
