package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.WithdrawRequestDTO;
import com.proriberaapp.ribera.Domain.entities.WithdrawalRequestEntity;
import com.proriberaapp.ribera.services.client.WithdrawalService;
import com.proriberaapp.ribera.Infraestructure.repository.WithdrawalRequestRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final UserClientRepository userClientRepository;
    private final EmailService emailService;
    private final WalletRepository walletRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<WithdrawalRequestEntity> createWithdrawalRequest(WithdrawRequestDTO requestDTO, Integer userId) {
        return walletRepository.findById(requestDTO.getWalletId())
            .flatMap(wallet -> {
                if (wallet.getBalance().compareTo(requestDTO.getAmount()) < 0) {
                    return Mono.error(new RuntimeException("Saldo insuficiente para el retiro."));
                }
                WithdrawalRequestEntity newRequest = WithdrawalRequestEntity.builder()
                        .operationNumber(generateOperationNumber())
                        .userId(userId)
                        .walletId(requestDTO.getWalletId())
                        .amount(requestDTO.getAmount())
                        .destinationBank(requestDTO.getBank())
                        .accountNumber(requestDTO.getAccountNumber())
                        .accountHolderName(requestDTO.getHolderFirstName() + " " + requestDTO.getHolderLastName())
                        .accountHolderDocument(requestDTO.getDocumentNumber())
                        .country(requestDTO.getCountry())
                        .documentType(requestDTO.getDocumentType())
                        .observation(null)
                        .status("PENDING")
                        .creationDate(Timestamp.from(Instant.now()))
                        .updateDate(Timestamp.from(Instant.now()))
                        .build();
                return withdrawalRequestRepository.save(newRequest)
                    .flatMap(savedRequest ->
                        userClientRepository.findById(userId)
                            .flatMap(user -> {
                                String nombreTitular = user.getLastName() + " " + user.getFirstName();
                                String html = generarHtmlConstanciaRetiro(savedRequest, user, nombreTitular);
                                return emailService.sendEmail(
                                    user.getEmail(),
                                    "Constancia de operación - Transferencia a otro banco",
                                    html
                                ).thenReturn(savedRequest);
                            })
                            .switchIfEmpty(Mono.just(savedRequest))
                    );
            })
            .as(transactionalOperator::transactional);
    }

    private String generarHtmlConstanciaRetiro(WithdrawalRequestEntity retiro, UserClientEntity user, String nombreTitularWallet) {
        String monto = String.format("%.2f", retiro.getAmount());
        String fecha = retiro.getCreationDate().toLocalDateTime().toLocalDate().toString();
        return String.format("""
        <!DOCTYPE html>
        <html lang=\"es\">
        <head>
            <meta charset=\"UTF-8\">
            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
            <title>Constancia de operación - Transferencia a otro banco</title>
            <style>
                body { background: #f7f7fa; margin: 0; padding: 0; font-family: Arial, sans-serif; }
                .main-container { max-width: 600px; margin: 40px auto 0 auto; }
                .box { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; }
                .header { background: #014421; padding: 32px 0 24px 0; text-align: center; }
                .header img { height: 60px; }
                .content { padding: 40px 32px 32px 32px; }
                .content h2 { color: #014421; font-size: 22px; margin-bottom: 8px; }
                .content p { margin: 0 0 24px 0; color: #222; }
                .transfer-table { width: 100%%; background: #f7f7fa; border-radius: 8px; margin-bottom: 24px; border-collapse: separate; border-spacing: 0; }
                .transfer-table th, .transfer-table td { padding: 10px 16px; text-align: left; }
                .transfer-table th { color: #888; font-weight: 400; font-size: 14px; }
                .transfer-table td { color: #222; font-size: 15px; }
                .transfer-table .section-title { font-weight: bold; font-size: 15px; padding-top: 18px; }
                .footer { padding: 0 32px 32px 32px; font-size: 13px; color: #888; }
                .help-box, .confidential-box { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); margin: 24px auto 0 auto; max-width: 600px; padding: 24px 32px; }
                .help { font-size: 13px; color: #014421; text-align: left; }
                .help a { color: #014421; text-decoration: underline; }
                .confidential { font-size: 13px; color: #384860; text-align: left; }
                .confidential b { color: #222; }
            </style>
        </head>
        <body>
            <div class="main-container">
                <div class="box">
                    <div class="header">
                        <img src="https://cieneguillariberadelrio.com/assets/images/logo-ribera.png" alt="Ribera Club">
                    </div>
                    <div class="content">
                        <h2>Constancia de operación</h2>
                        <p>Estimado/a %s %s,</p>
                        <p>Le confirmamos que su solicitud de transferencia a otro banco ha sido procesada exitosamente.</p>
                        
                        <table class="transfer-table">
                            <tr>
                                <th>Número de operación</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>Fecha de operación</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>Titular de la cuenta origen</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>Monto transferido</th>
                                <td>S/ %s</td>
                            </tr>
                            <tr>
                                <th>Titular de la cuenta destino</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>Número de cuenta destino</th>
                                <td>%s</td>
                            </tr>
                            <tr>
                                <th>Banco destino</th>
                                <td>%s</td>
                            </tr>
                        </table>
                        
                        <p>La transferencia será procesada según los horarios de su banco destino. Por favor, tenga en cuenta que los tiempos de acreditación pueden variar según la entidad financiera.</p>
                    </div>
                </div>
                
                <div class="help-box">
                    <div class="help">
                        <b>¿Necesita ayuda?</b><br>
                        Si tiene alguna consulta sobre esta operación, puede contactarnos a través de nuestro <a href="https://cieneguillariberadelrio.com/contacto">formulario de contacto</a> o llamando a nuestra línea de atención al cliente.
                    </div>
                </div>
                
                <div class="confidential-box">
                    <div class="confidential">
                        <b>Aviso de confidencialidad</b><br>
                        Este correo electrónico y/o el material adjunto es para uso exclusivo de la persona o entidad a la que expresamente se le ha enviado, y puede contener información confidencial o material privilegiado. Si usted no es el destinatario legítimo del mismo, por favor repórtelo inmediatamente al remitente del correo y bórrelo. Cualquier revisión, retransmisión, difusión o cualquier otro uso de este correo, por personas o entidades distintas a las del destinatario legítimo, queda expresamente prohibido. Este correo electrónico no pretende ni debe ser considerado como constitutivo de ninguna relación legal, contractual o de otra índole similar, en consecuencia, no genera obligación alguna a cargo de su emisor o su representada. En tal sentido, nada de lo señalado en esta comunicación o en sus anexos podrá ser interpretado como una recomendación sobre los riesgos o ventajas económicas, legales, contables o tributarias, o sobre las consecuencias de realizar o no determinada transacción.
                    </div>
                </div>
            </div>
        </body>
        </html>
        """,
        user.getLastName(), user.getFirstName(),
        retiro.getOperationNumber(),
        fecha,
        nombreTitularWallet,
        monto,
        retiro.getAccountHolderName(),
        retiro.getAccountNumber(),
        retiro.getDestinationBank()
    );
    }

    /**
     * Genera un número de operación único con formato YYYYMMDD-XXXX
     * @return Número de operación único
     */
    private String generateOperationNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return datePart + "-" + randomPart;
    }
} 