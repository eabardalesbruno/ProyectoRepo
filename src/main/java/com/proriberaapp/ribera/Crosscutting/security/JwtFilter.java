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
        if (
                path.contains("/api/v1/admin/login")
                        || path.contains("/api/v1/users/login")
                        || path.contains("/api/v1/users/registerbo")
                        || path.contains("/api/v1/users/register")
                        || path.contains("/api/countries")
                        || path.contains("/api/documenttype")
                        || path.contains("/api/termsversions")
                        || path.contains("/api/userclientversions")
                        || path.contains("/api/v1/password-reset/request")
                        || path.contains("/api/v1/password-reset/verify")
                        || path.contains("/api/v1/password-reset/confirm")
                        || path.contains("/api/v1/payment-books")
                        || path.contains("/api/v1/**")
                        || path.contains("/swagger-doc/**")
                        || path.contains("/swagger-doc/webjars/**")
                        || path.contains("/swagger-doc/v3/api-docs")
                        || path.contains("/swagger-doc/webjars/swagger-ui/**")
                        || path.contains("/swagger-doc/webjars/swagger-ui/index.html")
                        || path.contains("/swagger-doc/webjars/swagger-ui/swagger-ui-bundle.js")
                        || path.contains("/swagger-doc/webjars/swagger-ui/swagger-ui.css")
                        || path.contains("/swagger-doc/webjars/swagger-ui/swagger-ui-standalone-preset.js")
                        || path.contains("/swagger-doc/webjars/swagger-ui/swagger-ui.js")
                        || path.contains("/swagger-doc/webjars/swagger-ui/index.css")
                        || path.contains("/swagger-doc/webjars/swagger-ui/index.js")
                        || path.contains("/swagger-doc/webjars/swagger-ui/swagger-initializer.js")
                        || path.contains("/swagger-doc/webjars/swagger-ui/favicon-32x32.png")

                        || path.contains("/swagger-ui.html")
                        || path.contains("/api-docs")
                        || path.contains("/favicon.ico")
        )
            return chain.filter(exchange);
        String auth = request.getHeaders().getFirst("Authorization");
        if (auth == null)
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Missing token"));
        if (!auth.startsWith("Bearer "))
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid token"));
        String token = auth.replace("Bearer ", "");
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);

    }
}
