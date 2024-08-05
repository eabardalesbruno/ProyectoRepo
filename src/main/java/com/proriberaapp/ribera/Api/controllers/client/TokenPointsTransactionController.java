package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.CreateTokenRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.PointsRequest;
import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.S3UploadService;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
/*
@RestController
@RequestMapping("/api/v1/points")
public class TokenPointsTransactionController {

    private final TokenPointsTransactionService tokenPointsTransactionService;

    @Autowired
    public TokenPointsTransactionController(TokenPointsTransactionService tokenPointsTransactionService) {
        this.tokenPointsTransactionService = tokenPointsTransactionService;
    }

    @PostMapping("/create")
    public Mono<String> createToken(@RequestBody PointsRequest tokenRequest) {
        return tokenPointsTransactionService.createToken(tokenRequest.getPartnerPointId(), tokenRequest.getBookingId())
                .map(token -> token.getCodigoToken());
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<Map<String, String>>> createTokenAndSendEmail(@RequestBody CreateTokenRequest request) {
        return tokenPointsTransactionService.createTokenAndSendEmail(request.getPartnerPointId(), request.getBookingId())
                .map(token -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token.getCodigoToken());
                    response.put("linkPayment", "https://ribera-dev.inclub.world/payment-validation?token=" + token.getCodigoToken());
                    response.put("mensaje", "Enviado");
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    return tokenPointsTransactionService.createToken(request.getPartnerPointId(), request.getBookingId())
                            .map(token -> {
                                Map<String, String> response = new HashMap<>();
                                response.put("token", token.getCodigoToken());
                                response.put("linkPayment", "https://ribera-dev.inclub.world/payment-validation?token=" + token.getCodigoToken());
                                response.put("mensaje", "Enviado (sin email)");
                                return ResponseEntity.ok(response);
                            });
                });
    }
}
 */
@RestController
@RequestMapping("/api/v1/points")
public class TokenPointsTransactionController {

    private final TokenPointsTransactionService tokenPointsTransactionService;
    private final PDFGeneratorService pdfGeneratorService;
    private final S3UploadService s3UploadService;
    private final BookingService bookingService; // Servicio para obtener puntos pagados


