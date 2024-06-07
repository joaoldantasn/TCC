package com.joaoldantas.fitapp.security.jwt;

import com.joaoldantas.fitapp.dto.response.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final int JWT_EXPIRATION = 10;

    public TokenDTO generateToken(String email) {
        Calendar dataAtual = Calendar.getInstance();
        dataAtual.add(Calendar.MINUTE, JWT_EXPIRATION);
        Date dataExpiracao = dataAtual.getTime();

        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(dataExpiracao)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();

        long dataExpiracaoSegundos = TimeUnit.MILLISECONDS.toSeconds(dataExpiracao.getTime() - new Date().getTime());

        return TokenDTO.builder()
                .token(token)
                .expiraEm(dataExpiracaoSegundos)
                .usuario(TokenDTO.UserDetailDTO.builder()
                        .email(email)
                        .build())
                .build();
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
        return true;
    }
}
