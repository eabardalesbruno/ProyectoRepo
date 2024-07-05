package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.payme.dto.*;
import com.proriberaapp.ribera.Infraestructure.repository.CountryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.DocumentTypeRepository;
import com.proriberaapp.ribera.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymeService {
    private final PaymeRepository paymeRepository;

    @Value("${pay_me.client_id}")
    private String CLIENT_ID;
    @Value("${pay_me.client_secret}")
    private String CLIENT_SECRET;

    @Value("${pay_me.url.token}")
    private String URL_TOKEN;
    @Value("${pay_me.url.tokenize}")
    private String URL_TOKENIZE;
    @Value("${pay_me.url.charges}")
    private String URL_CHARGES;
    @Value("${pay_me.url.nonce}")
    private String URL_NONCE;

    @Value("${pay_me.username}")
    private String USERNAME;
    @Value("${pay_me.password}")
    private String PASSWORD;
    @Value("${pay_me.audience}")
    private String AUDIENCE;

    private Mono<AuthorizeResponse> getToken() {
        WebClient webClient = WebClient.create();
        AuthorizeRequest body = new AuthorizeRequest(
                "authorize", "password",
                USERNAME, PASSWORD,
                AUDIENCE,
                CLIENT_ID, CLIENT_SECRET,
                "create:token post:charges offline_access"
        );
        return webClient.post()
                .uri(URL_TOKEN)
                .header("Content-Type", "application/json")
                .header("ALG-API-VERSION", "1618440906")
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(AuthorizeResponse.class)
                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST, e.getMessage()) {
                }));
    }

    public Mono<NonceResponse> getNonce() {
        NonceRequest nonceRequest = new NonceRequest("create.nonce", USERNAME, "https://api.dev.alignet.io", CLIENT_ID, "create:token post:charges");
        WebClient webClient = WebClient.create();
        return getToken()
                .flatMap(authorizeResponse ->
                        webClient.post()
                                .uri(URL_NONCE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("ALG-API-VERSION", "1618440906")
                                .header("Authorization", "Bearer " + authorizeResponse.getAccessToken())
                                .body(BodyInserters.fromValue(nonceRequest))
                                .retrieve()
                                .bodyToMono(NonceResponse.class)
                                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST, e.getMessage()) {
                                }))
                );
    }

    public Flux<PaymentEntity> getPayments(Integer idUser) {
        return paymeRepository.findByIdUser(idUser);
    }
}