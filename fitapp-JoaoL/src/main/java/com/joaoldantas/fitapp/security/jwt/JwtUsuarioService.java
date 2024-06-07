package com.joaoldantas.fitapp.security.jwt;

import com.joaoldantas.fitapp.dto.request.LoginRequestDTO;
import com.joaoldantas.fitapp.dto.response.TokenDTO;
import com.joaoldantas.fitapp.entity.Usuario;
import com.joaoldantas.fitapp.exception.SenhaInvalidaException;
import com.joaoldantas.fitapp.exception.UsuarioNotFoundException;
import com.joaoldantas.fitapp.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class JwtUsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtUsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = Optional.ofNullable(usuarioRepository.findByEmail(email))
                .orElseThrow(() -> new UsuarioNotFoundException(String.format("Email %s não encontrado.", email)));

        Collection<SimpleGrantedAuthority> authority = Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(), authority);
    }

    public TokenDTO generateToken(LoginRequestDTO loginRequestDTO) {
        verifyUserCredentials(loginRequestDTO);
        return jwtService.generateToken(loginRequestDTO.email());
    }

    private void verifyUserCredentials(LoginRequestDTO loginRequestDTO) {
        UserDetails user = loadUserByUsername(loginRequestDTO.email());

        boolean isSenhaCorreta = passwordEncoder.matches(loginRequestDTO.senha(), user.getPassword());

        if(!isSenhaCorreta) {
            throw new SenhaInvalidaException("Senha incorreta!");
        }
    }
}
