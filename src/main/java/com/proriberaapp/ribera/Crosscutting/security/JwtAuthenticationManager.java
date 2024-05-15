package com.proriberaapp.ribera.Crosscutting.security;

import com.proriberaapp.ribera.Api.controllers.admin.exception.CustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.SlideShow;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> jwtProvider.getClaimsFromToken(auth.getCredentials().toString())
                )
                .onErrorResume(e -> Mono.error(new Throwable("bad token")))
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