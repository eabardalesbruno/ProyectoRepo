package com.proriberaapp.ribera.Api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
public class HomeRedirectController {
    @GetMapping("/")
    public Mono<String> redirectToSwagger(ServerWebExchange exchange) {
        return Mono.just("redirect:/swagger-doc/swagger-ui.html");
    }
}
