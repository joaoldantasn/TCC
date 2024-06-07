package com.joaoldantas.fitapp.service;

import com.joaoldantas.fitapp.dto.mapper.DesafioDTOMapper;
import com.joaoldantas.fitapp.dto.response.DesafioDTO;
import com.joaoldantas.fitapp.entity.Desafio;
import com.joaoldantas.fitapp.entity.Usuario;
import com.joaoldantas.fitapp.exception.DesafioNotFoundException;
import com.joaoldantas.fitapp.exception.UsuarioNotFoundException;
import com.joaoldantas.fitapp.repository.DesafioRepository;
import com.joaoldantas.fitapp.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DesafioService {

    private final DesafioRepository desafioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DesafioDTOMapper desafioDTOMapper;

    public DesafioService(DesafioRepository desafioRepository, UsuarioRepository usuarioRepository, DesafioDTOMapper desafioDTOMapper) {
        this.desafioRepository = desafioRepository;
        this.usuarioRepository = usuarioRepository;
        this.desafioDTOMapper = desafioDTOMapper;
    }

    // Usuario pode checar desafios disponiveis para entrar
    public List<DesafioDTO> checaDesafiosDisponiveis() {
        Usuario user = pegaAtualUsuarioAutenticado();

        List<Desafio> desafiosUsuarioNaoParticipando = desafioRepository.findDesafiosUsuarioNaoParticipando(user.getEmail());

        if(desafiosUsuarioNaoParticipando.isEmpty()) {
            throw new DesafioNotFoundException(String.format("Nenhum desafio disponível para usuário '%s'.", user.getNome()));
        }

        return desafiosUsuarioNaoParticipando.stream()
                .map(desafioDTOMapper::mapUsuarioDesafios)
                .collect(Collectors.toList());
    }

    // Usuario pode checar informações de um desafio especifico
    public DesafioDTO checaDesafio(String desafioNome) {
        return Optional.of(desafioRepository.findByNome(desafioNome))
                .map(desafioDTOMapper)
                .orElseThrow(() -> new DesafioNotFoundException(String.format("Desafio de nome '%s' não foi encontrado.", desafioNome)));
    }

    // Usuario pode checar desafios que entrou
    public List<DesafioDTO> checaDesafios() {
        Usuario user = pegaAtualUsuarioAutenticado();

        if(user.getDesafios().isEmpty())
            throw new DesafioNotFoundException(String.format("Usuário '%s' não está em nenhum desafio.", user.getNome()));

        return user.getDesafios().stream()
                .map(desafioDTOMapper::mapUsuarioDesafios)
                .collect(Collectors.toList());
    }

    // Usuario pode checar desafios que criou
    public List<DesafioDTO> checaDesafiosCriado() {
        Usuario user = pegaAtualUsuarioAutenticado();

        if(user.getDesafiosCriados().isEmpty())
            throw new DesafioNotFoundException(String.format("Usuário '%s' não possui desafios criados.", user.getNome()));

        return user.getDesafiosCriados().stream()
                .map(desafioDTOMapper::mapUsuarioDesafios)
                .collect(Collectors.toList());
    }

    private Usuario pegaAtualUsuarioAutenticado() {
        return Optional.of(usuarioRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UsuarioNotFoundException("Error ao encontrar as informações do usuário."));
    }
}
