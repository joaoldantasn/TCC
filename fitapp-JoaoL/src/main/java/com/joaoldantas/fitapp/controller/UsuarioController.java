package com.joaoldantas.fitapp.controller;

import com.joaoldantas.fitapp.dto.request.DesafioRequestDTO;
import com.joaoldantas.fitapp.dto.request.TreinoRequestDTO;
import com.joaoldantas.fitapp.dto.request.UsuarioRequestDTO;
import com.joaoldantas.fitapp.dto.response.DesafioDTO;
import com.joaoldantas.fitapp.dto.response.UsuarioDTO;
import com.joaoldantas.fitapp.dto.response.util.UsuarioCriadoDTO;
import com.joaoldantas.fitapp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/info")
    public UsuarioDTO findUsuario() {
        return usuarioService.usuarioChecaInfo();
    }

    @PatchMapping
    public Optional<UsuarioDTO> usuarioAtualizaInfo(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return usuarioService.usuarioAlteraInfo(usuarioRequestDTO);
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioCriadoDTO> registrar(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return new ResponseEntity<>(usuarioService.registrarUsuario(usuarioRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/desafios/criar")
    public DesafioDTO usuarioCriaDesafio(@Valid @RequestBody DesafioRequestDTO desafioRequestDTO) {
        return usuarioService.usuarioCriaDesafio(desafioRequestDTO);
    }

    @DeleteMapping("/desafios/deleta/{desafioNome}")
    public ResponseEntity<Void> usuarioDeletaDesafio(@PathVariable String desafioNome) {
        usuarioService.usuarioDeletaDesafio(desafioNome);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/desafios/entrar/{desafioNome}")
    public DesafioDTO usuarioEntraDesafio(@PathVariable String desafioNome) {
        return usuarioService.UsuarioEntraDesafio(desafioNome);
    }

    @PutMapping("/desafios/sair/{desafioNome}")
    public DesafioDTO usuarioSairDesafio(@PathVariable String desafioNome) {
        return usuarioService.usuarioSairDesafio(desafioNome);
    }

    @PatchMapping("/desafios/atualizar/{desafioNome}")
    public DesafioDTO usuarioSairDesafio(@PathVariable String desafioNome, @RequestBody DesafioRequestDTO desafioRequestDTO) {
        return usuarioService.usuarioAtualizaDesafio(desafioNome, desafioRequestDTO);
    }

    @PutMapping("/desafios/{desafioNome}/postar-treino")
    public DesafioDTO usuarioPostaTreino(@PathVariable String desafioNome, @Valid @RequestBody TreinoRequestDTO treinoRequestDTO) {
        return usuarioService.usuarioPostaTreino(desafioNome, treinoRequestDTO);
    }
}
