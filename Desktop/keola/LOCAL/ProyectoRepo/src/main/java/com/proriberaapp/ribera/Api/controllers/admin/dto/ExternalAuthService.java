package com.proriberaapp.ribera.Api.controllers.admin.dto;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalAuthService {
    private final WebClient webClient;

    public ExternalAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://opengateway.inclub.world/api/v1/auth").build();
    }

    public Mono<String> getExternalToken() {
        return webClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "username":"master",
                            "password":"#Inresorts321#"
                        }
                        """)
                .retrieve()
                .bodyToMono(ExternalAuthResponse.class)
                .map(response -> response.data().accessToken());
    }
}
