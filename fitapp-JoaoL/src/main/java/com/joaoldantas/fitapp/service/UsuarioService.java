package com.joaoldantas.fitapp.service;

import com.joaoldantas.fitapp.dto.mapper.DesafioDTOMapper;
import com.joaoldantas.fitapp.dto.mapper.TreinoDTOMapper;
import com.joaoldantas.fitapp.dto.mapper.UsuarioDTOMapper;
import com.joaoldantas.fitapp.dto.request.DesafioRequestDTO;
import com.joaoldantas.fitapp.dto.request.TreinoRequestDTO;
import com.joaoldantas.fitapp.dto.request.UsuarioRequestDTO;
import com.joaoldantas.fitapp.dto.response.DesafioDTO;
import com.joaoldantas.fitapp.dto.response.UsuarioDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioCriadoDTO;
import com.joaoldantas.fitapp.entity.Desafio;
import com.joaoldantas.fitapp.entity.Exercicio;
import com.joaoldantas.fitapp.entity.Treino;
import com.joaoldantas.fitapp.entity.Usuario;
import com.joaoldantas.fitapp.exception.DesafioFinalizadoException;
import com.joaoldantas.fitapp.exception.DesafioNotFoundException;
import com.joaoldantas.fitapp.exception.ExerciciosNotFoundException;
import com.joaoldantas.fitapp.exception.IntegrityConstraintViolationException;
import com.joaoldantas.fitapp.exception.UsuarioCommandoInvalidoException;
import com.joaoldantas.fitapp.exception.UsuarioNotFoundException;
import com.joaoldantas.fitapp.exception.UsuarioPermissaoInsuficienteException;
import com.joaoldantas.fitapp.repository.DesafioRepository;
import com.joaoldantas.fitapp.repository.ExercicioRepository;
import com.joaoldantas.fitapp.repository.UsuarioRepository;
import com.joaoldantas.fitapp.util.TipoDesafio;
import com.joaoldantas.fitapp.util.UsuarioRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DesafioRepository desafioRepository;
    private final ExercicioRepository exercicioRepository;
    private final UsuarioDTOMapper usuarioDTOMapper;
    private final DesafioDTOMapper desafioDTOMapper;
    private final TreinoDTOMapper treinoDTOMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          DesafioRepository desafioRepository,
                          ExercicioRepository exercicioRepository,
                          UsuarioDTOMapper usuarioDTOMapper,
                          DesafioDTOMapper desafioDTOMapper,
                          TreinoDTOMapper treinoDTOMapper,
                          BCryptPasswordEncoder passwordEncoder)
    {
        this.usuarioRepository = usuarioRepository;
        this.desafioRepository = desafioRepository;
        this.exercicioRepository = exercicioRepository;
        this.usuarioDTOMapper = usuarioDTOMapper;
        this.desafioDTOMapper = desafioDTOMapper;
        this.treinoDTOMapper = treinoDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Usuario pode ver sua informações
    public UsuarioDTO usuarioChecaInfo() {
        Usuario user = pegaAtualUsuarioAutenticado();
        return usuarioDTOMapper.apply(user);
    }

    // Usuario pode alterar suas informações
    public Optional<UsuarioDTO> usuarioAlteraInfo(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario user = pegaAtualUsuarioAutenticado();

        if(Optional.ofNullable(usuarioRepository.findByEmail(usuarioRequestDTO.email())).isPresent()) {
            throw new IntegrityConstraintViolationException(
                    String.format(
                            "Email '%s' já esta em uso.", usuarioRequestDTO.email()
                    ));
        }

        final String nome = StringUtils.hasText(usuarioRequestDTO.nome()) ? usuarioRequestDTO.nome() : user.getNome();
        final String email = StringUtils.hasText(usuarioRequestDTO.email()) ? usuarioRequestDTO.email() : user.getEmail();
        final String telefone = StringUtils.hasText(usuarioRequestDTO.telefone()) ?
                usuarioRequestDTO.telefone() : user.getTelefone();
        final String peso = (usuarioRequestDTO.peso() != 0.0) ?
                String.valueOf(usuarioRequestDTO.peso()) : String.valueOf(user.getPeso());
        final String altura = (usuarioRequestDTO.altura() != 0.0) ?
                String.valueOf(usuarioRequestDTO.altura()) : String.valueOf(user.getAltura());
        final String fotoPerfil = StringUtils.hasText(usuarioRequestDTO.foto_perfil()) ? usuarioRequestDTO.foto_perfil() : user.getFotoPerfil();

        String senha = user.getSenha();
        if(StringUtils.hasText(usuarioRequestDTO.senha()) && !passwordEncoder.matches(usuarioRequestDTO.senha(), user.getSenha())) {
            senha = passwordEncoder.encode(usuarioRequestDTO.senha());
        }

        Usuario usuarioAtualizado = Usuario.builder()
                .id(user.getId())
                .nome(nome)
                .email(email)
                .telefone(telefone)
                .altura(Double.parseDouble(altura))
                .peso(Double.parseDouble(peso))
                .treinos(user.getTreinos())
                .desafios(user.getDesafios())
                .desafiosCriados(user.getDesafiosCriados())
                .FotoPerfil(fotoPerfil)
                .senha(senha)
                .active(true)
                .role(user.getRole())
                .build();

        usuarioRepository.save(usuarioAtualizado);

        return usuarioRepository.findById(usuarioAtualizado.getId()).map(usuarioDTOMapper);
    }

    // Usuario pode entrar em desafio
    public DesafioDTO UsuarioEntraDesafio(String desafioNome) {
        Desafio desafio = Optional.of(desafioRepository.findByNome(desafioNome))
                .orElseThrow(() -> new DesafioNotFoundException(String.format("Desafio com nome '%s' não foi encontrado.", desafioNome)));

        checaDesafioFinalizado(desafio);

        Usuario user = pegaAtualUsuarioAutenticado();

        if(desafio.getParticipantes().contains(user)) {
            throw new UsuarioCommandoInvalidoException(
                    String.format("Usuário '%s' já está neste desafio.", user.getNome()));
        }

        desafio.getParticipantes().add(user);
        user.getDesafios().add(desafio);

        usuarioRepository.save(user);

        return desafioDTOMapper.apply(desafio);
    }

    // Usuario pode sair de desafio — Se o usuario que sair for o criador o desafio ACABA
    public DesafioDTO usuarioSairDesafio(String desafioNome) {
        Usuario user = pegaAtualUsuarioAutenticado();

        Desafio desafio = Optional.of(desafioRepository.findByNome(desafioNome))
                .orElseThrow(() -> new DesafioNotFoundException(String.format("Desafio com nome '%s' não foi encontrado.", desafioNome)));

        user.getDesafios().removeIf(d -> d.equals(desafio));
        desafio.getParticipantes().remove(user);

        if(desafio.getDesafioCriador().equals(user)) {
            desafio.setFinalizado(true);
        }

        usuarioRepository.save(user);

        return desafioDTOMapper.apply(desafio);
    }

    // Usuario pode postar treino em desafio
    public DesafioDTO usuarioPostaTreino(String desafioNome, TreinoRequestDTO treinoRequestDTO) {
        Desafio desafio = Optional.ofNullable(desafioRepository.findByNome(desafioNome))
                .orElseThrow(() -> new DesafioNotFoundException(String.format("Desafio com nome '%s' não foi encontrado.", desafioNome)));

        checaDesafioFinalizado(desafio);

        Usuario user = pegaAtualUsuarioAutenticado();

        /*
         * Presença - todos os tipos de exericios no treino consta como 1 ponto quando o tipo do desafio é presença
         * Exercicios - pontua qualquer tipo de exercicio independente do tipo
         * Cardio - apenas exercicios do tipo 01 de cardio pontuam
         */

        if(treinoRequestDTO.exercicios() == null || treinoRequestDTO.exercicios().isEmpty()) {
            throw new UsuarioCommandoInvalidoException("Para postar um treino é necessário especificar exercícios.");
        }

        Treino novoTreino = treinoDTOMapper.mapRequestToEntity(treinoRequestDTO);
        // Mapeador mapea os exercicios porem eles não estão relacionados com o banco de dados
        // Então no momento é limpo para ser inserido novamente
        novoTreino.setExercicios(new ArrayList<>());

        for(TreinoRequestDTO.ExercicioRequestDTO e : treinoRequestDTO.exercicios()) {
            Exercicio exe = Optional.ofNullable(exercicioRepository.findByNome(e.nome()))
                    .orElseThrow(() -> new ExerciciosNotFoundException(String.format("Exercício com nome '%s' não foi encontrado.", e.nome())));
            novoTreino.getExercicios().add(exe);
            exe.getTreinos().add(novoTreino);
        }

        List<Exercicio> exercicioList = novoTreino.getExercicios();
        double pontuacao = 0;
        switch(desafio.getTipo()) {
            case CARDIO -> {
                for(Exercicio e : exercicioList) {
                    String exeNum = e.getNome().substring(0, 2);
                    if(exeNum.equals("01")) {
                        pontuacao++;
                    }
                }
            }
            case EXERCICIOS -> {
                for(int i = 0; i < exercicioList.size(); i++) {
                    pontuacao++;
                }
            }
            case PRESENCA -> pontuacao = 1.0;
        }

        novoTreino.setDesafio(desafio);
        novoTreino.setUsuario(user);
        novoTreino.setPontuacao(pontuacao);

        desafio.getTreinos().add(novoTreino);
        user.getTreinos().add(novoTreino);

        usuarioRepository.save(user);

        return desafioDTOMapper.apply(desafio);
    }

    // Usuario Professor pode criar desafio
    public DesafioDTO usuarioCriaDesafio(DesafioRequestDTO desafioRequestDTO) {
        Usuario user = pegaAtualUsuarioAutenticado();

        if(Optional.ofNullable(desafioRepository.findByNome(desafioRequestDTO.nome())).isPresent()) {
            throw new IntegrityConstraintViolationException(
                    String.format("Desafio de nome '%s' já está em uso. Escolha um novo nome.", desafioRequestDTO.nome()));
        }

        Desafio novoDesafio = desafioDTOMapper.mapToEntidade(desafioRequestDTO);
        novoDesafio.setDesafioCriador(user);
        novoDesafio.setParticipantes(List.of(user));
        desafioRepository.save(novoDesafio);

        return desafioDTOMapper.apply(novoDesafio);
    }

    // Usuario Professor pode atualizar desafio
    public DesafioDTO usuarioAtualizaDesafio(String desafioNome, DesafioRequestDTO desafioRequestDTO) {
        Desafio desafio = Optional.ofNullable(desafioRepository.findByNome(desafioNome))
                .orElseThrow(() -> new DesafioNotFoundException(String.format("Desafio com nome '%s' não foi encontrado.", desafioNome)));

        checaDesafioFinalizado(desafio);

        if(Optional.ofNullable(desafioRepository.findByNome(desafioRequestDTO.nome())).isPresent()) {
            throw new IntegrityConstraintViolationException(
                    String.format("Desafio de nome '%s' já está em uso. Escolha um novo nome.", desafioRequestDTO.nome()));
        }

        Usuario user = pegaAtualUsuarioAutenticado();

        checaUsuarioCriador(user, desafio);

        final String nome = StringUtils.hasText(desafioRequestDTO.nome())
                ? desafioRequestDTO.nome() : desafio.getNome();
        final String descricao = StringUtils.hasText(desafioRequestDTO.descricao())
                ? desafioRequestDTO.descricao() : desafio.getDescricao();
        final String premiacao = StringUtils.hasText(desafioRequestDTO.premiacao())
                ? desafioRequestDTO.premiacao() : desafio.getPremiacao();
        final String imagem = StringUtils.hasText(desafioRequestDTO.imagem())
                ? desafioRequestDTO.imagem() : desafio.getImagem();
        final LocalDate data_inicio = desafioRequestDTO.data_inicio() != null
                ? desafioRequestDTO.data_inicio() : desafio.getStartAt();
        final LocalDate data_final = desafioRequestDTO.data_final() != null
                ? desafioRequestDTO.data_final() : desafio.getEndAt();

        final TipoDesafio tipoDesafio;
        if(desafioRequestDTO.tipo_desafio() != null && StringUtils.hasText(desafioRequestDTO.tipo_desafio().name()))
            tipoDesafio = desafioRequestDTO.tipo_desafio();
        else
            tipoDesafio = desafio.getTipo();

        Desafio desafioAtualizado = Desafio.builder()
                .id(desafio.getId())
                .nome(nome)
                .descricao(descricao)
                .premiacao(premiacao)
                .imagem(imagem)
                .tipo(tipoDesafio)
                .startAt(data_inicio)
                .endAt(data_final)
                .createdAt(desafio.getCreatedAt())
                .treinos(desafio.getTreinos())
                .participantes(desafio.getParticipantes())
                .desafioCriador(desafio.getDesafioCriador())
                .finalizado(desafio.isFinalizado())
                .build();

        desafioRepository.save(desafioAtualizado);
        return desafioDTOMapper.apply(desafioAtualizado);
    }

    // Usuario Professor pode deletar desafio
    public void usuarioDeletaDesafio(String desafioNome) {
        Optional.ofNullable(desafioRepository.findByNome(desafioNome)).ifPresentOrElse(d -> {
                    Usuario user = pegaAtualUsuarioAutenticado();

                    checaUsuarioCriador(user, d);

                    d.getTreinos().forEach(t -> t.getExercicios().forEach(e -> e.getTreinos().remove(t)));

                    desafioRepository.delete(d);
                },
                () -> {
                    throw new DesafioNotFoundException(String.format("Desafio de nome '%s' não foi encontrado.", desafioNome));
                });
    }

    // Registrar novos usuarios
    public UsuarioCriadoDTO registrarUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        if(Optional.ofNullable(usuarioRepository.findByEmail(usuarioRequestDTO.email())).isPresent()) {
            throw new IntegrityConstraintViolationException(
                    String.format(
                            "Email '%s' já esta em uso.", usuarioRequestDTO.email()
                    ));
        }

        Usuario user = usuarioDTOMapper.DtoToEntityFromRequest(usuarioRequestDTO);
        // Codificando senha
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        // Por padrão todos novos usuarios são Alunos
        user.setRole(UsuarioRole.ROLE_ALUNO);

        usuarioRepository.save(user);

        return UsuarioCriadoDTO.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .mensagem("Usuário criado com sucesso!")
                .build();
    }

    // Metodos utilitarios

    private static void checaUsuarioCriador(Usuario user, Desafio desafio) {
        if(!desafio.getDesafioCriador().equals(user)) {
            throw new UsuarioPermissaoInsuficienteException(
                    String.format("Usuário '%s' não possui permissão para realizar está ação.", user.getNome()));
        }
    }

    private static void checaDesafioFinalizado(Desafio desafio) {
        if(desafio.isFinalizado()) {
            throw new DesafioFinalizadoException(String.format("Desafio '%s' acabou.", desafio.getNome()));
        }
    }

    private Usuario pegaAtualUsuarioAutenticado() {
        return Optional.of(usuarioRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UsuarioNotFoundException("Error ao encontrar as informações do usuário."));
    }
}
