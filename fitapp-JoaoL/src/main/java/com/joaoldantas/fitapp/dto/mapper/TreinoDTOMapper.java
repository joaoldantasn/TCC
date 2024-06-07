package com.joaoldantas.fitapp.dto.mapper;

import com.joaoldantas.fitapp.dto.request.TreinoRequestDTO;
import com.joaoldantas.fitapp.dto.response.ExercicioDTO;
import com.joaoldantas.fitapp.dto.response.TreinoDTO;
import com.joaoldantas.fitapp.entity.Exercicio;
import com.joaoldantas.fitapp.entity.Treino;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class TreinoDTOMapper implements Function<Treino, TreinoDTO> {

    @Override
    public TreinoDTO apply(Treino treino) {

        List<ExercicioDTO> exercicioDTOS = new ArrayList<>();
        if(treino.getExercicios() != null) {
            for(Exercicio e : treino.getExercicios()) {
                exercicioDTOS.add(ExercicioDTO.builder()
                        .nome(e.getNome())
                        .build());
            }
        }

        return TreinoDTO.builder()
                .nome(treino.getNome())
                .descricao(treino.getDescricao())
                .feito_em(treino.getDataTreino())
                .pontuacao(treino.getPontuacao())
                .exercicios_feitos(exercicioDTOS)
                .build();
    }

    public Treino mapRequestToEntity(TreinoRequestDTO treinoRequestDTO) {

        List<Exercicio> exercicios = new ArrayList<>();
        if(treinoRequestDTO.exercicios() != null) {
            for(TreinoRequestDTO.ExercicioRequestDTO e : treinoRequestDTO.exercicios()) {
                exercicios.add(Exercicio.builder()
                        .nome(e.nome())
                        .build());
            }
        }

        return Treino.builder()
                .nome(treinoRequestDTO.nome())
                .descricao(treinoRequestDTO.descricao())
                .exercicios(exercicios)
                .build();
    }
}
