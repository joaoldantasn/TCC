package com.joaoldantas.fitapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoDesafio {
    CARDIO("CARDIO"),
    EXERCICIOS("EXERCICIOS"),
    PRESENCA("PRESENCA");

    private final String nome;
}
