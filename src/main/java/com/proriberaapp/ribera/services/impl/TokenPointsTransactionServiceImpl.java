package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.Infraestructure.repository.PartnerPointsRepository;
import com.proriberaapp.ribera.Infraestructure.repository.TokenPointsTransactionRepository;
import com.proriberaapp.ribera.services.EmailService;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.S3UploadService;
import com.proriberaapp.ribera.services.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
/*
@Service
public class TokenPointsTransactionServiceImpl implements TokenPointsTransactionService {

    private final TokenPointsTransactionRepository tokenPointsTransactionRepository;
    private final EmailService emailService;

    @Autowired
    public TokenPointsTransactionServiceImpl(TokenPointsTransactionRepository tokenPointsTransactionRepository, EmailService emailService) {
        this.tokenPointsTransactionRepository = tokenPointsTransactionRepository;
        this.emailService = emailService;
    }

    @Override
    public Mono<TokenPointsTransaction> createToken(Integer partnerPointId, Integer bookingId) {
        TokenPointsTransaction tokenPointsTransaction = TokenPointsTransaction.builder()
                .codigoToken(UUID.randomUUID().toString())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                .expirationDate(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusHours(24)))
                .partnerPointId(partnerPointId)
                .bookingId(bookingId)
                .build();

        return tokenPointsTransactionRepository.save(tokenPointsTransaction);
    }
    @Override
    public Mono<TokenPointsTransaction> createTokenAndSendEmail(Integer partnerPointId, Integer bookingId) {
        return tokenPointsTransactionRepository.findEmailByPartnerPointId(partnerPointId)
                .flatMap(email -> {
                    TokenPointsTransaction tokenPointsTransaction = TokenPointsTransaction.builder()
                            .codigoToken(UUID.randomUUID().toString())
                            .dateCreated(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                            .expirationDate(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusHours(24)))
                            .partnerPointId(partnerPointId)
                            .bookingId(bookingId)
                            .build();

                    return tokenPointsTransactionRepository.save(tokenPointsTransaction)
                            .flatMap(savedToken -> {
                                String linkPayment = "https://ribera-dev.inclub.world/payment-validation?token=" + savedToken.getCodigoToken();
                                String subject = "Pagos";
                                String body = generateEmailBody(email, linkPayment);
                                return emailService.sendEmail(email, subject, body)
                                        .thenReturn(savedToken);
                            });
                });
    }

    private String generateEmailBody(String email, String linkPayment) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Bienvenido " + email + "</h1>";
        body += "<h3 style='text-align: center;'>Producto por Adquirir: Reserva</h3>";
        body += "<h3 style='text-align: center;'>Descripcion: Reserva de Habitacion</h3>";
        body += "<h2 style='text-align: center;'>Muy pronto formaras parte de la familia inResorts. Estamos a la espera de que procese su pago a traves del siguiente portal</h2>";
        body += "<center><p style='margin-left: 10%; margin-right: 10%;'>Puede procesar el pago cuando se sienta preparado, pero recuerde que tienes 24 Horas para realizarlo</p></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>Click en el boton para pagar</p>";
        body += "<a style='text-decoration: none;' href='" + linkPayment + "'>";
        body += "<center><div style='background: #0d80ea; border-radius: 10px; width: 158px; height: 30px; font-size: 16px; color: white; font-weight: bold; padding: 4px; padding-top: 10px; cursor: pointer; text-align: center; margin: 23px;'>Validar pago<div></center>";
        body += "</a></div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div></center>";
        body += "</body></html>";

        return body;
    }
}
 */
@Service
public class TokenPointsTransactionServiceImpl implements TokenPointsTransactionService {

    private final TokenPointsTransactionRepository tokenPointsTransactionRepository;
    private final EmailService emailService;
    private final PDFGeneratorService pdfGeneratorService;
    private final WebClient webClient;
    private final S3UploadService s3UploadService;

    @Autowired
    public TokenPointsTransactionServiceImpl(TokenPointsTransactionRepository tokenPointsTransactionRepository, EmailService emailService, PDFGeneratorService pdfGeneratorService, WebClient.Builder webClientBuilder, S3UploadService s3UploadService) {
        this.tokenPointsTransactionRepository = tokenPointsTransactionRepository;
        this.emailService = emailService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.webClient = webClientBuilder.baseUrl("https://riberams-dev.inclub.world/api/v1/s3-client/upload").build();
        this.s3UploadService = s3UploadService;
    }

    @Override
    public Mono<TokenPointsTransaction> createToken(Integer partnerPointId, Integer bookingId) {
        TokenPointsTransaction tokenPointsTransaction = TokenPointsTransaction.builder()
                .codigoToken(UUID.randomUUID().toString())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                .expirationDate(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusHours(24)))
                .partnerPointId(partnerPointId)
                .bookingId(bookingId)
                .build();

        return tokenPointsTransactionRepository.save(tokenPointsTransaction);
    }

    @Override
    public Mono<TokenPointsTransaction> createTokenAndSendEmail(Integer partnerPointId, Integer bookingId) {
        return tokenPointsTransactionRepository.findEmailByPartnerPointId(partnerPointId)
                .flatMap(email -> {
                    TokenPointsTransaction tokenPointsTransaction = TokenPointsTransaction.builder()
                            .codigoToken(UUID.randomUUID().toString())
                            .dateCreated(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                            .expirationDate(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusHours(24)))
                            .partnerPointId(partnerPointId)
                            .bookingId(bookingId)
                            .build();

                    return tokenPointsTransactionRepository.save(tokenPointsTransaction)
                            .flatMap(savedToken -> {
                                String linkPayment = "https://ribera-dev.inclub.world/payment-validation?token=" + savedToken.getCodigoToken();
                                String subject = "Pagos";
                                String body = generateEmailBody(email, linkPayment);

                                try {
                                    byte[] pdfData = pdfGeneratorService.generatePDF(body);
                                    return s3UploadService.uploadPdf(pdfData)
                                            .then(emailService.sendEmail(email, subject, body))
                                            .thenReturn(savedToken);
                                } catch (IOException e) {
                                    return Mono.error(new RuntimeException("Failed to generate PDF", e));
                                }
                            });
                });
    }

    private Mono<Void> uploadPDFToS3(byte[] pdfBytes) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", pdfBytes).filename("payment_validation.pdf").contentType(MediaType.APPLICATION_PDF);

        return webClient.post()
                .header("folderNumber", "13")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Void.class);
    }

    private String generateEmailBody(String email, String linkPayment) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png' />";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Bienvenido " + email + "</h1>";
        body += "<h3 style='text-align: center;'>Producto por Adquirir: Reserva</h3>";
        body += "<h3 style='text-align: center;'>Descripcion: Reserva de Habitacion</h3>";
        body += "<h2 style='text-align: center;'>Muy pronto formaras parte de la familia inResorts. Estamos a la espera de que procese su pago a traves del siguiente portal</h2>";
        body += "<center><p style='margin-left: 10%; margin-right: 10%;'>Puede procesar el pago cuando se sienta preparado, pero recuerde que tienes 24 Horas para realizarlo</p></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>Click en el boton para pagar</p>";
        body += "<a style='text-decoration: none;' href='" + linkPayment + "'>";
        body += "<center><div style='background: #0d80ea; border-radius: 10px; width: 158px; height: 30px; font-size: 16px; color: white; font-weight: bold; padding: 4px; padding-top: 10px; cursor: pointer; text-align: center; margin: 23px;'>Validar pago</div></center>";
        body += "</a></div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div>";
        body += "</body></html>";

        return body;
    }
}
