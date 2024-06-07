package com.joaoldantas.fitapp.security;

import com.joaoldantas.fitapp.security.jwt.JwtAuthFilter;
import com.joaoldantas.fitapp.util.UsuarioRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(config ->
                config
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/desafios/criar")
                            .hasRole(UsuarioRole.ROLE_PROFESSOR.name().substring(5))
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/desafios/deleta/{desafioNome}")
                            .hasRole(UsuarioRole.ROLE_PROFESSOR.name().substring(5))
                        .requestMatchers(HttpMethod.PATCH, "/api/usuarios/desafios/atualizar/{desafioNome}")
                            .hasRole(UsuarioRole.ROLE_PROFESSOR.name().substring(5))
                        .requestMatchers(HttpMethod.GET, "/api/desafios/criados")
                        .hasRole(UsuarioRole.ROLE_PROFESSOR.name().substring(5))
                        .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }
}
