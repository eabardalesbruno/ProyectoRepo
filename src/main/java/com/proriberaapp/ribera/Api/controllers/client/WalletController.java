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

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

/*
    @PostMapping("/create-wallet")
    public Mono<Integer> createWallet(Integer userId, Integer currencyId) {
        return walletService.createWalletUsuario(userId, currencyId)
                .map(walletEntity -> walletEntity.getWalletId());
    }

    @PostMapping("/create-wallet-promoter")
    public Mono<Integer> createWalletPromoter(Integer promoterId, Integer currencyId) {
        return walletService.createWalletPromoter(promoterId, currencyId)
                .map(walletEntity -> walletEntity.getWalletId());
    }
*/

    @PostMapping("/transfer")
    public Mono<ResponseEntity<WalletTransactionEntity>> transfer(@RequestParam Integer walletIdOrigin, @RequestParam Integer walletIdDestiny, @RequestParam String emailDestiny, @RequestParam String nameDestiny, @RequestParam Double amount) {
        return walletService.makeTransfer(walletIdOrigin, walletIdDestiny, emailDestiny, nameDestiny, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));

    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<WalletTransactionEntity>> withdrawal(Integer walletId, Integer transactioncatid, Double amount) {
        return walletService.makeWithdrawal(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/deposit")
    public Mono<ResponseEntity<WalletTransactionEntity>> deposit(Integer walletId, Integer transactioncatid, Double amount) {
        return walletService.makeDeposit(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/payment")
    public Mono<ResponseEntity<WalletTransactionEntity>> payment(Integer walletId, Integer transactioncatid, Double amount) {
        return walletService.makePayment(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/recharge")
    public Mono<ResponseEntity<WalletTransactionEntity>> recharge(Integer walletId, Integer transactioncatid, Double amount) {
        return walletService.makeRecharge(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

}
