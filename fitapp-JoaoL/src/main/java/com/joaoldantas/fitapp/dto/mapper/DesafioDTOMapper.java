package com.joaoldantas.fitapp.dto.mapper;

import com.joaoldantas.fitapp.dto.request.DesafioRequestDTO;
import com.joaoldantas.fitapp.dto.response.DesafioDTO;
import com.joaoldantas.fitapp.dto.response.ExercicioDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioClassificacaoDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioReferenciaDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioTreinoFeitoReferenciaDTO;
import com.joaoldantas.fitapp.entity.Desafio;
import com.joaoldantas.fitapp.entity.Treino;
import com.joaoldantas.fitapp.entity.Usuario;
import com.joaoldantas.fitapp.util.TipoDesafio;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Service
public class DesafioDTOMapper implements Function<Desafio, DesafioDTO> {

    @Override
    public DesafioDTO apply(Desafio desafio) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");
        DateTimeFormatter formatterWithoutHour = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        List<UsuarioClassificacaoDTO> ranking = new ArrayList<>();
        List<UsuarioReferenciaDTO> participantes = new ArrayList<>();
        // Pega os participantes e pega a pontuação atual no desafio
        if(desafio.getParticipantes() != null) {
            for(Usuario u : desafio.getParticipantes()) {

                // Checar se o treino faz parte do desafio para pegar a pontuação
                double pontuacao = 0;
                for(Treino treino : u.getTreinos()) {
                    if(treino.getDesafio().equals(desafio))
                        pontuacao = pontuacao + treino.getPontuacao();
                }

                participantes.add(UsuarioReferenciaDTO.builder()
                        .nome(u.getNome())
                        .peso(u.getPeso())
                        .altura(u.getAltura())
                        .pontuacao(pontuacao)
                        .build());

                if(desafio.isFinalizado()) {
                    ranking.add(UsuarioClassificacaoDTO.builder()
                            .usuario(UsuarioReferenciaDTO.builder()
                                    .nome(u.getNome())
                                    .peso(u.getPeso())
                                    .altura(u.getAltura())
                                    .pontuacao(pontuacao)
                                    .build())
                            .build());
                }
            }
        }

        ranking.sort(Comparator.comparingDouble((UsuarioClassificacaoDTO dto) -> dto.usuario().pontuacao()).reversed());
        List<UsuarioClassificacaoDTO> rankingList = new ArrayList<>();
        for(int i = 0; i < ranking.size(); i++) {
            rankingList.add(UsuarioClassificacaoDTO.builder()
                    .classificacao(i + 1)
                    .usuario(ranking.get(i).usuario())
                    .build());
        }

        // Pega os treinos postados no desafio
        List<UsuarioTreinoFeitoReferenciaDTO> treinosPostadosList = new ArrayList<>();
        if(desafio.getTreinos() != null) {
            for(Treino treino : desafio.getTreinos()) {
                treinosPostadosList.add(UsuarioTreinoFeitoReferenciaDTO.builder()
                        .nome_treino(treino.getNome())
                        .data_treino(treino.getDataTreino().format(formatter))
                        .descricao(treino.getDescricao())
                        .usuario(treino.getUsuario().getNome())
                        .pontuacao(treino.getPontuacao())
                        .exercicios_feitos(treino.getExercicios().stream().map(exe -> ExercicioDTO.builder()
                                .nome(exe.getNome())
                                .build()).toList())
                        .build());
            }
        }

        participantes.sort(Comparator.comparingDouble(UsuarioReferenciaDTO::pontuacao).reversed());

        return DesafioDTO.builder()
                .nome(desafio.getNome())
                .descricao(desafio.getDescricao())
                .created_At(desafio.getCreatedAt() != null ? desafio.getCreatedAt().format(formatter) : null)
                .start_date(desafio.getStartAt() != null ? desafio.getStartAt().format(formatterWithoutHour) : null)
                .end_date(desafio.getEndAt() != null ? desafio.getEndAt().format(formatterWithoutHour) : null)
                .premiacao(desafio.getPremiacao())
                .tipo_desafio(desafio.getTipo())
                .ranking(rankingList)
                .treinos_postados(treinosPostadosList)
                .participantes(participantes)
                .criador(UsuarioReferenciaDTO.builder()
                        .nome(desafio.getDesafioCriador().getNome())
                        .build())
                .finalizado(desafio.isFinalizado())
                .imagem(desafio.getImagem())
                .build();
    }

    public Desafio mapToEntidade(DesafioRequestDTO desafioRequestDTO) {
        return Desafio.builder()
                .nome(desafioRequestDTO.nome())
                .descricao(desafioRequestDTO.descricao())
                .startAt(desafioRequestDTO.data_inicio())
                .endAt(desafioRequestDTO.data_final())
                .premiacao(desafioRequestDTO.premiacao())
                .imagem(desafioRequestDTO.imagem())
                .tipo(desafioRequestDTO.tipo_desafio())
                .build();
    }

    public DesafioDTO mapUsuarioDesafios(Desafio desafio) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");
        DateTimeFormatter formatterWithoutHour = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return DesafioDTO.builder()
                .nome(desafio.getNome())
                .descricao(desafio.getDescricao())
                .created_At(desafio.getCreatedAt() != null ? desafio.getCreatedAt().format(formatter) : null)
                .start_date(desafio.getStartAt() != null ? desafio.getStartAt().format(formatterWithoutHour) : null)
                .end_date(desafio.getEndAt() != null ? desafio.getEndAt().format(formatterWithoutHour) : null)
                .premiacao(desafio.getPremiacao())
                .tipo_desafio(desafio.getTipo())
                .criador(UsuarioReferenciaDTO.builder()
                        .nome(desafio.getDesafioCriador().getNome())
                        .build())
                .finalizado(desafio.isFinalizado())
                .imagem(desafio.getImagem())
                .build();
    }
}