    @Autowired
    public TokenPointsTransactionController(TokenPointsTransactionService tokenPointsTransactionService,
                                            PDFGeneratorService pdfGeneratorService,
                                            S3UploadService s3UploadService,
                                            BookingService bookingService) {
        this.tokenPointsTransactionService = tokenPointsTransactionService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.s3UploadService = s3UploadService;
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public Mono<String> createToken(@RequestBody PointsRequest tokenRequest) {
        return tokenPointsTransactionService.createToken(tokenRequest.getPartnerPointId(), tokenRequest.getBookingId())
                .map(token -> token.getCodigoToken());
    }

    /*
    @PostMapping("/send")
    public Mono<ResponseEntity<Map<String, String>>> createTokenAndSendEmail(@RequestBody CreateTokenRequest request) {
        return tokenPointsTransactionService.createTokenAndSendEmail(request.getPartnerPointId(), request.getBookingId())
                .flatMap(token -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token.getCodigoToken());
                    response.put("linkPayment", "https://ribera-dev.inclub.world/payment-validation?token=" + token.getCodigoToken());
                    response.put("mensaje", "Enviado");

                    try {
                        String pdfFileName = token.getCodigoToken() + ".pdf";
                        File pdfFile = pdfGeneratorService.generatePDFFile(buildEmailBody(response, points), pdfFileName);

                        // Aquí se debe pasar el folderNumber adecuadamente
                        int folderNumber = 13; // Ajusta este valor según tus necesidades
                        return s3UploadService.uploadPdf(pdfFile, folderNumber) // Pasar folderNumber aquí
                                .map(s3Url -> ResponseEntity.ok(response))
                                .defaultIfEmpty(ResponseEntity.badRequest().build());
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> {
                    return tokenPointsTransactionService.createToken(request.getPartnerPointId(), request.getBookingId())
                            .flatMap(token -> {
                                Map<String, String> response = new HashMap<>();
                                response.put("token", token.getCodigoToken());
                                response.put("linkPayment", "https://ribera-dev.inclub.world/payment-validation?token=" + token.getCodigoToken());
                                response.put("mensaje", "Enviado (sin email)");

                                try {
                                    String pdfFileName = token.getCodigoToken() + ".pdf";
                                    File pdfFile = pdfGeneratorService.generatePDFFile(buildEmailBody(response, points), pdfFileName);

                                    // Aquí se debe pasar el folderNumber adecuadamente
                                    int folderNumber = 13; // Ajusta este valor según tus necesidades
                                    return s3UploadService.uploadPdf(pdfFile, folderNumber) // Pasar folderNumber aquí
                                            .map(s3Url -> ResponseEntity.ok(response))
                                            .defaultIfEmpty(ResponseEntity.badRequest().build());
                                } catch (Exception ex) {
                                    return Mono.error(ex);
                                }
                            });
                });
    }
     */

    @PostMapping("/send")
    public Mono<ResponseEntity<Map<String, String>>> createTokenAndSendEmail(@RequestBody CreateTokenRequest request) {
        return tokenPointsTransactionService.createTokenAndSendEmail(request.getPartnerPointId(), request.getBookingId())
                .flatMap(token -> {
                    // Obtener puntos pagados
                    return bookingService.getRiberaPointsByBookingId(request.getBookingId())
                            .flatMap(points -> {
                                Map<String, String> response = new HashMap<>();
                                response.put("token", token.getCodigoToken());
                                response.put("linkPayment", "https://ribera-dev.inclub.world/payment-validation?token=" + token.getCodigoToken());
                                response.put("mensaje", "Enviado");

                                try {
                                    // Generar archivo PDF
                                    String pdfFileName = token.getCodigoToken() + ".pdf";
                                    File pdfFile = pdfGeneratorService.generatePDFFile(buildEmailBody(response, points), pdfFileName);

                                    // Subir archivo PDF a S3
                                    int folderNumber = 13; // Ajusta este valor según tus necesidades
                                    return s3UploadService.uploadPdf(pdfFile, folderNumber)
                                            .map(s3Url -> ResponseEntity.ok(response))
                                            .defaultIfEmpty(ResponseEntity.status(500).body(Map.of("error", "Error al subir el archivo PDF a S3")));
                                } catch (IOException e) {
                                    return Mono.error(new RuntimeException("Error al generar el archivo PDF", e));
                                } catch (Exception e) {
                                    return Mono.error(new RuntimeException("Error interno del servidor", e));
                                }
                            });
                })
                .onErrorResume(e -> {
                    String errorMessage = e.getMessage();
                    return Mono.just(ResponseEntity.status(500).body(Map.of("error", "Error: " + errorMessage)));
                });
    }


    /*
    private String buildEmailBody(Map<String, String> response) {
        String emailBody = "<html><head><title></title></head><body style='color:black'>";
        emailBody += "<div style='width: 100%;'>";
        emailBody += "<div style='display:flex;'>";
        emailBody += "<img style='width: 100%;' src='http://www.inresorts.club/Views/img/fondo.png' />";
        emailBody += "</div>";
        emailBody += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Voucher de Pago con Puntos</h1>";
        emailBody += "<h3 style='text-align: center;'>Fecha de Pago: 13-06-2023 04:55:00 pm</h3>";
        emailBody += "<h3 style='text-align: center;'>Puntos Pagados: 50</h3>";
        emailBody += "<center><p style='margin-left: 10%; margin-right: 10%;'>Cod Transaccion: " + response.get("token") + "</p></center>";
        emailBody += "<center><p style='margin-left: 10%; margin-right: 10%;'>Gracias por su pago </p></center>";
        emailBody += "<center><div style='width: 100%;'>";
        emailBody += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        emailBody += "<center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>";
        emailBody += "</div></center>";
        emailBody += "<center><div style='width: 100%;'>";
        emailBody += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        emailBody += "</div></center>";
        emailBody += "</div></body></html>";

        return emailBody;
    }
     */


    private String buildEmailBody(Map<String, String> response, BigDecimal riberaPoints) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Lima"));
        String dateFormatted = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a"));

        String emailBody = "<html><head><title></title></head><body style='color:black'>";
        emailBody += "<div style='width: 100%;'>";
        emailBody += "<div style='display:flex;'>";
        emailBody += "<img style='width: 100%;' src='http://www.inresorts.club/Views/img/fondo.png' />";
        emailBody += "</div>";
        emailBody += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Voucher de Pago con Puntos</h1>";
        emailBody += "<h3 style='text-align: center;'>Fecha de Pago: " + dateFormatted + "</h3>";
        emailBody += "<h3 style='text-align: center;'>Puntos Pagados: " + riberaPoints.toString() + "</h3>";
        emailBody += "<center><p style='margin-left: 10%; margin-right: 10%;'>Cod Transaccion: " + response.get("token") + "</p></center>";
        emailBody += "<center><p style='margin-left: 10%; margin-right: 10%;'>Gracias por su pago </p></center>";
        emailBody += "<center><div style='width: 100%;'>";
        emailBody += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        emailBody += "<center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>";
        emailBody += "</div></center>";
        emailBody += "<center><div style='width: 100%;'>";
        emailBody += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        emailBody += "</div></center>";
        emailBody += "</div></body></html>";

        return emailBody;
    }

    /*
    private String buildEmailBody(Map<String, String> response) {
        String emailBody = "<html><body>";
        emailBody += "<h1>Token: " + response.get("token") + "</h1>";
        emailBody += "<p>Link de pago: " + response.get("linkPayment") + "</p>";
        emailBody += "<p>Mensaje: " + response.get("mensaje") + "</p>";
        emailBody += "</body></html>";
        return emailBody;
    }
 */
}