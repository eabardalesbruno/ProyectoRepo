package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.payme.AuthorizationRepository;
import com.proriberaapp.ribera.Api.controllers.payme.dto.*;
import com.proriberaapp.ribera.Api.controllers.payme.entity.AuthorizationEntity;
import com.proriberaapp.ribera.Api.controllers.payme.entity.PayMeAuthorization;
import com.proriberaapp.ribera.Api.controllers.payme.entity.TokenizeEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    /* ENVIA CORREO
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
     */
    /*1907
    private final PayMeRepository paymeRepository;
    private final AuthorizationRepository authorizationRepository;
    private final UserClientRepository userClientRepository;
    private final PaymentBookRepository paymentBookRepository;
    private final BookingRepository bookingRepository;
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
                                .flatMap(userClient ->
                                        bookingRepository.findByBookingId(savedAuthorization.getIdBooking())
                                                .flatMap(booking -> {
                                                    if (booking.getBookingStateId() == 3) {
                                                        booking.setBookingStateId(2);
                                                        return bookingRepository.save(booking);
                                                    } else {
                                                        return Mono.just(booking);
                                                    }
                                                })
                                                .flatMap(updatedBooking -> sendSuccessEmail(userClient.getEmail()))
                                                .thenReturn(new TransactionNecessaryResponse(true))
                                )
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
     */
    private final PayMeRepository paymeRepository;
    private final AuthorizationRepository authorizationRepository;
    private final UserClientRepository userClientRepository;
    private final PaymentBookRepository paymentBookRepository;
    private final BookingRepository bookingRepository;
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
                                .flatMap(userClient ->
                                        bookingRepository.findByBookingId(savedAuthorization.getIdBooking())
                                                .flatMap(booking -> {
                                                    if (booking.getBookingStateId() == 3) {
                                                        booking.setBookingStateId(2);
                                                        return bookingRepository.save(booking);
                                                    } else {
                                                        return Mono.just(booking);
                                                    }
                                                })
                                                .flatMap(updatedBooking -> {
                                                    BigDecimal monto = new BigDecimal(authorizationResponse.getTransaction().getAmount());

                                                    PaymentBookEntity paymentBook = PaymentBookEntity.builder()
                                                            .bookingId(updatedBooking.getBookingId())
                                                            .userClientId(updatedBooking.getUserClientId())
                                                            .refuseReasonId(1)
                                                            .cancelReasonId(1)
                                                            .paymentMethodId(5)
                                                            .paymentStateId(1)
                                                            .paymentTypeId(6)
                                                            .paymentSubTypeId(1)
                                                            .currencyTypeId(1)
                                                            .amount(monto)
                                                            .description("Pago exitoso")
                                                            .paymentDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima"))))
                                                            .operationCode(authorizationResponse.getId())
                                                            .note("Nota de pago")
                                                            .totalCost(monto)
                                                            .imageVoucher("Pago con Tarjeta")
                                                            .totalPoints(0)
                                                            .paymentComplete(true)
                                                            .pendingpay(0)
                                                            .build();
                                                    return paymentBookRepository.save(paymentBook)
                                                            .then(sendSuccessEmail(userClient.getEmail()));
                                                })
                                                .thenReturn(new TransactionNecessaryResponse(true))
                                )
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
        String body = "<html><head><title>Pago Exitoso</title></head><body style='color:black'>";
        body += "<div style='width: 100%;'>";
        body += "<div style='display: flex; align-items: center; justify-content: space-between;'>";
        body += "<img style='width: 100px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453907774_2238863976459404_4409148998166454890_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=1p66qBQN6IYQ7kNvgEnxiv2&_nc_ht=scontent.flim1-2.fna&oh=00_AYACRHyTnMSMkClEmGFw8OmSBT2T_U4LGusY0F3KX0OBVQ&oe=66B1E966' alt='Logo Izquierda'>";
        body += "<div style='display: flex;'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453503393_2238863839792751_3678586622785113323_n.jpg?stp=cp0_dst-jpg&_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OMKWsE877hcQ7kNvgHnzNGq&_nc_ht=scontent.flim1-2.fna&oh=00_AYBSmgM6SVV33fWdVeqn9sUMleFSdtOGZPcc0m-USS93bg&oe=66B20925' alt='Logo 1'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453501437_2238863739792761_5553627034492335729_n.jpg?stp=cp0_dst-jpg&_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=fcEltLDDNeMQ7kNvgFNAsL6&_nc_ht=scontent.flim1-2.fna&oh=00_AYBD75zTjdsLuKmtk3vPYR7fBfCg5U2aVQ_tYm8679ZFCQ&oe=66B1FF76' alt='Logo 2'>";
        body += "<img style='width: 80px;' src='https://scontent.flim1-1.fna.fbcdn.net/v/t39.30808-6/453497633_2238863526459449_291281439279005519_n.jpg?stp=cp0_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=vMzblHxFzGUQ7kNvgHhI3YO&_nc_ht=scontent.flim1-1.fna&oh=00_AYAEn_ThdeZSWqvo7RurNrnoAulbgxM7V5YzJc_CGsYACg&oe=66B1E905' alt='Logo 3'>";
        body += "</div>";
        body += "</div>";
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
        String body = "<html><head><title>Error en el Pago</title></head><body style='color:black'>";
        body += "<div style='width: 100%;'>";
        body += "<div style='display: flex; align-items: center; justify-content: space-between;'>";
        body += "<img style='width: 100px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453907774_2238863976459404_4409148998166454890_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=1p66qBQN6IYQ7kNvgEnxiv2&_nc_ht=scontent.flim1-2.fna&oh=00_AYACRHyTnMSMkClEmGFw8OmSBT2T_U4LGusY0F3KX0OBVQ&oe=66B1E966' alt='Logo Izquierda'>";
        body += "<div style='display: flex;'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453503393_2238863839792751_3678586622785113323_n.jpg?stp=cp0_dst-jpg&_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OMKWsE877hcQ7kNvgHnzNGq&_nc_ht=scontent.flim1-2.fna&oh=00_AYBSmgM6SVV33fWdVeqn9sUMleFSdtOGZPcc0m-USS93bg&oe=66B20925' alt='Logo 1'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453501437_2238863739792761_5553627034492335729_n.jpg?stp=cp0_dst-jpg&_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=fcEltLDDNeMQ7kNvgFNAsL6&_nc_ht=scontent.flim1-2.fna&oh=00_AYBD75zTjdsLuKmtk3vPYR7fBfCg5U2aVQ_tYm8679ZFCQ&oe=66B1FF76' alt='Logo 2'>";
        body += "<img style='width: 80px;' src='https://scontent.flim1-1.fna.fbcdn.net/v/t39.30808-6/453497633_2238863526459449_291281439279005519_n.jpg?stp=cp0_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=vMzblHxFzGUQ7kNvgHhI3YO&_nc_ht=scontent.flim1-1.fna&oh=00_AYAEn_ThdeZSWqvo7RurNrnoAulbgxM7V5YzJc_CGsYACg&oe=66B1E905' alt='Logo 3'>";
        body += "</div>";
        body += "</div>";
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



    public Mono<TransactionNecessaryResponse> savePayment2(Integer idUser, AuthorizationResponse authorizationResponse) {
        PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser, Role.ROLE_USER, authorizationResponse);

        return authorizationRepository.save(payMeAuthorization)
                .flatMap(savedAuthorization ->
                        userClientRepository.findById(idUser)
                                .flatMap(userClient ->
                                        bookingRepository.findByBookingId(savedAuthorization.getIdBooking())
                                                .flatMap(booking -> {
                                                    if (booking.getBookingStateId() == 3) {
                                                        booking.setBookingStateId(2);
                                                        return bookingRepository.save(booking);
                                                    } else {
                                                        return Mono.just(booking);
                                                    }
                                                })
                                                .flatMap(updatedBooking -> sendSuccessEmail(userClient.getEmail()))
                                                .thenReturn(new TransactionNecessaryResponse(true))
                                )
                )
                .onErrorResume(e ->
                        userClientRepository.findById(idUser)
                                .flatMap(userClient -> sendErrorEmail(userClient.getEmail(), e.getMessage()))
                                .then(Mono.error(e))
                );
    }
}