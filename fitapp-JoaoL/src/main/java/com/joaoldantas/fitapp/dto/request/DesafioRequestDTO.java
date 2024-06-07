package com.joaoldantas.fitapp.dto.request;

import com.joaoldantas.fitapp.util.TipoDesafio;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DesafioRequestDTO(
        String nome,
        String descricao,
        String premiacao,
        String imagem,
        TipoDesafio tipo_desafio,
        @NotNull(message = "Challenge needs a starting date.") LocalDate data_inicio,
        @NotNull(message = "Challenge needs a ending date.") LocalDate data_final
) {
}
