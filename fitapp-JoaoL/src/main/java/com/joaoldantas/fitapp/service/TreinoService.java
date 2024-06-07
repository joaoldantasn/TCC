package com.joaoldantas.fitapp.service;

import com.joaoldantas.fitapp.dto.mapper.TreinoDTOMapper;
import com.joaoldantas.fitapp.dto.response.TreinoDTO;
import com.joaoldantas.fitapp.entity.Desafio;
import com.joaoldantas.fitapp.entity.Usuario;
import com.joaoldantas.fitapp.exception.DesafioNotFoundException;
import com.joaoldantas.fitapp.exception.UsuarioCommandoInvalidoException;
import com.joaoldantas.fitapp.exception.UsuarioNotFoundException;
import com.joaoldantas.fitapp.repository.DesafioRepository;
import com.joaoldantas.fitapp.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreinoService {

    private final DesafioRepository desafioRepository;
    private final UsuarioRepository usuarioRepository;
    private final TreinoDTOMapper treinoDTOMapper;

    public TreinoService(DesafioRepository desafioRepository,
                         UsuarioRepository usuarioRepository,
                         TreinoDTOMapper treinoDTOMapper)
    {
        this.desafioRepository = desafioRepository;
        this.usuarioRepository = usuarioRepository;
        this.treinoDTOMapper = treinoDTOMapper;
    }

    // Usuario pode checar seus treinos em um desafio especifico
    public TreinoDTO.DesafioTreinoDTO checarTreinos(String desafioNome) {
        Desafio desafio = Optional.ofNullable(desafioRepository.findByNome(desafioNome))
                .orElseThrow(() -> new DesafioNotFoundException(String.format("Desafio com nome '%s' não foi encontrado.", desafioNome)));

        Usuario user = pegaAtualUsuarioAutenticado();

        if(!desafio.getParticipantes().contains(user)) {
            throw new UsuarioCommandoInvalidoException(
                    String.format("Usuário '%s' não está neste desafio.", user.getNome()));
        }

        List<TreinoDTO> treinoDTOS = user.getTreinos().stream()
                .filter(t -> t.getDesafio().getNome().equals(desafio.getNome()))
                .map(treinoDTOMapper)
                .collect(Collectors.toList());

        if(treinoDTOS.isEmpty())
            throw new DesafioNotFoundException(
                    String.format("Nenhum treino encontrado no desafio '%s'.", desafio.getNome()));

        return TreinoDTO.DesafioTreinoDTO.builder()
                .nome_desafio(desafioNome)
                .treinos(treinoDTOS)
                .build();
    }

    private Usuario pegaAtualUsuarioAutenticado() {
        return Optional.of(usuarioRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UsuarioNotFoundException("Error ao encontrar as informações do usuário."));
    }
}
