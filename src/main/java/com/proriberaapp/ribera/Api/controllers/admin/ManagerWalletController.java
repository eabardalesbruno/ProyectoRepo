package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.client.dto.WithdrawRequestDTO;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.services.client.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/manager/wallet")
@RequiredArgsConstructor
@Slf4j
public class ManagerWalletController {

    private final WalletTransactionService walletTransactionService;
    private final JwtProvider jwtProvider;

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<WalletTransactionEntity>> withdrawal(
            @RequestBody WithdrawRequestDTO withdrawRequest,
            @RequestHeader("Authorization") String token) {
        
        Integer adminId = jwtProvider.getIdFromToken(token);
        List<Permission> permissions = jwtProvider.getPermissionsFromToken(token);
        
        if (!permissions.contains(Permission.WRITE)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para realizar retiros"));
        }
        
        log.info("Admin {} está realizando un retiro directo de {} desde wallet {}", adminId, withdrawRequest.getAmount(), withdrawRequest.getWalletId());
        
        return walletTransactionService.makeWithdrawal(withdrawRequest)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    /*@PostMapping("/deposit")
    public Mono<ResponseEntity<WalletTransactionEntity>> deposit(
            @RequestParam Integer walletId,
            @RequestParam Integer transactioncatid,
            @RequestParam BigDecimal amount,
            @RequestHeader("Authorization") String token) {
        
        Integer adminId = jwtProvider.getIdFromToken(token);
        List<Permission> permissions = jwtProvider.getPermissionsFromToken(token);
        
        if (!permissions.contains(Permission.WRITE)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para realizar depósitos"));
        }
        
        log.info("Admin {} está realizando un depósito de {} en wallet {}", adminId, amount, walletId);
        
        return walletTransactionService.makeDeposit(walletId, transactioncatid, amount, "Depósito de efectivo")
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    } */
} 