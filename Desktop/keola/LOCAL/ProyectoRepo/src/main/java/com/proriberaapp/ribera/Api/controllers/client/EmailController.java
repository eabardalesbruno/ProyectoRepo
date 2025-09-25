package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.EmailRequest;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.VerificationCodeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;
    private final VerificationCodeService verificationService;

    @Autowired
    public EmailController(EmailService emailService, VerificationCodeService verificationService) {
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<String>> sendEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody())
                .thenReturn(ResponseEntity.ok("Email enviado"))
                .onErrorReturn(ResponseEntity.status(200).body("Enviado"));
    }


    @PostMapping("/sendCode")
    public Mono<ResponseEntity<String>> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        return verificationService.sendVerificationCode(email)
                .map(message -> {
                    if (message.startsWith("📬")) {
                        return ResponseEntity.ok(message);
                    } else {
                        return ResponseEntity.status(404).body(message);
                    }
                });
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<String>> validateCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        return verificationService.validateCode(email, code)
                .map(valid -> valid ? ResponseEntity.ok("✅ Código válido") : ResponseEntity.status(400).body("❌ Código inválido o expirado"));
    }

    @PostMapping("/resend")
    public Mono<ResponseEntity<String>> resendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return verificationService.resendCode(email)
                .thenReturn(ResponseEntity.ok("🔁 Código reenviado"));
    }

}