package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.WithdrawalRequestEntity;
import com.proriberaapp.ribera.Infraestructure.repository.WithdrawalRequestRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletTransactionRepository;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.enums.Permission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/manager/wallet/withdrawals")
@RequiredArgsConstructor
@Slf4j
public class ManagerWalletWithdrawalController {
    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final UserClientRepository userClientRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;

    @PatchMapping("/request/{id}/approve")
    public Mono<ResponseEntity<WithdrawalRequestEntity>> approveWithdrawal(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token) {
        
        Integer adminId = jwtProvider.getIdFromToken(token);
        List<Permission> permissions = jwtProvider.getPermissionsFromToken(token);
        
        if (!permissions.contains(Permission.UPDATE)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para aprobar retiros"));
        }
        
        log.info("Admin {} está aprobando el retiro {}", adminId, id);
        
        return withdrawalRequestRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de retiro no encontrada")))
                .flatMap(withdrawalRequest -> {
                    if (!"PENDING".equals(withdrawalRequest.getStatus())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud no está pendiente"));
                    }
                    
                    return userClientRepository.findById(withdrawalRequest.getUserId())
                            .flatMap(userClient -> walletRepository.findByUserClientId(userClient.getUserClientId())
                                    .flatMap(wallet -> {
                                        if (wallet.getBalance().compareTo(withdrawalRequest.getAmount()) < 0) {
                                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente"));
                                        }
                                        
                                        // Actualizar saldo de la wallet
                                        BigDecimal newBalance = wallet.getBalance().subtract(withdrawalRequest.getAmount());
                                        wallet.setBalance(newBalance);
                                        
                                        // Actualizar estado de la solicitud
                                        withdrawalRequest.setStatus("APPROVED");
                                        withdrawalRequest.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                                        
                                        return walletRepository.save(wallet)
                                                .then(withdrawalRequestRepository.save(withdrawalRequest))
                                                .flatMap(savedRequest -> {
                                                    // Enviar email de confirmación
                                                    String emailBody = generateApprovalEmailBody(userClient, withdrawalRequest);
                                                    return emailService.sendEmail(userClient.getEmail(), 
                                                            "Retiro aprobado", emailBody)
                                                            .thenReturn(savedRequest);
                                                })
                                                .map(savedRequest -> ResponseEntity.ok(savedRequest));
                                    }));
                });
    }

    @PatchMapping("/request/{id}/reject")
    public Mono<ResponseEntity<WithdrawalRequestEntity>> rejectWithdrawal(
            @PathVariable Integer id,
            @RequestParam(required = false) String reason,
            @RequestHeader("Authorization") String token) {
        
        Integer adminId = jwtProvider.getIdFromToken(token);
        List<Permission> permissions = jwtProvider.getPermissionsFromToken(token);
        
        if (!permissions.contains(Permission.UPDATE)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para rechazar retiros"));
        }
        
        log.info("Admin {} está rechazando el retiro {} con razón: {}", adminId, id, reason);
        
        return withdrawalRequestRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de retiro no encontrada")))
                .flatMap(withdrawalRequest -> {
                    if (!"PENDING".equals(withdrawalRequest.getStatus())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud no está pendiente"));
                    }
                    
                    return userClientRepository.findById(withdrawalRequest.getUserId())
                            .flatMap(userClient -> {
                                // Actualizar estado de la solicitud
                                withdrawalRequest.setStatus("REJECTED");
                                withdrawalRequest.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                                if (reason != null && !reason.trim().isEmpty()) {
                                    withdrawalRequest.setRejectReason(reason);
                                }
                                
                                return withdrawalRequestRepository.save(withdrawalRequest)
                                        .flatMap(savedRequest -> {
                                            // Enviar email de rechazo
                                            String emailBody = generateRejectionEmailBody(userClient, withdrawalRequest, reason);
                                            return emailService.sendEmail(userClient.getEmail(), 
                                                    "Retiro rechazado", emailBody)
                                                    .thenReturn(savedRequest);
                                        })
                                        .map(savedRequest -> ResponseEntity.ok(savedRequest));
                            });
                });
    }

    @GetMapping("/pending")
    public Mono<ResponseEntity<List<WithdrawalRequestEntity>>> getPendingWithdrawals(
            @RequestHeader("Authorization") String token) {
        
        Integer adminId = jwtProvider.getIdFromToken(token);
        List<Permission> permissions = jwtProvider.getPermissionsFromToken(token);
        
        if (!permissions.contains(Permission.READ)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver solicitudes de retiro"));
        }
        
        return withdrawalRequestRepository.findByStatus("PENDING")
                .collectList()
                .map(withdrawals -> ResponseEntity.ok(withdrawals));
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<List<WithdrawalRequestEntity>>> getAllWithdrawals(
            @RequestHeader("Authorization") String token) {
        
        Integer adminId = jwtProvider.getIdFromToken(token);
        List<Permission> permissions = jwtProvider.getPermissionsFromToken(token);
        
        if (!permissions.contains(Permission.READ)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver solicitudes de retiro"));
        }
        
        return withdrawalRequestRepository.findAll()
                .collectList()
                .map(withdrawals -> ResponseEntity.ok(withdrawals));
    }

    private String generateApprovalEmailBody(UserClientEntity user, WithdrawalRequestEntity withdrawal) {
        return String.format("""
            <html>
            <body>
                <h2>Retiro Aprobado</h2>
                <p>Estimado/a %s %s,</p>
                <p>Su solicitud de retiro por S/ %s ha sido aprobada.</p>
                <p>Detalles de la operación:</p>
                <ul>
                    <li>Número de operación: %s</li>
                    <li>Banco destino: %s</li>
                    <li>Cuenta: %s</li>
                    <li>Fecha de aprobación: %s</li>
                </ul>
                <p>El dinero será transferido a su cuenta bancaria según los horarios de su banco.</p>
            </body>
            </html>
            """, 
            user.getFirstName(), user.getLastName(),
            withdrawal.getAmount(),
            withdrawal.getOperationNumber(),
            withdrawal.getDestinationBank(),
            withdrawal.getAccountNumber(),
            withdrawal.getUpdateDate()
        );
    }

    private String generateRejectionEmailBody(UserClientEntity user, WithdrawalRequestEntity withdrawal, String reason) {
        String reasonText = (reason != null && !reason.trim().isEmpty()) ? reason : "No especificada";
        return String.format("""
            <html>
            <body>
                <h2>Retiro Rechazado</h2>
                <p>Estimado/a %s %s,</p>
                <p>Su solicitud de retiro por S/ %s ha sido rechazada.</p>
                <p>Detalles de la operación:</p>
                <ul>
                    <li>Número de operación: %s</li>
                    <li>Banco destino: %s</li>
                    <li>Cuenta: %s</li>
                    <li>Fecha de rechazo: %s</li>
                </ul>
                <p>Motivo del rechazo: %s</p>
                <p>Si tiene alguna consulta, por favor contacte a soporte.</p>
            </body>
            </html>
            """, 
            user.getFirstName(), user.getLastName(),
            withdrawal.getAmount(),
            withdrawal.getOperationNumber(),
            withdrawal.getDestinationBank(),
            withdrawal.getAccountNumber(),
            withdrawal.getUpdateDate(),
            reasonText
        );
    }
} 