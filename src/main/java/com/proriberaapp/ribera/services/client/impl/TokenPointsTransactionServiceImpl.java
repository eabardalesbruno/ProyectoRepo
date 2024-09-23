package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.Infraestructure.repository.PartnerPointsRepository;
import com.proriberaapp.ribera.Infraestructure.repository.TokenPointsTransactionRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.PDFGeneratorService;
import com.proriberaapp.ribera.services.S3UploadService;
import com.proriberaapp.ribera.services.client.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                                String linkPayment = "https://cieneguillariberadelrio.online/login";
                                String subject = "Pagos";
                                String body = generateEmailBody(email, linkPayment);
                                return emailService.sendEmail(email, subject, body)
                                        .thenReturn(savedToken);
                            });
                });
    }

    private String generateEmailBody(String email, String linkPayment) {
        String body = "<html>\n" +
                "<head>\n" +
                "    <title>Proceso de Pago</title>\n" +
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
                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n" +
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
                "        .content h2, .content h3, .content p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            background: #0d80ea;\n" +
                "            border-radius: 10px;\n" +
                "            width: 158px;\n" +
                "            height: 30px;\n" +
                "            font-size: 16px;\n" +
                "            color: white;\n" +
                "            font-weight: bold;\n" +
                "            padding: 4px;\n" +
                "            padding-top: 10px;\n" +
                "            cursor: pointer;\n" +
                "            text-align: center;\n" +
                "            margin: 23px auto;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <!-- Encabezado con logos -->\n" +
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Bienvenido " + email + "</h1>\n" +
                "            <h3 style='text-align: center;'>Reserva</h3>\n" +
                "            <h3 style='text-align: center;'>Descripcion: Reserva de Habitacion</h3>\n" +
                "            <h2 style='text-align: center;'>Muy pronto formarás parte de la familia inResorts. Estamos a la espera de que procese su pago a través del siguiente portal</h2>\n" +
                "            <center><p style='margin-left: 10%; margin-right: 10%;'>Puede procesar el pago cuando se sienta preparado, pero recuerde que tienes 24 Horas para realizarlo</p></center>\n" +
                "            <center>\n" +
                "                <div style='width: 100%'>\n" +
                "                    <p style='margin-left: 10%; margin-right: 10%;'>Click en el botón para pagar</p>\n" +
                "                    <a style='text-decoration: none;' href='" + linkPayment + "'>\n" +
                "                        <div class=\"button\">Validar pago</div>\n" +
                "                    </a>\n" +
                "                </div>\n" +
                "            </center>\n" +
                "            <center>\n" +
                "                <div style='width: 100%'>\n" +
                "                    <p style='margin-left: 10%; margin-right: 10%;'></p>\n" +
                "                    <center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>\n" +
                "                </div>\n" +
                "            </center>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return body;
    }
}
