package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Api.controllers.dto.CreateTokenRequest;
import com.proriberaapp.ribera.Api.controllers.dto.PointsRequest;
import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.S3UploadService;
import com.proriberaapp.ribera.services.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @Autowired
    public TokenPointsTransactionController(TokenPointsTransactionService tokenPointsTransactionService,
                                            PDFGeneratorService pdfGeneratorService,
                                            S3UploadService s3UploadService) {
        this.tokenPointsTransactionService = tokenPointsTransactionService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.s3UploadService = s3UploadService;
    }

    @PostMapping("/create")
    public Mono<String> createToken(@RequestBody PointsRequest tokenRequest) {
        return tokenPointsTransactionService.createToken(tokenRequest.getPartnerPointId(), tokenRequest.getBookingId())
                .map(token -> token.getCodigoToken());
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<Map<String, String>>> createTokenAndSendEmail(@RequestBody CreateTokenRequest request) {
        return tokenPointsTransactionService.createTokenAndSendEmail(request.getPartnerPointId(), request.getBookingId())
                .flatMap(token -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token.getCodigoToken());
                    response.put("linkPayment", "https://ribera-dev.inclub.world/payment-validation?token=" + token.getCodigoToken());
                    response.put("mensaje", "Enviado");

                    // Generar PDF y subirlo a S3
                    try {
                        byte[] pdfData = pdfGeneratorService.generatePDF(buildEmailBody(response));
                        return s3UploadService.uploadPdf(pdfData)
                                .map(s3Url -> ResponseEntity.ok(response))
                                .defaultIfEmpty(ResponseEntity.badRequest().build());
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
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

    private String buildEmailBody(Map<String, String> response) {
        String emailBody = "<html><body>";
        // Construir el cuerpo del email a partir de los datos de la respuesta
        emailBody += "<h1>Token: " + response.get("token") + "</h1>";
        emailBody += "<p>Link de pago: " + response.get("linkPayment") + "</p>";
        emailBody += "<p>Mensaje: " + response.get("mensaje") + "</p>";
        emailBody += "</body></html>";
        return emailBody;
    }
}
