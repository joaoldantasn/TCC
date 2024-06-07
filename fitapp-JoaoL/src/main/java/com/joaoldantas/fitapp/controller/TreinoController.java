package com.joaoldantas.fitapp.controller;

import com.joaoldantas.fitapp.dto.response.TreinoDTO;
import com.joaoldantas.fitapp.service.TreinoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/treinos")
public class TreinoController {

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }

    @GetMapping("/desafio/{desafioNome}")
    public TreinoDTO.DesafioTreinoDTO checarTreinos(@PathVariable String desafioNome) {
        return treinoService.checarTreinos(desafioNome);
    }
}
