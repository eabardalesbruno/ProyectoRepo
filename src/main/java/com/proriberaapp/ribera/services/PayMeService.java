package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.payme.AuthorizationRepository;
import com.proriberaapp.ribera.Api.controllers.payme.dto.*;
import com.proriberaapp.ribera.Api.controllers.payme.entity.AuthorizationEntity;
import com.proriberaapp.ribera.Api.controllers.payme.entity.PayMeAuthorization;
import com.proriberaapp.ribera.Api.controllers.payme.entity.TokenizeEntity;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.EmailService;
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
public class PayMeService {
    /* BRUNO
    private final PayMeRepository paymeRepository;
    private final AuthorizationRepository authorizationRepository;

    @Value("${pay_me.client_id}")
    private String CLIENT_ID;
    @Value("${pay_me.client_secret}")
    private String CLIENT_SECRET;

    @Value("${pay_me.url.access_token}")
    private String URL_ACCESS_TOKEN;
    @Value("${pay_me.url.nonce}")
    private String URL_NONCE;

    @Value("${pay_me.url.audience}")
    private String AUDIENCE;
    @Value("${pay_me.url.ALG-API-VERSION}")
    private String ALG_API_VERSION;

    private Mono<AuthorizeResponse> getToken() {
        WebClient webClient = WebClient.create();
        AuthorizeRequest body = new AuthorizeRequest(
                "authorize", "password",
                AUDIENCE,
                CLIENT_ID, CLIENT_SECRET,
                "create:token post:charges offline_access"
        );
        return webClient.post()
                .uri(URL_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .header("ALG-API-VERSION", ALG_API_VERSION)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(AuthorizeResponse.class)
                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting token") {
                }));
    }

    public Mono<NonceResponse> getNonce() {
        NonceRequest nonceRequest = new NonceRequest(
                "create.nonce",
                AUDIENCE,
                CLIENT_ID,
                "create:token post:charges");
        WebClient webClient = WebClient.create();
        return getToken()
                .flatMap(authorizeResponse ->
                        webClient.post()
                                .uri(URL_NONCE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("ALG-API-VERSION", ALG_API_VERSION)
                                .header("Authorization", authorizeResponse.getTokenType() + " " + authorizeResponse.getAccessToken())
                                .body(BodyInserters.fromValue(nonceRequest))
                                .retrieve()
                                .bodyToMono(NonceResponse.class)
                                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting nonce") {
                                }))
                );
    }

    public Flux<TokenizeEntity> getPayments(Integer idUser) {
        return paymeRepository.findByIdUser(idUser);
    }

    public Mono<TransactionNecessaryResponse> savePayment(Integer idUser, AuthorizationResponse authorizationResponse) {
        PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser, Role.ROLE_USER, authorizationResponse);

        return authorizationRepository.save(payMeAuthorization)
                .map(paymentEntity1 -> new TransactionNecessaryResponse(true));
    }
     */
    private final PayMeRepository paymeRepository;
    private final AuthorizationRepository authorizationRepository;
    private final UserClientRepository userClientRepository;
    private final EmailService emailService;

    @Value("${pay_me.client_id}")
    private String CLIENT_ID;
    @Value("${pay_me.client_secret}")
    private String CLIENT_SECRET;

    @Value("${pay_me.url.access_token}")
    private String URL_ACCESS_TOKEN;
    @Value("${pay_me.url.nonce}")
    private String URL_NONCE;

    @Value("${pay_me.url.audience}")
    private String AUDIENCE;
    @Value("${pay_me.url.ALG-API-VERSION}")
    private String ALG_API_VERSION;

    private Mono<AuthorizeResponse> getToken() {
        WebClient webClient = WebClient.create();
        AuthorizeRequest body = new AuthorizeRequest(
                "authorize", "password",
                AUDIENCE,
                CLIENT_ID, CLIENT_SECRET,
                "create:token post:charges offline_access"
        );
        return webClient.post()
                .uri(URL_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .header("ALG-API-VERSION", ALG_API_VERSION)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(AuthorizeResponse.class)
                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting token") {
                }));
    }

    public Mono<NonceResponse> getNonce() {
        NonceRequest nonceRequest = new NonceRequest(
                "create.nonce",
                AUDIENCE,
                CLIENT_ID,
                "create:token post:charges");
        WebClient webClient = WebClient.create();
        return getToken()
                .flatMap(authorizeResponse ->
                        webClient.post()
                                .uri(URL_NONCE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("ALG-API-VERSION", ALG_API_VERSION)
                                .header("Authorization", authorizeResponse.getTokenType() + " " + authorizeResponse.getAccessToken())
                                .body(BodyInserters.fromValue(nonceRequest))
                                .retrieve()
                                .bodyToMono(NonceResponse.class)
                                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting nonce") {
                                }))
                );
    }

    public Flux<TokenizeEntity> getPayments(Integer idUser) {
        return paymeRepository.findByIdUser(idUser);
    }

    public Mono<TransactionNecessaryResponse> savePayment(Integer idUser, AuthorizationResponse authorizationResponse) {
        PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser, Role.ROLE_USER, authorizationResponse);

        return authorizationRepository.save(payMeAuthorization)
                .flatMap(savedAuthorization ->
                        userClientRepository.findById(idUser)
                                .flatMap(userClient -> sendSuccessEmail(userClient.getEmail()))
                                .thenReturn(new TransactionNecessaryResponse(true))
                )
                .onErrorResume(e ->
                        userClientRepository.findById(idUser)
                                .flatMap(userClient -> sendErrorEmail(userClient.getEmail(), e.getMessage()))
                                .then(Mono.error(e))
                );
    }

    private Mono<Void> sendSuccessEmail(String email) {
        String emailBody = generateSuccessEmailBody();
        return emailService.sendEmail(email, "Pago Exitoso", emailBody);
    }

    private String generateSuccessEmailBody() {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<h1 style='text-align: center;'>Pago Exitoso</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido procesado con éxito.</p>";
        body += "<p>Gracias por su confianza.</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }

    private Mono<Void> sendErrorEmail(String email, String errorMessage) {
        String emailBody = generateErrorEmailBody(errorMessage);
        return emailService.sendEmail(email, "Error en el Pago", emailBody);
    }

    private String generateErrorEmailBody(String errorMessage) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<h1 style='text-align: center;'>Error en el Pago</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Ha ocurrido un error durante el procesamiento de su pago.</p>";
        body += "<p>Detalles del error:</p>";
        body += "<p>" + errorMessage + "</p>";
        body += "<p>Por favor, póngase en contacto con soporte.</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }
}