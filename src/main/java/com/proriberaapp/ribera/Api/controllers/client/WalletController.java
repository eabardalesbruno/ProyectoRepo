package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.services.client.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;


    @PostMapping("/create-wallet")
    public Mono<Integer> createWallet(@RequestParam Integer userClientId, @RequestParam Integer currencyId) {
        return walletService.createWalletUsuario(userClientId, currencyId)
                .map(walletEntity -> walletEntity.getWalletId());
    }

    @PostMapping("/create-wallet-promoter")
    public Mono<Integer> createWalletPromoter(@RequestParam Integer userPromoterId, @RequestParam Integer currencyId) {
        return walletService.createWalletPromoter(userPromoterId, currencyId)
                .map(walletEntity -> walletEntity.getWalletId());
    }

    @PostMapping("/transfer")
    public Mono<ResponseEntity<WalletTransactionEntity>> transfer(@RequestParam Integer walletIdOrigin, @RequestParam(required = false) Integer walletIdDestiny, @RequestParam(required = false) String emailDestiny, @RequestParam(required = false) String documentNumber, @RequestParam BigDecimal amount) {
        return walletService.makeTransfer(walletIdOrigin, walletIdDestiny, emailDestiny, documentNumber, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));

    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<WalletTransactionEntity>> withdrawal(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletService.makeWithdrawal(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/deposit")
    public Mono<ResponseEntity<WalletTransactionEntity>> deposit(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletService.makeDeposit(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/payment")
    public Mono<ResponseEntity<WalletTransactionEntity>> payment(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletService.makePayment(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/recharge")
    public Mono<ResponseEntity<WalletTransactionEntity>> recharge(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletService.makeRecharge(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

}
