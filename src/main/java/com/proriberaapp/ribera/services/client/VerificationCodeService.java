package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeService {

    private final EmailService emailService;

    private final UserClientRepository userClientRepository;

    private final Map<String, String> codes = new ConcurrentHashMap<>();

    @Autowired
    public VerificationCodeService(EmailService emailService, UserClientRepository userClientRepository) {
        this.emailService = emailService;
        this.userClientRepository = userClientRepository;
    }

    private String generateCode() {
        return String.format("%05d", new Random().nextInt(100000));
    }

    public Mono<String> sendVerificationCode(String email) {
        return userClientRepository.findByEmail(email)
                .flatMap(user -> {
                    String code = generateCode();
                    codes.put(email, code);

        String htmlTemplate = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Código de Validación</title>
          <style>
            body { font-family: Arial, sans-serif; background-color: #f5f7fa; margin: 0; padding: 0; }
            .container { max-width: 600px; margin: 30px auto; background-color: white; border-radius: 8px; overflow: hidden; box-shadow: 0 0 5px rgba(0,0,0,0.1); }
            .header { background-color: #02451F; padding: 20px; text-align: center; }
            .header img { max-height: 50px; }
            .content { padding: 30px; text-align: center; }
            .content p { margin: 10px 0; color: #555; font-size: 16px; }
            .code { font-size: 40px; font-weight: bold; color: #2c3e50; margin: 20px 0; }
            .footer { background-color: #f2f4f7; padding: 20px; font-size: 14px; color: #333; text-align: center; }
            .footer a { color: #007bff; text-decoration: none; }
            .footer a:hover { text-decoration: underline; }
            .bold { font-weight: bold; }
          </style>
        </head>
        <body>
          <div class="container">
            <div class="header">
              <img src="https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/Carta/3d76fb2e-6121-4e70-8054-cb8b3052aa74-menu&image.png" alt="Ribera del Río Club Resort">
            </div>
            <div class="content">
              <p>Su código de validación es:</p>
              <div class="code">%s</div>
              <p>Este correo es solo de carácter informativo, no compartir el código entregado. En caso de no haber realizado este movimiento por favor de comunicarse.</p>
              <p>Muchas gracias.</p>
            </div>
            <div class="footer">
              <p class="bold">¿Necesitas ayuda?</p>
              <p>Envíe sus comentarios o información de errores a 
                <a href="mailto:informesyreservas@cieneguillariberadelrio.com">
                  informesyreservas@cieneguillariberadelrio.com
                </a>
              </p>
            </div>
          </div>
        </body>
        </html>
        """;

        String htmlBody = String.format(htmlTemplate, code);
        return emailService.sendEmail(email, "Código de Verificación", htmlBody)
                .thenReturn("📬 El usuario está registrado. Código enviado al correo.");
                }).switchIfEmpty(Mono.just("❌ El usuario no está registrado en la base de datos."));
    }
    
    public Mono<Boolean> validateCode(String email, String inputCode) {
        String savedCode = codes.get(email);
        if (savedCode != null && savedCode.equals(inputCode)) {
            codes.remove(email);
            return Mono.just(true);
        }
        return Mono.just(false);
    }

    public Mono<Void> resendCode(String email) {
        return sendVerificationCode(email).then();
    }
}