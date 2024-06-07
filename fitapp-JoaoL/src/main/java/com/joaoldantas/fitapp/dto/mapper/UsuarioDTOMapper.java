package com.joaoldantas.fitapp.dto.mapper;

import com.joaoldantas.fitapp.dto.request.UsuarioRequestDTO;
import com.joaoldantas.fitapp.dto.response.UsuarioDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioDesafioDTO;
import com.joaoldantas.fitapp.entity.Desafio;
import com.joaoldantas.fitapp.entity.Treino;
import com.joaoldantas.fitapp.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class UsuarioDTOMapper implements Function<Usuario, UsuarioDTO> {

    @Override
    public UsuarioDTO apply(Usuario usuario) {

        // Pega desafios que usuario faz parte
        List<UsuarioDesafioDTO> desafios = new ArrayList<>();
        for(Desafio d : usuario.getDesafios()) {

            // Pega a pontuação total que o usuario tem em um desafio
            List<Treino> treinosPoints = usuario.getTreinos();
            double pontuacao = 0;
            for(Treino treino : treinosPoints) {
                if(treino.getDesafio() != null && treino.getDesafio().getNome().equals(d.getNome())) {
                    pontuacao += treino.getPontuacao();
                }
            }

            desafios.add(UsuarioDesafioDTO.builder()
                    .nome_desafio(d.getNome())
                    .finalizado(d.isFinalizado())
                    .pontuacao(pontuacao)
                    .build());
        }

        List<UsuarioDesafioDTO> desafios_criados = new ArrayList<>();
        for(Desafio d : usuario.getDesafiosCriados()) {
            desafios_criados.add(UsuarioDesafioDTO.builder()
                    .nome_desafio(d.getNome())
                    .finalizado(d.isFinalizado())
                    .build());
        }

        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .altura(usuario.getAltura())
                .peso(usuario.getPeso())
                .desafios(desafios)
                .desafios_criados(desafios_criados)
                .foto_perfil(usuario.getFotoPerfil())
                .tipo_conta(usuario.getRole().name().substring(5).substring(0, 1).toUpperCase() +
                        usuario.getRole().name().substring(5).substring(1).toLowerCase())
                .build();
    }

    public Usuario DtoToEntityFromRequest(UsuarioRequestDTO usuarioRequestDTO) {
        return Usuario.builder()
                .nome(usuarioRequestDTO.nome())
                .email(usuarioRequestDTO.email())
                .senha(usuarioRequestDTO.senha())
                .telefone(usuarioRequestDTO.telefone())
                .altura(usuarioRequestDTO.altura())
                .peso(usuarioRequestDTO.peso())
                .FotoPerfil(usuarioRequestDTO.foto_perfil())
                .active(true)
                .build();
    }
}
