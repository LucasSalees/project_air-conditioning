package com.system_air.project_airconditioning.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.system_air.project_airconditioning.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "class-ar-api";

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            
            // Verificação de segurança para evitar erro se a empresa for nula
            Long empresaId = (usuario.getEmpresa() != null) ? usuario.getEmpresa().getId() : null;

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withClaim("empresaId", empresaId) 
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        // Padrão de 2 horas de validade em UTC
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);
    }
    
    // Adicione este método ao seu TokenService.java
    public Long getClaim(String tokenJWT, String claimName) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("class-ar-api")
                    .build()
                    .verify(tokenJWT)
                    .getClaim(claimName).asLong();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}