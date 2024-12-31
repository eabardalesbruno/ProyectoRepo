package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.payme.AuthorizationRepository;
import com.proriberaapp.ribera.Api.controllers.payme.dto.*;
import com.proriberaapp.ribera.Api.controllers.payme.entity.PayMeAuthorization;
import com.proriberaapp.ribera.Api.controllers.payme.entity.TokenizeEntity;
import com.proriberaapp.ribera.Domain.dto.BookingAndRoomNameDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.BookingEmailDto;
import com.proriberaapp.ribera.utils.emails.ConfirmPaymentByBankTransferAndCardTemplateEmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayMeService {
        /*
         * BRUNO
         * private final PayMeRepository paymeRepository;
         * private final AuthorizationRepository authorizationRepository;
         * 
         * @Value("${pay_me.client_id}")
         * private String CLIENT_ID;
         * 
         * @Value("${pay_me.client_secret}")
         * private String CLIENT_SECRET;
         * 
         * @Value("${pay_me.url.access_token}")
         * private String URL_ACCESS_TOKEN;
         * 
         * @Value("${pay_me.url.nonce}")
         * private String URL_NONCE;
         * 
         * @Value("${pay_me.url.audience}")
         * private String AUDIENCE;
         * 
         * @Value("${pay_me.url.ALG-API-VERSION}")
         * private String ALG_API_VERSION;
         * 
         * private Mono<AuthorizeResponse> getToken() {
         * WebClient webClient = WebClient.create();
         * AuthorizeRequest body = new AuthorizeRequest(
         * "authorize", "password",
         * AUDIENCE,
         * CLIENT_ID, CLIENT_SECRET,
         * "create:token post:charges offline_access"
         * );
         * return webClient.post()
         * .uri(URL_ACCESS_TOKEN)
         * .contentType(MediaType.APPLICATION_JSON)
         * .header("ALG-API-VERSION", ALG_API_VERSION)
         * .body(BodyInserters.fromValue(body))
         * .retrieve()
         * .bodyToMono(AuthorizeResponse.class)
         * .onErrorResume(e -> Mono.error(new
         * HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting token") {
         * }));
         * }
         * 
         * public Mono<NonceResponse> getNonce() {
         * NonceRequest nonceRequest = new NonceRequest(
         * "create.nonce",
         * AUDIENCE,
         * CLIENT_ID,
         * "create:token post:charges");
         * WebClient webClient = WebClient.create();
         * return getToken()
         * .flatMap(authorizeResponse ->
         * webClient.post()
         * .uri(URL_NONCE)
         * .contentType(MediaType.APPLICATION_JSON)
         * .header("ALG-API-VERSION", ALG_API_VERSION)
         * .header("Authorization", authorizeResponse.getTokenType() + " " +
         * authorizeResponse.getAccessToken())
         * .body(BodyInserters.fromValue(nonceRequest))
         * .retrieve()
         * .bodyToMono(NonceResponse.class)
         * .onErrorResume(e -> Mono.error(new
         * HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting nonce") {
         * }))
         * );
         * }
         * 
         * public Flux<TokenizeEntity> getPayments(Integer idUser) {
         * return paymeRepository.findByIdUser(idUser);
         * }
         * 
         * public Mono<TransactionNecessaryResponse> savePayment(Integer idUser,
         * AuthorizationResponse authorizationResponse) {
         * PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser,
         * Role.ROLE_USER, authorizationResponse);
         * 
         * return authorizationRepository.save(payMeAuthorization)
         * .map(paymentEntity1 -> new TransactionNecessaryResponse(true));
         * }
         */
        /*
         * ENVIA CORREO
         * private final PayMeRepository paymeRepository;
         * private final AuthorizationRepository authorizationRepository;
         * private final UserClientRepository userClientRepository;
         * private final EmailService emailService;
         * 
         * @Value("${pay_me.client_id}")
         * private String CLIENT_ID;
         * 
         * @Value("${pay_me.client_secret}")
         * private String CLIENT_SECRET;
         * 
         * @Value("${pay_me.url.access_token}")
         * private String URL_ACCESS_TOKEN;
         * 
         * @Value("${pay_me.url.nonce}")
         * private String URL_NONCE;
         * 
         * @Value("${pay_me.url.audience}")
         * private String AUDIENCE;
         * 
         * @Value("${pay_me.url.ALG-API-VERSION}")
         * private String ALG_API_VERSION;
         * 
         * private Mono<AuthorizeResponse> getToken() {
         * WebClient webClient = WebClient.create();
         * AuthorizeRequest body = new AuthorizeRequest(
         * "authorize", "password",
         * AUDIENCE,
         * CLIENT_ID, CLIENT_SECRET,
         * "create:token post:charges offline_access"
         * );
         * return webClient.post()
         * .uri(URL_ACCESS_TOKEN)
         * .contentType(MediaType.APPLICATION_JSON)
         * .header("ALG-API-VERSION", ALG_API_VERSION)
         * .body(BodyInserters.fromValue(body))
         * .retrieve()
         * .bodyToMono(AuthorizeResponse.class)
         * .onErrorResume(e -> Mono.error(new
         * HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting token") {
         * }));
         * }
         * 
         * public Mono<NonceResponse> getNonce() {
         * NonceRequest nonceRequest = new NonceRequest(
         * "create.nonce",
         * AUDIENCE,
         * CLIENT_ID,
         * "create:token post:charges");
         * WebClient webClient = WebClient.create();
         * return getToken()
         * .flatMap(authorizeResponse ->
         * webClient.post()
         * .uri(URL_NONCE)
         * .contentType(MediaType.APPLICATION_JSON)
         * .header("ALG-API-VERSION", ALG_API_VERSION)
         * .header("Authorization", authorizeResponse.getTokenType() + " " +
         * authorizeResponse.getAccessToken())
         * .body(BodyInserters.fromValue(nonceRequest))
         * .retrieve()
         * .bodyToMono(NonceResponse.class)
         * .onErrorResume(e -> Mono.error(new
         * HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting nonce") {
         * }))
         * );
         * }
         * 
         * public Flux<TokenizeEntity> getPayments(Integer idUser) {
         * return paymeRepository.findByIdUser(idUser);
         * }
         * 
         * public Mono<TransactionNecessaryResponse> savePayment(Integer idUser,
         * AuthorizationResponse authorizationResponse) {
         * PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser,
         * Role.ROLE_USER, authorizationResponse);
         * 
         * return authorizationRepository.save(payMeAuthorization)
         * .flatMap(savedAuthorization ->
         * userClientRepository.findById(idUser)
         * .flatMap(userClient -> sendSuccessEmail(userClient.getEmail()))
         * .thenReturn(new TransactionNecessaryResponse(true))
         * )
         * .onErrorResume(e ->
         * userClientRepository.findById(idUser)
         * .flatMap(userClient -> sendErrorEmail(userClient.getEmail(), e.getMessage()))
         * .then(Mono.error(e))
         * );
         * }
         * 
         * private Mono<Void> sendSuccessEmail(String email) {
         * String emailBody = generateSuccessEmailBody();
         * return emailService.sendEmail(email, "Pago Exitoso", emailBody);
         * }
         * 
         * private String generateSuccessEmailBody() {
         * String body = "<html><head><title></title></head><body style='color:black'>";
         * body += "<div style='width: 100%'>";
         * body += "<h1 style='text-align: center;'>Pago Exitoso</h1>";
         * body += "<p>Estimado cliente,</p>";
         * body += "<p>Su pago ha sido procesado con éxito.</p>";
         * body += "<p>Gracias por su confianza.</p>";
         * body += "</div>";
         * body += "</body></html>";
         * return body;
         * }
         * 
         * private Mono<Void> sendErrorEmail(String email, String errorMessage) {
         * String emailBody = generateErrorEmailBody(errorMessage);
         * return emailService.sendEmail(email, "Error en el Pago", emailBody);
         * }
         * 
         * private String generateErrorEmailBody(String errorMessage) {
         * String body = "<html><head><title></title></head><body style='color:black'>";
         * body += "<div style='width: 100%'>";
         * body += "<h1 style='text-align: center;'>Error en el Pago</h1>";
         * body += "<p>Estimado cliente,</p>";
         * body += "<p>Ha ocurrido un error durante el procesamiento de su pago.</p>";
         * body += "<p>Detalles del error:</p>";
         * body += "<p>" + errorMessage + "</p>";
         * body += "<p>Por favor, póngase en contacto con soporte.</p>";
         * body += "</div>";
         * body += "</body></html>";
         * return body;
         * }
         */
        /*
         * 1907
         * private final PayMeRepository paymeRepository;
         * private final AuthorizationRepository authorizationRepository;
         * private final UserClientRepository userClientRepository;
         * private final PaymentBookRepository paymentBookRepository;
         * private final BookingRepository bookingRepository;
         * private final EmailService emailService;
         * 
         * @Value("${pay_me.client_id}")
         * private String CLIENT_ID;
         * 
         * @Value("${pay_me.client_secret}")
         * private String CLIENT_SECRET;
         * 
         * @Value("${pay_me.url.access_token}")
         * private String URL_ACCESS_TOKEN;
         * 
         * @Value("${pay_me.url.nonce}")
         * private String URL_NONCE;
         * 
         * @Value("${pay_me.url.audience}")
         * private String AUDIENCE;
         * 
         * @Value("${pay_me.url.ALG-API-VERSION}")
         * private String ALG_API_VERSION;
         * 
         * private Mono<AuthorizeResponse> getToken() {
         * WebClient webClient = WebClient.create();
         * AuthorizeRequest body = new AuthorizeRequest(
         * "authorize", "password",
         * AUDIENCE,
         * CLIENT_ID, CLIENT_SECRET,
         * "create:token post:charges offline_access"
         * );
         * return webClient.post()
         * .uri(URL_ACCESS_TOKEN)
         * .contentType(MediaType.APPLICATION_JSON)
         * .header("ALG-API-VERSION", ALG_API_VERSION)
         * .body(BodyInserters.fromValue(body))
         * .retrieve()
         * .bodyToMono(AuthorizeResponse.class)
         * .onErrorResume(e -> Mono.error(new
         * HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting token") {
         * }));
         * }
         * 
         * public Mono<NonceResponse> getNonce() {
         * NonceRequest nonceRequest = new NonceRequest(
         * "create.nonce",
         * AUDIENCE,
         * CLIENT_ID,
         * "create:token post:charges");
         * WebClient webClient = WebClient.create();
         * return getToken()
         * .flatMap(authorizeResponse ->
         * webClient.post()
         * .uri(URL_NONCE)
         * .contentType(MediaType.APPLICATION_JSON)
         * .header("ALG-API-VERSION", ALG_API_VERSION)
         * .header("Authorization", authorizeResponse.getTokenType() + " " +
         * authorizeResponse.getAccessToken())
         * .body(BodyInserters.fromValue(nonceRequest))
         * .retrieve()
         * .bodyToMono(NonceResponse.class)
         * .onErrorResume(e -> Mono.error(new
         * HttpStatusCodeException(HttpStatus.BAD_REQUEST, "Error getting nonce") {
         * }))
         * );
         * }
         * 
         * public Flux<TokenizeEntity> getPayments(Integer idUser) {
         * return paymeRepository.findByIdUser(idUser);
         * }
         * 
         * public Mono<TransactionNecessaryResponse> savePayment(Integer idUser,
         * AuthorizationResponse authorizationResponse) {
         * PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser,
         * Role.ROLE_USER, authorizationResponse);
         * 
         * return authorizationRepository.save(payMeAuthorization)
         * .flatMap(savedAuthorization ->
         * userClientRepository.findById(idUser)
         * .flatMap(userClient ->
         * bookingRepository.findByBookingId(savedAuthorization.getIdBooking())
         * .flatMap(booking -> {
         * if (booking.getBookingStateId() == 3) {
         * booking.setBookingStateId(2);
         * return bookingRepository.save(booking);
         * } else {
         * return Mono.just(booking);
         * }
         * })
         * .flatMap(updatedBooking -> sendSuccessEmail(userClient.getEmail()))
         * .thenReturn(new TransactionNecessaryResponse(true))
         * )
         * )
         * .onErrorResume(e ->
         * userClientRepository.findById(idUser)
         * .flatMap(userClient -> sendErrorEmail(userClient.getEmail(), e.getMessage()))
         * .then(Mono.error(e))
         * );
         * }
         * 
         * private Mono<Void> sendSuccessEmail(String email) {
         * String emailBody = generateSuccessEmailBody();
         * return emailService.sendEmail(email, "Pago Exitoso", emailBody);
         * }
         * 
         * private String generateSuccessEmailBody() {
         * String body = "<html><head><title></title></head><body style='color:black'>";
         * body += "<div style='width: 100%'>";
         * body += "<h1 style='text-align: center;'>Pago Exitoso</h1>";
         * body += "<p>Estimado cliente,</p>";
         * body += "<p>Su pago ha sido procesado con éxito.</p>";
         * body += "<p>Gracias por su confianza.</p>";
         * body += "</div>";
         * body += "</body></html>";
         * return body;
         * }
         * 
         * private Mono<Void> sendErrorEmail(String email, String errorMessage) {
         * String emailBody = generateErrorEmailBody(errorMessage);
         * return emailService.sendEmail(email, "Error en el Pago", emailBody);
         * }
         * 
         * private String generateErrorEmailBody(String errorMessage) {
         * String body = "<html><head><title></title></head><body style='color:black'>";
         * body += "<div style='width: 100%'>";
         * body += "<h1 style='text-align: center;'>Error en el Pago</h1>";
         * body += "<p>Estimado cliente,</p>";
         * body += "<p>Ha ocurrido un error durante el procesamiento de su pago.</p>";
         * body += "<p>Detalles del error:</p>";
         * body += "<p>" + errorMessage + "</p>";
         * body += "<p>Por favor, póngase en contacto con soporte.</p>";
         * body += "</div>";
         * body += "</body></html>";
         * return body;
         * }
         */
        private final PayMeRepository paymeRepository;
        private final AuthorizationRepository authorizationRepository;
        private final UserClientRepository userClientRepository;
        private final PaymentBookRepository paymentBookRepository;
        private final BookingRepository bookingRepository;
        private final EmailService emailService;
        private final InvoiceServiceI invoiceService;
        private final RefusePaymentService refusePaymentService;

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
                                "create:token post:charges offline_access");
                return webClient.post()
                                .uri(URL_ACCESS_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("ALG-API-VERSION", ALG_API_VERSION)
                                .body(BodyInserters.fromValue(body))
                                .retrieve()
                                .bodyToMono(AuthorizeResponse.class)
                                .doOnError(WebClientResponseException.class, e -> {
                                        System.out.println(e.getResponseBodyAsString());
                                })
                                .onErrorResume(WebClientResponseException.class, e -> {
                                        System.out.println(e);
                                        return Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST,
                                                        "Error getting token" + e.getResponseBodyAsString()) {
                                        });
                                });
        }

        public Mono<NonceResponse> getNonce() {
                NonceRequest nonceRequest = new NonceRequest(
                                "create.nonce",
                                AUDIENCE,
                                CLIENT_ID,
                                "create:token post:charges");
                WebClient webClient = WebClient.create();
                return getToken()
                                .flatMap(authorizeResponse -> webClient.post()
                                                .uri(URL_NONCE)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("ALG-API-VERSION", ALG_API_VERSION)
                                                .header("Authorization",
                                                                authorizeResponse.getTokenType() + " "
                                                                                + authorizeResponse.getAccessToken())
                                                .body(BodyInserters.fromValue(nonceRequest))
                                                .retrieve()
                                                .bodyToMono(NonceResponse.class)
                                                .onErrorResume(e -> Mono.error(new HttpStatusCodeException(
                                                                HttpStatus.BAD_REQUEST, "Error getting nonce") {
                                                })));
        }

        public Flux<TokenizeEntity> getPayments(Integer idUser) {
                return paymeRepository.findByIdUser(idUser);
        }

        /*
         * public Mono<TransactionNecessaryResponse> savePayment(Integer idUser,
         * AuthorizationResponse authorizationResponse) {
         * PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser,
         * Role.ROLE_USER, authorizationResponse);
         * 
         * return authorizationRepository.save(payMeAuthorization)
         * .flatMap(savedAuthorization ->
         * userClientRepository.findById(idUser)
         * .flatMap(userClient ->
         * bookingRepository.findByBookingId(savedAuthorization.getIdBooking())
         * .flatMap(booking -> {
         * if (booking.getBookingStateId() == 3) {
         * booking.setBookingStateId(2);
         * return bookingRepository.save(booking);
         * } else {
         * return Mono.just(booking);
         * }
         * })
         * .flatMap(updatedBooking -> {
         * BigDecimal monto = new
         * BigDecimal(authorizationResponse.getTransaction().getAmount());
         * 
         * PaymentBookEntity paymentBook = PaymentBookEntity.builder()
         * .bookingId(updatedBooking.getBookingId())
         * .userClientId(updatedBooking.getUserClientId())
         * .refuseReasonId(1)
         * .cancelReasonId(1)
         * .paymentMethodId(5)
         * .paymentStateId(1)
         * .paymentTypeId(6)
         * .paymentSubTypeId(1)
         * .currencyTypeId(1)
         * .amount(monto)
         * .description("Pago exitoso")
         * .paymentDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima"))))
         * .operationCode(authorizationResponse.getId())
         * .note("Nota de pago")
         * .totalCost(monto)
         * .imageVoucher("Pago con Tarjeta")
         * .totalPoints(0)
         * .paymentComplete(true)
         * .pendingpay(0)
         * .build();
         * return paymentBookRepository.save(paymentBook)
         * .then(sendSuccessEmail(userClient.getEmail()));
         * })
         * .thenReturn(new TransactionNecessaryResponse(true))
         * )
         * )
         * .onErrorResume(e ->
         * userClientRepository.findById(idUser)
         * .flatMap(userClient -> sendErrorEmail(userClient.getEmail(), e.getMessage()))
         * .then(Mono.error(e))
         * );
         * }
         */

        public Mono<TransactionNecessaryResponse> savePayment(Integer idUser,
                        AuthorizationResponse authorizationResponse, String invoiceType, String invoiceDocumentNumber,
                        double totalDiscount, double percentageDiscount, double totalCostWithOutDiscount) {
                PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser, Role.ROLE_USER,
                                authorizationResponse);
                if (Objects.equals(payMeAuthorization.getStatusCode(), "01")) {
                        return authorizationRepository.findById(idUser)
                                        .flatMap(savedAuthorization -> bookingRepository
                                                        .findByBookingId(savedAuthorization.getIdBooking())
                                                        .flatMap(booking -> {
                                                                booking.setBookingStateId(3);
                                                                return bookingRepository.save(booking);
                                                        }))
                                        .flatMap(updatedBooking -> userClientRepository.findById(idUser)
                                                        .flatMap(userClient -> sendErrorEmail(userClient.getEmail(),
                                                                        "Error al procesar el pago. Intente nuevamente."))
                                                        .then(Mono.just(new TransactionNecessaryResponse(false))));
                } else {

                        return authorizationRepository.save(payMeAuthorization)
                                        .doOnSubscribe(subscription -> System.out.println("Subscription started"))
                                        .doOnError(e -> System.err.println("Error occurred: " + e.getMessage()))
                                        .switchIfEmpty(Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST,
                                                        "Error al guardar el pago") {
                                        }))
                                        .flatMap(savedAuthorization -> bookingRepository
                                                        .findByBookingId(payMeAuthorization.getIdBooking())
                                                        .switchIfEmpty(Mono.error(new HttpStatusCodeException(
                                                                        HttpStatus.BAD_REQUEST,
                                                                        "Error al guardar el pago") {
                                                        }))
                                                        .flatMap(booking -> {
                                                                if (booking.getBookingStateId() == 3) {
                                                                        booking.setBookingStateId(2);
                                                                }
                                                                return Mono.zip(bookingRepository.save(
                                                                                booking),
                                                                                this.bookingRepository
                                                                                                .getRoomNameAndDescriptionfindByBookingId(
                                                                                                                booking.getBookingId()),
                                                                                userClientRepository.findById(booking
                                                                                                .getUserClientId()));

                                                        }).flatMap(tuple -> {
                                                                BookingEntity updatedBooking = tuple.getT1();
                                                                BookingAndRoomNameDto bookingAndRoomNameDto = tuple
                                                                                .getT2();
                                                                UserClientEntity userClient = tuple.getT3();
                                                                BigDecimal monto = new BigDecimal(
                                                                                authorizationResponse
                                                                                                .getTransaction()
                                                                                                .getAmount());
                                                                BigDecimal totalCost = monto.divide(
                                                                                BigDecimal.valueOf(
                                                                                                100000));
                                                                InvoiceType type = InvoiceType
                                                                                .getInvoiceTypeByName(
                                                                                                invoiceType.toUpperCase());
                                                                InvoiceClientDomain invoiceClientDomain = new InvoiceClientDomain(
                                                                                userClient.getFirstName(),
                                                                                invoiceDocumentNumber,
                                                                                userClient.getAddress(),
                                                                                userClient.getCellNumber(),
                                                                                userClient.getEmail(),
                                                                                updatedBooking.getUserClientId());
                                                                InvoiceDomain invoice = new InvoiceDomain(
                                                                                invoiceClientDomain,
                                                                                updatedBooking.getBookingId(),
                                                                                18.0,
                                                                                InvoiceCurrency.PEN,
                                                                                type,
                                                                                percentageDiscount);
                                                                invoice.setOperationCode(
                                                                                authorizationResponse
                                                                                                .getId());
                                                                InvoiceItemDomain item = new InvoiceItemDomain(
                                                                                bookingAndRoomNameDto.getRoomName(),
                                                                                bookingAndRoomNameDto
                                                                                                .getRoomDescription(),
                                                                                1,
                                                                                totalCost);
                                                                invoice.addItemWithIncludedIgv(
                                                                                item);
                                                                PaymentBookEntity paymentBook = PaymentBookEntity
                                                                                .builder()
                                                                                .bookingId(updatedBooking
                                                                                                .getBookingId())
                                                                                .userClientId(updatedBooking
                                                                                                .getUserClientId())
                                                                                .refuseReasonId(1)
                                                                                .paymentMethodId(1)
                                                                                .paymentStateId(2)
                                                                                .paymentTypeId(3)
                                                                                .paymentSubTypeId(6)
                                                                                .currencyTypeId(1)
                                                                                .amount(totalCost)
                                                                                .description("Pago exitoso")
                                                                                .paymentDate(Timestamp
                                                                                                .valueOf(LocalDateTime
                                                                                                                .now(ZoneId.of("America/Lima"))))
                                                                                .operationCode(authorizationResponse
                                                                                                .getId())
                                                                                .note("Nota de pago")
                                                                                .totalCost(totalCost)
                                                                                .invoiceDocumentNumber(
                                                                                                invoiceDocumentNumber)
                                                                                .invoiceType(invoiceType)
                                                                                .imageVoucher("Pago con Tarjeta")
                                                                                .totalPoints(0)
                                                                                .paymentComplete(true)
                                                                                .pendingpay(1)
                                                                                .totalDiscount(totalDiscount)
                                                                                .percentageDiscount(
                                                                                                percentageDiscount)
                                                                                .totalCostWithOutDiscount(
                                                                                                totalCostWithOutDiscount)
                                                                                .build();
                                                                return paymentBookRepository
                                                                                .save(paymentBook)
                                                                                .flatMap(paymentBookR -> {
                                                                                        invoice.setPaymentBookId(
                                                                                                        paymentBookR.getPaymentBookId());
                                                                                        /*
                                                                                         * return
                                                                                         * Mono.zip(this.invoiceService
                                                                                         * .save(invoice),
                                                                                         * sendSuccessEmail(
                                                                                         * userClient.getEmail(),
                                                                                         * paymentBookR.getPaymentBookId
                                                                                         * ()));
                                                                                         */
                                                                                        return this.invoiceService
                                                                                                        .save(invoice)
                                                                                                        .then(sendSuccessEmail(
                                                                                                                        userClient.getEmail(),
                                                                                                                        paymentBookR.getPaymentBookId()));

                                                                                })
                                                                                .thenReturn(new TransactionNecessaryResponse(
                                                                                                true))
                                                                                .onErrorResume(e -> authorizationRepository
                                                                                                .findById(idUser)
                                                                                                .flatMap(savedA -> bookingRepository
                                                                                                                .findByBookingId(
                                                                                                                                savedA
                                                                                                                                                .getIdBooking())
                                                                                                                .flatMap(booking -> {
                                                                                                                        booking.setBookingStateId(
                                                                                                                                        3);
                                                                                                                        return bookingRepository
                                                                                                                                        .save(booking);
                                                                                                                }))
                                                                                                .flatMap(booking -> userClientRepository
                                                                                                                .findById(idUser)
                                                                                                                .flatMap(userCliente -> sendErrorEmail(
                                                                                                                                userCliente.getEmail(),
                                                                                                                                e.getMessage()))
                                                                                                                .then(Mono.error(
                                                                                                                                e))));
                                                        })

                                        );
                        /*
                         * return authorizationRepository.save(payMeAuthorization)
                         * .doOnSubscribe(subscription -> System.out.println("Subscription started"))
                         * .doOnError(e -> System.err.println("Error occurred: " + e.getMessage()))
                         * .switchIfEmpty(Mono.error(new HttpStatusCodeException(HttpStatus.BAD_REQUEST,
                         * "Error al guardar el pago") {
                         * }))
                         * .flatMap(savedAuthorization ->
                         * 
                         * userClientRepository.findById(idUser)
                         * .switchIfEmpty(Mono.error(new HttpStatusCodeException(
                         * HttpStatus.BAD_REQUEST,
                         * "Error al guardar el pago") {
                         * }))
                         * .flatMap(userClient -> bookingRepository
                         * .findByBookingId(savedAuthorization
                         * .getIdBooking())
                         * .switchIfEmpty(Mono.error(
                         * new HttpStatusCodeException(
                         * HttpStatus.BAD_REQUEST,
                         * "Error al guardar el pago") {
                         * }))
                         * 
                         * .flatMap(booking -> {
                         * if (booking.getBookingStateId() == 3) {
                         * booking.setBookingStateId(2);
                         * }
                         * return bookingRepository.save(booking)
                         * .flatMap(b -> this.bookingRepository
                         * .getRoomNameAndDescriptionfindByBookingId(
                         * b.getBookingId()));
                         * })
                         * .flatMap(updatedBooking -> {
                         * BigDecimal monto = new BigDecimal(
                         * authorizationResponse
                         * .getTransaction()
                         * .getAmount());
                         * BigDecimal totalCost = monto.divide(
                         * BigDecimal.valueOf(
                         * 100000));
                         * InvoiceType type = InvoiceType
                         * .getInvoiceTypeByName(
                         * invoiceType.toUpperCase());
                         * InvoiceClientDomain invoiceClientDomain = new InvoiceClientDomain(
                         * userClient.getFirstName(),
                         * invoiceDocumentNumber,
                         * userClient.getAddress(),
                         * userClient.getCellNumber(),
                         * userClient.getEmail(),
                         * updatedBooking.getUserClientId());
                         * InvoiceDomain invoice = new InvoiceDomain(
                         * invoiceClientDomain,
                         * updatedBooking.getBookingId(),
                         * 18.0,
                         * InvoiceCurrency.PEN,
                         * type,
                         * percentageDiscount);
                         * invoice.setOperationCode(
                         * authorizationResponse
                         * .getId());
                         * InvoiceItemDomain item = new InvoiceItemDomain(
                         * updatedBooking.getRoomName(),
                         * updatedBooking.getRoomDescription(),
                         * 1,
                         * totalCost);
                         * invoice.addItemWithIncludedIgv(
                         * item);
                         * PaymentBookEntity paymentBook = PaymentBookEntity
                         * .builder()
                         * .bookingId(updatedBooking
                         * .getBookingId())
                         * .userClientId(updatedBooking
                         * .getUserClientId())
                         * .refuseReasonId(1)
                         * .paymentMethodId(1)
                         * .paymentStateId(2)
                         * .paymentTypeId(3)
                         * .paymentSubTypeId(6)
                         * .currencyTypeId(1)
                         * .amount(totalCost)
                         * .description("Pago exitoso")
                         * .paymentDate(Timestamp
                         * .valueOf(LocalDateTime
                         * .now(ZoneId.of("America/Lima"))))
                         * .operationCode(authorizationResponse
                         * .getId())
                         * .note("Nota de pago")
                         * .totalCost(totalCost)
                         * .invoiceDocumentNumber(
                         * invoiceDocumentNumber)
                         * .invoiceType(invoiceType)
                         * .imageVoucher("Pago con Tarjeta")
                         * .totalPoints(0)
                         * .paymentComplete(true)
                         * .pendingpay(1)
                         * .totalDiscount(totalDiscount)
                         * .percentageDiscount(
                         * percentageDiscount)
                         * .totalCostWithOutDiscount(
                         * totalCostWithOutDiscount)
                         * .build();
                         * return paymentBookRepository
                         * .save(paymentBook)
                         * .flatMap(paymentBookR -> {
                         * invoice.setPaymentBookId(
                         * paymentBookR.getPaymentBookId());
                         * return Mono.zip(this.invoiceService
                         * .save(invoice),
                         * sendSuccessEmail(
                         * userClient.getEmail(),
                         * paymentBookR.getPaymentBookId()));
                         * 
                         * })
                         * .thenReturn(new TransactionNecessaryResponse(
                         * true));
                         * })))
                         * .onErrorResume(e -> authorizationRepository.findById(idUser)
                         * .flatMap(savedAuthorization -> bookingRepository
                         * .findByBookingId(savedAuthorization
                         * .getIdBooking())
                         * .flatMap(booking -> {
                         * booking.setBookingStateId(3);
                         * return bookingRepository.save(booking);
                         * }))
                         * .flatMap(updatedBooking -> userClientRepository.findById(idUser)
                         * .flatMap(userClient -> sendErrorEmail(
                         * userClient.getEmail(),
                         * e.getMessage()))
                         * .then(Mono.error(e))));
                         */
                }

        }

        private Mono<Void> sendSuccessEmail(String email, int paymentBookId) {
                System.out.println("Enviando correo de pago exitoso" + paymentBookId);
                return this.refusePaymentService.getPaymentDetails(paymentBookId)
                                .map(paymentDetails -> {
                                        String nombres = (String) paymentDetails.get("Nombres");
                                        Integer codigoReserva = (Integer) paymentDetails.get("Codigo Reserva");
                                        String checkIn = (String) paymentDetails.get("Check In");
                                        String checkOut = (String) paymentDetails.get("Check Out");
                                        long duracionEstancia = (long) paymentDetails.get("Duración Estancia");
                                        String cantidadPersonas = (String) paymentDetails.get("Cantidad de Personas");
                                        String imagen = (String) paymentDetails.get("Imagen");
                                        String roomName = (String) paymentDetails.get("RoomName");
                                        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
                                        BookingEmailDto bookingEmailDto = new BookingEmailDto(
                                                        roomName, nombres, codigoReserva.toString(), checkIn, checkOut,
                                                        checkIn, imagen, (int) duracionEstancia,
                                                        "Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593",
                                                        cantidadPersonas);
                                        ConfirmPaymentByBankTransferAndCardTemplateEmail confirmReserveBookingTemplateEmail = new ConfirmPaymentByBankTransferAndCardTemplateEmail(
                                                        nombres, bookingEmailDto);
                                        baseEmailReserve.addEmailHandler(confirmReserveBookingTemplateEmail);
                                        System.out.println("Email body: " + baseEmailReserve.execute());
                                        return baseEmailReserve.execute();

                                }).flatMap(emailBody -> emailService.sendEmail(email, "Pago Exitoso", emailBody));
        }

        private String generateSuccessEmailBody() {
                String body = "<html>\n" +
                                "<head>\n" +
                                "    <title>Bienvenido</title>\n" +
                                "    <style>\n" +
                                "        body {\n" +
                                "            font-family: Arial, sans-serif;\n" +
                                "            margin: 0;\n" +
                                "            padding: 0;\n" +
                                "            color: black;\n" +
                                "            background-color: white; /* Color de fondo */\n" +
                                "        }\n" +
                                "        .header {\n" +
                                "            width: 100%;\n" +
                                "            position: relative;\n" +
                                "            background-color: white; /* Color de fondo del encabezado */\n" +
                                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n"
                                +
                                "        }\n" +
                                "        .logos-right {\n" +
                                "            position: absolute;\n" +
                                "            top: 10px;\n" +
                                "            right: 10px;\n" +
                                "            display: flex;\n" +
                                "            gap: 5px;\n" +
                                "        }\n" +
                                "        .logos-right img {\n" +
                                "            width: 30px;\n" +
                                "            height: 30px;\n" +
                                "        }\n" +
                                "        .logo-left {\n" +
                                "            width: 50px;\n" +
                                "            position: absolute;\n" +
                                "            top: 10px;\n" +
                                "            left: 10px;\n" +
                                "        }\n" +
                                "        .banner {\n" +
                                "            width: 540px;\n" +
                                "            border-top-left-radius: 20px;\n" +
                                "            border-top-right-radius: 20px;\n" +
                                "            display: block;\n" +
                                "            margin: 0 auto;\n" +
                                "        }\n" +
                                "        .container {\n" +
                                "            width: 500px;\n" +
                                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
                                "            margin: 0 auto;\n" +
                                "            padding: 20px;\n" +
                                "            border-bottom-left-radius: 10px;\n" +
                                "            border-bottom-right-radius: 10px;\n" +
                                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                                "        }\n" +
                                "        .content {\n" +
                                "            text-align: center;\n" +
                                "            padding: 20px;\n" +
                                "        }\n" +
                                "        .content h1 {\n" +
                                "            margin-top: 20px;\n" +
                                "            font-weight: bold;\n" +
                                "            font-style: italic;\n" +
                                "        }\n" +
                                "        .content h3, .content p {\n" +
                                "            margin: 10px 0;\n" +
                                "        }\n" +
                                "        .footer {\n" +
                                "            width: 100%;\n" +
                                "            text-align: center;\n" +
                                "            margin: 20px 0;\n" +
                                "        }\n" +
                                "        .help-section {\n" +
                                "            width: 500px;\n" +
                                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
                                "            margin: 20px auto;\n" +
                                "            padding: 20px;\n" +
                                "            border-radius: 10px;\n" +
                                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                                "            text-align: center;\n" +
                                "        }\n" +
                                "    </style>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "    <div class=\"header\">\n" +
                                "        <!-- Encabezado con logos -->\n" +
                                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n"
                                +
                                "    </div>\n" +
                                "\n" +
                                "    <!-- Imagen de banner -->\n" +
                                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                                "\n" +
                                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                                "    <div class=\"container\">\n" +
                                "        <div class=\"content\">\n" +
                                "            <h1 style='text-align: center;'>Pago exitoso</h1>\n" +
                                "            <p>Estimado cliente,</p>\n" +
                                "            <p>Su pago ha sido procesado con exito.</p>\n" +
                                "            <p>Gracias por su confianza.</p>\n" +
                                "        </div>\n" +
                                "    </div>\n" +
                                "\n" +
                                "    <!-- Sección de ayuda -->\n" +
                                "    <div class=\"help-section\">\n" +
                                "        <h3>Necesitas ayuda?</h3>\n" +
                                "        <p>Comunicate con nosotros a traves de los siguientes medios:</p>\n" +
                                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                                "    </div>\n" +
                                "</body>\n" +
                                "</html>";

                return body;
        }

        private Mono<Void> sendErrorEmail(String email, String errorMessage) {
                String emailBody = generateErrorEmailBody(errorMessage);
                return emailService.sendEmail(email, "Error en el Pago", emailBody);
        }

        private String generateErrorEmailBody(String errorMessage) {
                String body = "<html>\n" +
                                "<head>\n" +
                                "    <title>Bienvenido</title>\n" +
                                "    <style>\n" +
                                "        body {\n" +
                                "            font-family: Arial, sans-serif;\n" +
                                "            margin: 0;\n" +
                                "            padding: 0;\n" +
                                "            color: black;\n" +
                                "            background-color: white; /* Color de fondo */\n" +
                                "        }\n" +
                                "        .header {\n" +
                                "            width: 100%;\n" +
                                "            position: relative;\n" +
                                "            background-color: white; /* Color de fondo del encabezado */\n" +
                                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n"
                                +
                                "        }\n" +
                                "        .logos-right {\n" +
                                "            position: absolute;\n" +
                                "            top: 10px;\n" +
                                "            right: 10px;\n" +
                                "            display: flex;\n" +
                                "            gap: 5px;\n" +
                                "        }\n" +
                                "        .logos-right img {\n" +
                                "            width: 30px;\n" +
                                "            height: 30px;\n" +
                                "        }\n" +
                                "        .logo-left {\n" +
                                "            width: 50px;\n" +
                                "            position: absolute;\n" +
                                "            top: 10px;\n" +
                                "            left: 10px;\n" +
                                "        }\n" +
                                "        .banner {\n" +
                                "            width: 540px;\n" +
                                "            border-top-left-radius: 20px;\n" +
                                "            border-top-right-radius: 20px;\n" +
                                "            display: block;\n" +
                                "            margin: 0 auto;\n" +
                                "        }\n" +
                                "        .container {\n" +
                                "            width: 500px;\n" +
                                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
                                "            margin: 0 auto;\n" +
                                "            padding: 20px;\n" +
                                "            border-bottom-left-radius: 10px;\n" +
                                "            border-bottom-right-radius: 10px;\n" +
                                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                                "        }\n" +
                                "        .content {\n" +
                                "            text-align: center;\n" +
                                "            padding: 20px;\n" +
                                "        }\n" +
                                "        .content h1 {\n" +
                                "            margin-top: 20px;\n" +
                                "            font-weight: bold;\n" +
                                "            font-style: italic;\n" +
                                "        }\n" +
                                "        .content h3, .content p {\n" +
                                "            margin: 10px 0;\n" +
                                "        }\n" +
                                "        .footer {\n" +
                                "            width: 100%;\n" +
                                "            text-align: center;\n" +
                                "            margin: 20px 0;\n" +
                                "        }\n" +
                                "        .help-section {\n" +
                                "            width: 500px;\n" +
                                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
                                "            margin: 20px auto;\n" +
                                "            padding: 20px;\n" +
                                "            border-radius: 10px;\n" +
                                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                                "            text-align: center;\n" +
                                "        }\n" +
                                "    </style>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "    <div class=\"header\">\n" +
                                "        <!-- Encabezado con logos -->\n" +
                                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n"
                                +
                                "    </div>\n" +
                                "\n" +
                                "    <!-- Imagen de banner -->\n" +
                                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                                "\n" +
                                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                                "    <div class=\"container\">\n" +
                                "        <div class=\"content\">\n" +
                                "            <p>Estimado cliente,</p>\n" +
                                "            <p>Ha ocurrido un error durante el procesamiento de su pago.</p>\n" +
                                "            <p>Detalles del error:</p>\n" +
                                "            <p>Error en el pago por motivos de su entidad bancaria</p>\n" +
                                "        </div>\n" +
                                "    </div>\n" +
                                "\n" +
                                "    <!-- Sección de ayuda -->\n" +
                                "    <div class=\"help-section\">\n" +
                                "        <h3>¿Necesitas ayuda?</h3>\n" +
                                "        <p>Comunicate con nosotros a través de los siguientes medios:</p>\n" +
                                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                                "    </div>\n" +
                                "</body>\n" +
                                "</html>";

                return body;
        }

        public Mono<TransactionNecessaryResponse> savePayment2(Integer idUser,
                        AuthorizationResponse authorizationResponse) {
                PayMeAuthorization payMeAuthorization = AuthorizationResponse.create(idUser, Role.ROLE_USER,
                                authorizationResponse);

                return authorizationRepository.save(payMeAuthorization)
                                .flatMap(savedAuthorization -> userClientRepository.findById(idUser)
                                                .flatMap(userClient -> bookingRepository
                                                                .findByBookingId(savedAuthorization.getIdBooking())
                                                                .flatMap(booking -> {
                                                                        if (booking.getBookingStateId() == 3) {
                                                                                booking.setBookingStateId(2);
                                                                                return bookingRepository.save(booking);
                                                                        } else {
                                                                                return Mono.just(booking);
                                                                        }
                                                                })
                                                                .flatMap(updatedBooking -> sendSuccessEmail(
                                                                                userClient.getEmail(), 1))
                                                                .thenReturn(new TransactionNecessaryResponse(true))))
                                .onErrorResume(e -> userClientRepository.findById(idUser)
                                                .flatMap(userClient -> sendErrorEmail(userClient.getEmail(),
                                                                e.getMessage()))
                                                .then(Mono.error(e)));
        }
}