package com.joaoldantas.fitapp.controller;

import com.joaoldantas.fitapp.dto.response.DesafioDTO;
import com.joaoldantas.fitapp.service.DesafioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/desafios")
public class DesafioController {

    private final DesafioService desafioService;

    public DesafioController(DesafioService desafioService) {
        this.desafioService = desafioService;
    }

    @GetMapping("/disponiveis")
    public List<DesafioDTO> checaDesafiosDisponiveis() {
        return desafioService.checaDesafiosDisponiveis();
    }

    @GetMapping("/participando")
    public List<DesafioDTO> checaDesafiosParticipando() {
        return desafioService.checaDesafios();
    }

    @GetMapping("/criados")
    public List<DesafioDTO> checaDesafiosCriado() {
        return desafioService.checaDesafiosCriado();
    }

    @GetMapping("/{desafioNome}")
    public DesafioDTO checaDesafio(@PathVariable String desafioNome) {
        return desafioService.checaDesafio(desafioNome);
    }
}
