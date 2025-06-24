package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.WalletTransactionDTO;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailsPromoterDTO;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletTransactionRepository;
import com.proriberaapp.ribera.services.client.WalletService;
import com.proriberaapp.ribera.services.client.WalletTransactionService;
import com.proriberaapp.ribera.services.client.impl.WalletTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletTransactionService walletTransactionService;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionServiceImpl walle;

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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }
        return walletTransactionService.makeTransfer(walletIdOrigin, walletIdDestiny, emailDestiny, cardNumber, amount, motiveDescription)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/payment")
    public Mono<ResponseEntity<Map<String, String>>> makePayment(@RequestParam Integer walletId, @RequestParam List<Integer> bookingIds) {
        return walletTransactionService.makePayment(walletId, bookingIds)
                .map(response -> ResponseEntity.ok(Collections.singletonMap("message", "Pago procesado con éxito para todas las reservas.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Error al realizar el pago: " + e.getMessage()))));
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

    @GetMapping("/transaction-details")
    public Flux<WalletTransactionDTO> getTransactionDetails(@RequestParam Integer walletId) {
        return walletTransactionRepository.findTransactionDetailsByWalletId(walletId);
    }

    @GetMapping("/total-debt")
    public Mono<ResponseEntity<BigDecimal>> getTotalDebt(@RequestParam Integer walletId) {
        return walletTransactionService.getTotalAmountForPromoter(walletId)
                .map(total -> ResponseEntity.ok(total))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @GetMapping("/details")
    public Mono<ResponseEntity<Map<String, Object>>> getBookingDetailsForPromoter(@RequestParam Integer walletId){
        return walletTransactionService.getBookingDetailsForPromoter(walletId)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(error -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                });
    }

    @GetMapping("/exchange-rate/{date}")
    public ResponseEntity<?> getExchangeRate(@PathVariable String date) {
        try {
            Map<String, Object> exchangeRate = walletTransactionService.getExchangeRate(date);
            return ResponseEntity.ok(exchangeRate);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/recharge-pending-commissions")
    public Mono<ResponseEntity<List<WalletTransactionEntity>>> rechargePendingCommissions() {
        return walle.processPendingCommissions()
                .collectList()
                .map(transactions -> ResponseEntity.ok(transactions))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }


    @GetMapping("/payment-details")
    public Flux<PaymentDetailsPromoterDTO> getPaymentDetails(@RequestParam Integer walletId) {
       return walletTransactionRepository.findPaymentDetailsByWalletId(walletId);
    }

}