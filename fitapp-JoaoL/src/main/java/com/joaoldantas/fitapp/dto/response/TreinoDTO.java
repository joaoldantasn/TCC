package com.joaoldantas.fitapp.dto.response;

import com.joaoldantas.fitapp.entity.Treino;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TreinoDTO(
        String nome,
        String descricao,
        LocalDateTime feito_em,
        double pontuacao,
        List<ExercicioDTO> exercicios_feitos
) {
    @Builder
    public record DesafioTreinoDTO(
       String nome_desafio,
       List<TreinoDTO> treinos
    ) {
    }
}
