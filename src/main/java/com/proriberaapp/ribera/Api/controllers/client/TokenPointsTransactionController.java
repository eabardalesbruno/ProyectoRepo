package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.CreateTokenRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.PointsRequest;
import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.S3UploadService;
import com.proriberaapp.ribera.services.client.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.File;
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

                    try {
                        String pdfFileName = token.getCodigoToken() + ".pdf";
                        File pdfFile = pdfGeneratorService.generatePDFFile(buildEmailBody(response), pdfFileName);

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
                                    File pdfFile = pdfGeneratorService.generatePDFFile(buildEmailBody(response), pdfFileName);

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
    private String buildEmailBody(Map<String, String> response) {
        /*
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
         */

        String emailBody = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Voucher de Pago con Puntos</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: black;\n" +
                "            background-color: white;\n" +
                "        }\n" +
                "        .header {\n" +
                "            width: 100%;\n" +
                "            position: relative;\n" +
                "            background-color: white;\n" +
                "            padding: 20px 0;\n" +
                "        }\n" +
                "        .logo-left {\n" +
                "            width: 50px;\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            left: 10px;\n" +
                "        }\n" +
                "        .banner {\n" +
                "            width: 100%;\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 500px;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: left;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .content h3 {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: block;\n" +
                "            width: 200px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 10px;\n" +
                "            background-color: green;\n" +
                "            color: white;\n" +
                "            text-align: center;\n" +
                "            border-radius: 5px;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .help-section {\n" +
                "            width: 500px;\n" +
                "            background-color: #f4f4f4;\n" +
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
                "        <!-- Encabezado con logo -->\n" +
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Banner\">\n" +
                "\n" +
                "    <!-- Contenedor con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Voucher de Pago con Puntos</h1>\n" +
                "            <h3 style='text-align: center;'>Fecha de Pago: 04-08-2024 04:55:00 pm</h3>\n" +
                "            <h3 style='text-align: center;'>Puntos Pagados: 50</h3>\n" +
                "            <center><p style='margin-left: 10%; margin-right: 10%;'>Cod Transaccion: " + response.get("token") + "</p></center>\n" +
                "            <center><p style='margin-left: 10%; margin-right: 10%;'>Gracias por su pago</p></center>\n" +
                "            <center><div style='width: 100%;'>\n" +
                "                <p style='margin-left: 10%; margin-right: 10%;'></p>\n" +
                "                <center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>\n" +
                "            </div></center>\n" +
                "            <center><div style='width: 100%;'>\n" +
                "                <p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>\n" +
                "            </div></center>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Sección de ayuda -->\n" +
                "    <div class=\"help-section\">\n" +
                "        <h3>¿Necesitas ayuda?</h3>\n" +
                "        <p>Envie sus comentarios e información de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

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