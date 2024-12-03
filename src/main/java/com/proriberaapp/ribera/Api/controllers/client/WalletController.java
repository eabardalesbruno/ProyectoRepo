package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.payme.dto.AuthorizationResponse;
import com.proriberaapp.ribera.Api.controllers.payme.dto.TransactionNecessaryResponse;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.services.client.WalletService;
import com.proriberaapp.ribera.services.client.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletTransactionService walletTransactionService;
    private final WalletRepository walletRepository;

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
    public Mono<ResponseEntity<WalletTransactionEntity>> transfer(@RequestParam Integer walletIdOrigin, @RequestParam(required = false) Integer walletIdDestiny, @RequestParam(required = false) String emailDestiny, @RequestParam(required = false) String cardNumber, @RequestParam BigDecimal amount, @RequestParam(required = false) String motiveDescription) {
        return walletTransactionService.makeTransfer(walletIdOrigin, walletIdDestiny, emailDestiny, cardNumber, amount, motiveDescription)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/payment")
    public Mono<ResponseEntity<WalletTransactionEntity>> payment(Integer walletId, Integer transactioncatid, Integer bookingId) {
        return walletTransactionService.makePayment(walletId, transactioncatid, bookingId)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<WalletTransactionEntity>> withdrawal(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletTransactionService.makeWithdrawal(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/deposit")
    public Mono<ResponseEntity<WalletTransactionEntity>> deposit(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletTransactionService.makeDeposit(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @PostMapping("/recharge")
    public Mono<ResponseEntity<WalletTransactionEntity>> recharge(@RequestParam Integer walletId, @RequestParam Integer transactioncatid, @RequestParam BigDecimal amount) {
        return walletTransactionService.makeRecharge(walletId, transactioncatid, amount)
                .map(walletTransactionEntity -> ResponseEntity.ok(walletTransactionEntity))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @GetMapping("/cardnumber/{walletId}")
    public Mono<String> getCardNumber(@PathVariable Integer walletId) {
        return walletRepository.findById(walletId)
                .map(walletEntity -> walletEntity.getCardNumber());
    }

    @GetMapping("/balance/{walletId}")
    public Mono<BigDecimal> getBalance(@PathVariable Integer walletId) {
        return walletRepository.findById(walletId)
                .map(walletEntity -> walletEntity.getBalance());
    }

    @GetMapping("/{walletId}")
    public Mono<String> getWalletIdForUserOrPromoter(@PathVariable Integer walletId) {
        return walletRepository.findById(walletId)
                .flatMap(wallet -> {
                    if (wallet.getUserClientId() != null || wallet.getUserPromoterId() != null) {
                        return Mono.just("ID : " + wallet.getWalletId());
                    } else {
                        return Mono.error(new IllegalArgumentException("La wallet no está asociada ni a un usuario ni a un promotor."));
                    }
                });
    }

}