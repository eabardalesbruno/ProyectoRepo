package com.proriberaapp.ribera.Crosscutting.security;

import com.proriberaapp.ribera.Api.controllers.admin.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
<<<<<<< HEAD
<<<<<<< HEAD
        if (path.contains("/api/v1/admin/login") || path.contains("/api/v1/users/login") || path.contains("/api/v1/users/registerbo") || path.contains("/api/v1/users/register") || path.contains("/api/v1/password-reset/request") || path.contains("/api/v1/password-reset/verify"))
=======
        if (path.contains("/api/v1/admin/login") || path.contains("/api/v1/users/login") || path.contains("/api/v1/users/registerbo") || path.contains("/api/v1/users/register") || path.contains("/api/v1/password-reset/request") || path.contains("/api/v1/password-reset/verify") || path.contains("/api/v1/password-reset/confirm"))
>>>>>>> jose-dev
=======
        if (path.contains("/api/v1/admin/login") || path.contains("/api/v1/users/login") || path.contains("/api/v1/users/registerbo") || path.contains("/api/v1/users/register") || path.contains("/api/v1/password-reset/request") || path.contains("/api/v1/password-reset/verify") || path.contains("/api/v1/password-reset/confirm") || path.contains("/api/v1/payment-books"))
>>>>>>> jose-dev
            return chain.filter(exchange);
        String auth = request.getHeaders().getFirst("Authorization");
        if (auth == null)
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Missing token"));
        if(!auth.startsWith("Bearer "))
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid token"));
        String token = auth.replace("Bearer ", "");
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);

    }
}
