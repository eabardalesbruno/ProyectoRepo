package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.TokenEmailRequest;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.PaymentTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/token-email")
public class TokenEmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private PaymentTokenService paymentTokenService;

    @PostMapping("/send")
    public Mono<ResponseEntity<Map<String, String>>> generateTokenAndSendEmail(@RequestBody TokenEmailRequest request) {
        /*
         * return paymentTokenService.generateAndSaveToken(request.getBookingId(),
         * request.getUserClientId())
         * .flatMap(token -> {
         * String subject = "Pagos";
         * String linkPayment =
         * "https://cieneguillariberadelrio.online/payment-validation?token=" + token;
         * String body = "<html><head><title></title></head><body style='color:black'>";
         * body += "<div style='width: 100%'>";
         * body += "<div style='display:flex;'>";
         * body += "</div>";
         * body +=
         * "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>"
         * ;
         * body +=
         * "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
         * + "Bienvenido " + request.getEmail() + "</h1>";
         * body +=
         * "<h3 style='text-align: center;'>Producto por Adquirir: Reserva</h3>";
         * body +=
         * "<h3 style='text-align: center;'>Descripcion: Reserva de Habitacion</h3>";
         * body +=
         * "<h2 style='text-align: center;'>Muy pronto formaras parte de la familia inResorts. Estamos a la espera de que procese su pago a traves del siguiente portal</h2>"
         * ;
         * body +=
         * "<center><p style='margin-left: 10%; margin-right: 10%;'>Puede procesar el pago cuando se sienta preparado, pero recuerde que tienes 24 Horas para realizarlo</p></center>"
         * ;
         * body += "<center><div style='width: 100%'>";
         * body +=
         * "<p style='margin-left: 10%; margin-right: 10%;'>Click en el boton para pagar</p>"
         * ;
         * body += "<a style='text-decoration: none;' href='" + linkPayment + "'>";
         * body +=
         * "<center><div style='background: #0d80ea; border-radius: 10px; width: 158px; height: 30px; font-size: 16px; color: white; font-weight: bold; padding: 4px; padding-top: 10px; cursor: pointer; text-align: center; margin: 23px;'>Validar pago<div></center>"
         * ;
         * body += "</a></div></center>";
         * body += "<center><div style='width: 100%'>";
         * body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
         * body +=
         * "<center>Recuerde que el pago lo puede realizar mediante los medios de pagos que se encuentran en el portal.</center>"
         * ;
         * body += "</div></center>";
         * body += "<center><div style='width: 100%'>";
         * body +=
         * "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>"
         * ;
         * body += "</div></center>";
         * body += "</div></center>";
         * body += "</body></html>";
         * 
         * return emailService.sendEmail(request.getEmail(), subject, body)
         * .thenReturn(linkPayment);
         * })
         * .map(linkPayment -> {
         * Map<String, String> response = new HashMap<>();
         * response.put("token", linkPayment.split("token=")[1]);
         * response.put("linkPayment", linkPayment);
         * response.put("mensaje", "Enviado");
         * return ResponseEntity.ok(response);
         * })
         * .onErrorResume(e -> {
         * Map<String, String> response = new HashMap<>();
         * return paymentTokenService.generateAndSaveToken(request.getBookingId(),
         * request.getUserClientId())
         * .map(token -> {
         * String linkPayment =
         * "https://ribera-dev.inclub.world/payment-validation?token=" + token;
         * response.put("token", token);
         * response.put("linkPayment", linkPayment);
         * response.put("message", "Enviado");
         * return ResponseEntity.status(200).body(response);
         * });
         * });
         */
        return paymentTokenService
                .generateAndSaveToken(request.getBookingId().intValue(),
                        request.getEmail())
                .map(token -> {
                    String linkPayment = "https://cieneguillariberadelrio.online/payment-validation?token=" + token;
                    Map<String, String> response = new HashMap<>();
                    response.put("token", linkPayment.split("token=")[1]);
                    response.put("linkPayment", linkPayment);
                    response.put("mensaje", "Enviado");
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    System.out.println("ERRROR" + e);
                    Map<String, String> response = new HashMap<>();
                    return paymentTokenService.generateAndSaveToken(request.getBookingId(), request.getUserClientId())
                            .map(token -> {

                                String linkPayment = "https://ribera-dev.inclub.world/payment-validation?token="
                                        + token;
                                response.put("token", token);
                                response.put("linkPayment", linkPayment);
                                response.put("message", "Enviado");
                                return ResponseEntity.status(200).body(response);
                            });
                });
    }
}
