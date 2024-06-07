package com.joaoldantas.fitapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record TreinoRequestDTO(
        @NotBlank(message = "Nome não pode estar vazio!") String nome,
        @NotBlank(message = "Descrição não pode estar vazio!") String descricao,
        List<ExercicioRequestDTO> exercicios
) {
    @Builder
    public record ExercicioRequestDTO(
            @NotBlank(message = "Nome do exercício não pode estar vazio!") String nome
    ) {
    }
}
