package com.joaoldantas.fitapp.controller;

import com.joaoldantas.fitapp.dto.request.LoginRequestDTO;
import com.joaoldantas.fitapp.dto.response.TokenDTO;
import com.joaoldantas.fitapp.security.jwt.JwtUsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final JwtUsuarioService jwtUsuarioService;

    public LoginController(JwtUsuarioService jwtUsuarioService) {
        this.jwtUsuarioService = jwtUsuarioService;
    }

    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return jwtUsuarioService.generateToken(loginRequestDTO);
    }
}
