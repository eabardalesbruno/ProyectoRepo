package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final UserClientRepository userClientRepository;
    private final EmailService emailService;
    
    @Value("${app.email.withdrawal.subject}")
    private String withdrawalEmailSubject;
    
    @Value("${app.email.company.name}")
    private String companyName;
    
    @Value("${app.email.currency.symbol}")
    private String currencySymbol;

    // Endpoint /user/{userId}/email ya existe en InternalController (admin)
    // No duplicamos funcionalidad
    
    /**
     * Endpoint para enviar correo de solicitud de retiro
     * El microservicio llama este endpoint cuando se hace una solicitud de retiro
     */
    @PostMapping("/withdrawal/notify")
    public Mono<ResponseEntity<String>> notifyWithdrawalRequest(@RequestBody WithdrawalNotification notification) {
        log.info("Recibida notificación de solicitud de retiro para usuario ID: {}", notification.getUserId());
        
        // Obtener los datos completos del usuario usando su ID
        return userClientRepository.findById(notification.getUserId())
                .flatMap(user -> {
                    if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                        log.info("Datos del usuario encontrados - ID: {}, Email: {}, Nombre: {} {}", 
                                notification.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName());
                        
                        // Construir el email con los datos del usuario y la notificación
                        String emailBody = generateWithdrawalEmailBody(user, notification);
                        
                        // Enviar el correo
                        return emailService.sendEmail(user.getEmail(), withdrawalEmailSubject, emailBody)
                                .then(Mono.fromCallable(() -> {
                                    log.info("Correo de solicitud de retiro enviado exitosamente para usuario: {} {} ({})", 
                                            user.getFirstName(), user.getLastName(), user.getEmail());
                                    return ResponseEntity.ok("Correo enviado exitosamente");
                                }));
                    } else {
                        log.warn("Usuario ID {} no tiene email configurado", notification.getUserId());
                        return Mono.just(ResponseEntity.badRequest().body("Usuario no tiene email configurado"));
                    }
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    log.warn("Usuario ID {} no encontrado", notification.getUserId());
                    return ResponseEntity.status(404).body("Usuario no encontrado");
                }))
                .onErrorResume(error -> {
                    log.error("Error procesando notificación de retiro para usuario ID {}: {}", 
                            notification.getUserId(), error.getMessage());
                    return Mono.just(ResponseEntity.status(500).body("Error procesando notificación"));
                });
    }
    
    /**
     * Endpoint para enviar correo de rechazo de retiro
     * El microservicio llama este endpoint cuando se rechaza una solicitud de retiro
     */
    @PostMapping("/withdrawal/reject")
    public Mono<ResponseEntity<String>> notifyWithdrawalRejection(@RequestBody WithdrawalRejectionNotification notification) {
        log.info("Recibida notificación de rechazo de retiro para usuario ID: {}", notification.getUserId());
        
        // Obtener los datos completos del usuario usando su ID
        return userClientRepository.findById(notification.getUserId())
                .flatMap(user -> {
                    if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                        log.info("Datos del usuario encontrados - ID: {}, Email: {}, Nombre: {} {}", 
                                notification.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName());
                        
                        // Construir el email de rechazo
                        String emailBody = generateRejectionEmailBody(user, notification);
                        
                        // Enviar el correo
                        return emailService.sendEmail(user.getEmail(), "Solicitud de Retiro Rechazada", emailBody)
                                .then(Mono.fromCallable(() -> {
                                    log.info("Correo de rechazo de retiro enviado exitosamente para usuario: {} {} ({})", 
                                            user.getFirstName(), user.getLastName(), user.getEmail());
                                    return ResponseEntity.ok("Correo de rechazo enviado exitosamente");
                                }));
                    } else {
                        log.warn("Usuario ID {} no tiene email configurado", notification.getUserId());
                        return Mono.just(ResponseEntity.badRequest().body("Usuario no tiene email configurado"));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario ID {} no encontrado", notification.getUserId());
                    return Mono.just(ResponseEntity.status(404).body("Usuario no encontrado"));
                }))
                .onErrorResume(error -> {
                    log.error("Error procesando notificación de rechazo para usuario ID {}: {}", 
                            notification.getUserId(), error.getMessage());
                    return Mono.just(ResponseEntity.status(500).body("Error procesando notificación"));
                });
    }
    
    private String generateWithdrawalEmailBody(UserClientEntity user, WithdrawalNotification notification) {
        // Usar los datos del usuario directamente
        String fullName = user.getFirstName() + " " + user.getLastName();
        String holderFullName = notification.getHolderFirstName() + " " + notification.getHolderLastName();
        
        // Generar fecha actual formateada
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm a");
        String requestDate = now.format(formatter);
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Solicitud de Retiro</title>
            </head>
            <body style="margin: 0; padding: 20px; font-family: Arial, sans-serif; background-color:rgb(255, 255, 255);">
                
                <!-- Contenedor principal gris -->
                <div style="max-width: 600px; margin: 0 auto; background-color: #e8e8e8; padding: 20px; border-radius: 8px;">
                    
                    <!-- Header con logo verde y contenido blanco -->
                    <div style="background-color: white; border-radius: 8px; overflow: hidden;">
                        
                        <!-- Sección verde del logo -->
                        <div style="background-color: #2d5c3e; padding: 30px; text-align: center;">
                            <div style="color: white; font-size: 24px; font-weight: bold; letter-spacing: 2px;">
                                ★★★<br>
                                RIBERA DEL RÍO<br>
                                <span style="font-size: 12px; font-weight: normal;">CLUB & RESORT</span>
                            </div>
                        </div>
                        
                        <!-- Sección blanca con todo el contenido -->
                        <div style="padding: 30px; background-color: white;">
                            <h2 style="margin: 0 0 10px 0; color: #333; font-size: 18px;">Estimado(a), %s</h2>
                            <p style="margin: 0 0 25px 0; color: #666; font-size: 14px; line-height: 1.5;">
                                Esta es la constancia de su transferencia
                            </p>
                            
                            <!-- Datos de la transferencia dentro de la caja blanca -->
                            <div style="background-color: #f8f8f8; padding: 20px; border-radius: 6px; margin-bottom: 25px;">
                                <h3 style="margin: 0 0 15px 0; color: #333; font-size: 16px; font-weight: bold;">
                                    Transferencia a otro banco
                                </h3>
                                
                                <p style="margin: 0 0 5px 0; color: #333; font-size: 14px;">
                                    <strong>N° de operación:</strong> %s
                                </p>
                                
                                <p style="margin: 0 0 15px 0; color: #333; font-size: 12px;">
                                    Fecha de solicitud: %s
                                </p>
                                
                                <table style="width: 100%%; border-collapse: collapse; margin-top: 15px;">
                                    <tr>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; vertical-align: top; width: 40%%;">
                                            <strong>Cuenta origen:</strong>
                                        </td>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; text-align: right;">
                                            Wallet - %s
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; vertical-align: top;">
                                            <strong>Monto enviado:</strong>
                                        </td>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; text-align: right; font-weight: bold;">
                                            %s %.2f
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; vertical-align: top;">
                                            <strong>Total cargado:</strong>
                                        </td>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; text-align: right; font-weight: bold;">
                                            %s %.2f
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; vertical-align: top;">
                                            <strong>Cuenta destino:</strong>
                                        </td>
                                        <td style="padding: 8px 0; color: #333; font-size: 14px; text-align: right;">
                                            %s<br>
                                            <span style="font-size: 12px; color: #666;">%s</span><br>
                                            <span style="font-size: 12px; color: #666;">%s</span>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            
                            <!-- Muchas gracias dentro de la caja blanca -->
                            <p style="margin: 0 0 5px 0; color: #333; font-size: 14px; text-align: center;">
                                Muchas gracias,
                            </p>
                            <p style="margin: 0; color: #666; font-size: 12px; text-align: center;">
                                %s
                            </p>
                        </div>
                    </div>
                    
                    <!-- Contenedor blanco para Aviso de confidencialidad -->
                    <div style="background-color: white; border-radius: 8px; padding: 20px; margin-top: 20px;">
                        <h3 style="margin: 0 0 15px 0; color: #333; font-size: 16px; font-weight: bold;">
                            Aviso de confidencialidad
                        </h3>
                        <p style="margin: 0; color: #666; font-size: 12px; line-height: 1.6; text-align: justify;">
                            Este correo electrónico y/o el material adjunto es para uso exclusivo de la persona o 
                            entidad a la que expresamente se le ha enviado, y puede contener información 
                            confidencial o material privilegiado. Si usted no es el destinatario legítimo del mismo, 
                            por favor repórtelo inmediatamente al remitente del correo y bórrelo. 
                            Cualquier revisión, retransmisión, difusión o cualquier otro uso de este correo, por 
                            personas o entidades distintas a las del destinatario legítimo, queda expresamente 
                            prohibido. Este correo electrónico no pretende ni debe ser considerado como 
                            constitutivo de ninguna relación legal, contractual o de otra índole similar, en 
                            consecuencia, no genera obligación alguna a cargo de su emisor o su representada. 
                            En tal sentido, nada de lo señalado en esta comunicación o en sus anexos podrá ser 
                            interpretado como una recomendación sobre los riesgos o ventajas económicas, 
                            legales, contables o tributarias, o sobre las consecuencias de realizar o no determinada 
                            transacción.
                        </p>
                    </div>
                    
                    <!-- Contenedor blanco para ¿Necesitas ayuda? -->
                    <div style="background-color: white; border-radius: 8px; padding: 20px; margin-top: 20px;">
                        <h3 style="margin: 0 0 15px 0; color: #333; font-size: 16px; font-weight: bold;">
                            ¿Necesitas ayuda?
                        </h3>
                        <p style="margin: 0; color: #666; font-size: 12px; line-height: 1.6;">
                            Envía tus comentarios o información de errores a 
                            <a href="mailto:informeyreservas@cieneguillariberadelrio.com" style="color: #0066cc; text-decoration: none;">
                                informeyreservas@cieneguillariberadelrio.com
                            </a>
                        </p>
                    </div>
                    
                </div>
                
                <!-- Pie de página fuera del contenedor gris -->
                <div style="max-width: 600px; margin: 20px auto 0; text-align: center;">
                    <p style="margin: 0; color: #666; font-size: 12px;">
                        Nota: Mensaje automático, por favor no responder.
                    </p>
                </div>
                
            </body>
            </html>
            """, 
            fullName, // Estimado(a), [Nombre] [Apellido]
            notification.getOperationCode(), // N° de operación del MS
            requestDate, // Fecha de solicitud generada automáticamente
            fullName, // Wallet - [Nombre] [Apellido]
            currencySymbol, // Símbolo de moneda
            notification.getAmount(), // Monto enviado
            currencySymbol, // Símbolo de moneda
            notification.getAmount(), // Total cargado (mismo monto)
            holderFullName, // Nombre titular cuenta destino
            notification.getAccountNumber(), // CCI
            notification.getBank(), // Banco
            companyName // Nombre de la empresa
        );
    }
    
    private String generateRejectionEmailBody(UserClientEntity user, WithdrawalRejectionNotification notification) {
        // Usar los datos del usuario directamente
        String fullName = user.getFirstName() + " " + user.getLastName();
        
        return String.format("""
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Solicitud de Retiro Rechazada</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 20px;
                        background-color: #f4f4f4;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #e8e8e8;
                        padding: 20px;
                        border-radius: 8px;
                    }
                    .header {
                        background-color: #2d5a3d;
                        color: white;
                        padding: 20px;
                        text-align: center;
                        border-radius: 8px 8px 0 0;
                        margin: -20px -20px 20px -20px;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 18px;
                        font-weight: bold;
                    }
                    .content {
                        background-color: white;
                        padding: 20px;
                        border-radius: 8px;
                        margin-bottom: 20px;
                    }
                    .rejection-info {
                        background-color: #f8f9fa;
                        padding: 15px;
                        border-radius: 5px;
                        margin: 15px 0;
                        border-left: 4px solid #dc3545;
                    }
                    .rejection-reason {
                        color: #dc3545;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                    .help-box {
                        background-color: white;
                        padding: 15px;
                        border-radius: 8px;
                        margin-bottom: 15px;
                        border: 1px solid #ddd;
                    }
                    .confidential-box {
                        background-color: white;
                        padding: 15px;
                        border-radius: 8px;
                        border: 1px solid #ddd;
                    }
                    .help, .confidential {
                        font-size: 12px;
                        color: #666;
                        line-height: 1.4;
                    }
                    a {
                        color: #0066cc;
                        text-decoration: none;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 20px;
                        color: #666;
                        font-size: 12px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>RIBERA DEL RÍO ★★★ CLUB & RESORT</h1>
                    </div>
                    
                    <div class="content">
                        <p><strong>Estimado(a), %s</strong></p>
                        <p>Lo sentimos, la solicitud de retiro fue rechazada.</p>
                        
                        <div class="rejection-info">
                            <div class="rejection-reason">Motivo de rechazo:</div>
                            <div>%s</div>
                            %s
                        </div>
                        
                        <p>Muchas gracias.</p>
                        <p><strong>Nota:</strong> Mensaje automático, por favor no responder.</p>
                    </div>
                    
                    <div class="help-box">
                        <div class="help">
                            <b>¿Necesitas ayuda?</b><br>
                            Envía tus comentarios o información de errores a 
                            <a href="mailto:informeyreservas@cieneguillariberadelrio.com">
                                informeyreservas@cieneguillariberadelrio.com
                            </a>
                        </div>
                    </div>
                    
                    <div class="confidential-box">
                        <div class="confidential">
                            <b>Aviso de confidencialidad</b><br>
                            Este correo electrónico y/o el material adjunto es para uso exclusivo de la persona o entidad a la que expresamente se le ha enviado, y puede contener información confidencial o material privilegiado. Si usted no es el destinatario legítimo del mismo, por favor repórtelo inmediatamente al remitente del correo y bórrelo.
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """, 
            fullName, // Estimado(a), [Nombre] [Apellido]
            notification.getRejectReason(), // Motivo del rechazo
            notification.getAdditionalMessage() != null && !notification.getAdditionalMessage().trim().isEmpty() 
                ? "<div style=\"margin-top: 10px;\"><strong>Mensaje adicional o solución alternativa:</strong><br>" + notification.getAdditionalMessage() + "</div>" 
                : "" // Mensaje adicional opcional
        );
    }
    
    // DTO para la notificación de solicitud de retiro
    public static class WithdrawalNotification {
        private Integer userId;            // ID del usuario que solicita
        private Double amount;             // Monto solicitado
        private String operationCode;      // Código de operación del MS
        private String bank;              // Banco de destino
        private String accountNumber;      // CCI de la cuenta destino
        private String holderFirstName;    // Nombre del titular de cuenta destino
        private String holderLastName;     // Apellido del titular de cuenta destino
        private String documentType;       // Tipo de documento (info adicional)
        private String documentNumber;     // Número de documento (info adicional)
        
        // Constructores
        public WithdrawalNotification() {}
        
        // Getters y setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        
        public String getOperationCode() { return operationCode; }
        public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
        
        public String getBank() { return bank; }
        public void setBank(String bank) { this.bank = bank; }
        
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
        
        public String getHolderFirstName() { return holderFirstName; }
        public void setHolderFirstName(String holderFirstName) { this.holderFirstName = holderFirstName; }
        
        public String getHolderLastName() { return holderLastName; }
        public void setHolderLastName(String holderLastName) { this.holderLastName = holderLastName; }
        
        public String getDocumentType() { return documentType; }
        public void setDocumentType(String documentType) { this.documentType = documentType; }
        
        public String getDocumentNumber() { return documentNumber; }
        public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    }
    
    // DTO para la notificación de rechazo de retiro
    public static class WithdrawalRejectionNotification {
        private Integer userId;            // ID del usuario que solicitó
        private String operationCode;      // Código de operación del MS
        private Double amount;             // Monto que se rechazó
        private String rejectReason;       // Motivo del rechazo
        private String additionalMessage;  // Mensaje adicional opcional
        private String requestDate;        // Fecha de la solicitud original
        
        // Constructores
        public WithdrawalRejectionNotification() {}
        
        // Getters y setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        
        public String getOperationCode() { return operationCode; }
        public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
        
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        
        public String getRejectReason() { return rejectReason; }
        public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
        
        public String getAdditionalMessage() { return additionalMessage; }
        public void setAdditionalMessage(String additionalMessage) { this.additionalMessage = additionalMessage; }
        
        public String getRequestDate() { return requestDate; }
        public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    }
} 