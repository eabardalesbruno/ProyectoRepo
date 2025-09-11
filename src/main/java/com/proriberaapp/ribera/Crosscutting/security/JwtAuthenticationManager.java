package com.proriberaapp.ribera.Crosscutting.security;

import com.proriberaapp.ribera.Api.controllers.exception.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private final JwtProvider jwtProvider;

    public JwtAuthenticationManager(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> jwtProvider.getClaimsFromToken(auth.getCredentials().toString()))
                .onErrorResume(e -> {
                    // Si es un token expirado, lanzar TokenExpiredException específica
                    if (e instanceof ExpiredJwtException) {
                        return Mono.error(new TokenExpiredException("Token expirado"));
                    }
                    // Para otros errores, mantener el comportamiento original
                    return Mono.error(new Throwable("bad token"));
                })
                .map(claims -> new UsernamePasswordAuthenticationToken(
                                claims.getSubject(),
                                null,
                                Stream.of(claims.get("roles"))
                                        .map(role -> (List<Map<String, String>>) role)
                                        .flatMap(role -> role.stream()
                                                .map(r -> r.get("authority"))
                                                .map(SimpleGrantedAuthority::new))
                                        .toList()
                        )
                );
    }

}