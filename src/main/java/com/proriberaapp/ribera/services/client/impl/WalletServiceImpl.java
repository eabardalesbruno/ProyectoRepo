package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final TypeWalletTransactionRepository typeWalletTransactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final UserClientRepository userClientRepository;
    private final UserPromoterRepository userPromoterRepository;

    @Override
    public Mono<String> generateUniqueAccountNumber(Integer userId) {

        String cardNumber = "4" + generaRamdomDigits(15);
        return walletRepository.findByCardNumber(cardNumber)
                .flatMap(walletEntity -> generateUniqueAccountNumber(userId))

                .switchIfEmpty(Mono.just(cardNumber));
    }

    @Override
    public Mono<WalletEntity> createWalletUsuario(Integer userClientId, Integer currencyId) {
        return generateUniqueAccountNumber(userClientId)
                .map(cardNumber -> {
                    WalletEntity wallet = WalletEntity.builder()
                            .userClientId(userClientId)
                            .cardNumber(cardNumber.toString())
                            .currencyTypeId(currencyId)
                            .balance(BigDecimal.valueOf(00.00))
                            .avalible(true)
                            .build();
                    return wallet;
                })
                .flatMap(walletRepository::save);
    }


    @Override
    public Mono<WalletEntity> createWalletPromoter(Integer userPromoterId, Integer currencyId) {
        return generateUniqueAccountNumber(userPromoterId)
                .map(cardNumber -> {
                        WalletEntity wallet = WalletEntity.builder()
                        .userPromoterId(userPromoterId)
                        .cardNumber(cardNumber.toString())
                        .currencyTypeId(currencyId)
                        .balance(BigDecimal.valueOf(00.00))
                        .avalible(true)
                        .build();
                    return wallet;
                })
                .flatMap(walletRepository::save);
    }

    @Override
    public Mono<WalletTransactionEntity> makeTransfer(Integer walletIdOrigin, Integer walletIdDestiny, String emailDestiny, String documentNumberDestiny, BigDecimal amount) {
        return walletRepository.findById(walletIdOrigin)
                .flatMap(walletEntityOrigin -> {
                    if (walletEntityOrigin.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                    Mono<WalletEntity> walletEntityDestinyMono = findDestinationWallet(walletIdDestiny, emailDestiny, documentNumberDestiny);

                    return walletEntityDestinyMono.flatMap(walletEntityDestiny -> {
                        walletEntityOrigin.setBalance(walletEntityOrigin.getBalance().subtract(amount));
                        walletEntityDestiny.setBalance(walletEntityDestiny.getBalance().add(amount));

                        return walletRepository.save(walletEntityOrigin)
                                .then(walletRepository.save(walletEntityDestiny))
                                .flatMap(savedDestiny -> {
                                    WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                            .walletId(walletIdOrigin)
                                            .currencyTypeId(walletEntityOrigin.getCurrencyTypeId())
                                            .transactionCategoryId(1)
                                            .amount(amount)
                                            .description("Transferencia a " + walletEntityDestiny.getCardNumber())
                                            .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                            .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                            .build();
                                    return walletTransactionRepository.save(transaction);
                                });
                    });
                });
    }

    private Mono<WalletEntity> findDestinationWallet(Integer walletIdDestiny, String emailDestiny, String documentNumberDestiny) {
        if (walletIdDestiny != null) {
            return walletRepository.findById(walletIdDestiny)
                    .switchIfEmpty(Mono.error(new Exception("No se encontró la wallet con el ID proporcionado.")));
        } else if (emailDestiny != null && !emailDestiny.isEmpty()) {
            return findWalletByEmailOrDocument(emailDestiny, null);
        } else if (documentNumberDestiny != null && !documentNumberDestiny.isEmpty()) {
            return findWalletByEmailOrDocument(null, documentNumberDestiny);
        }
        return Mono.error(new Exception("Debe proporcionar walletIdDestiny, emailDestiny o documentNumberDestiny."));
    }

    @Override
    public Mono<WalletEntity> findWalletByEmailOrDocument(String email, String documentNumber) {
        if (documentNumber != null && !documentNumber.isEmpty()) {
            return findWalletByUserOrPromoter(
                    userClientRepository.findByDocumentNumber(documentNumber.trim()),
                    userPromoterRepository.findByDocumentNumber(documentNumber.trim())
            ).switchIfEmpty(Mono.error(new Exception("No se encontró ninguna wallet asociada al documento.")));
        }
        if (email != null && !email.isEmpty()) {
            return findWalletByUserOrPromoter(
                    userClientRepository.findByEmail(email),
                    userPromoterRepository.findByEmail(email)
            ).switchIfEmpty(Mono.error(new Exception("No se encontró ninguna wallet asociada al email.")));
        }
        throw new IllegalArgumentException("Debe proporcionar email o documento para buscar la wallet de destino.");
    }


    private Mono<WalletEntity> findWalletByUserOrPromoter(Mono<UserClientEntity> userMono, Mono<UserPromoterEntity> promoterMono) {
        return userMono
                .flatMap(user -> {
                    if (user.getWalletId() != null) {
                        return walletRepository.findById(user.getWalletId());
                    } else {
                        return Mono.error(new Exception("El usuario no tiene una wallet asociada."));
                    }
                })
                .switchIfEmpty(promoterMono.flatMap(promoter -> {
                    if (promoter.getWalletId() != null) {
                        return walletRepository.findById(promoter.getWalletId());
                    } else {
                        return Mono.error(new Exception("El promotor no tiene una wallet asociada."));
                    }
                }));
    }


    @Override
    public Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactionCatId, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance().compareTo(amount) >= 0) {
                        walletEntity.setBalance(walletEntity.getBalance().subtract(amount));
                        return walletRepository.save(walletEntity)
                                .flatMap(savedWallet -> {
                                    WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                            .walletId(walletId)
                                            .currencyTypeId(walletEntity.getCurrencyTypeId())
                                            .transactionCategoryId(transactionCatId)
                                            .amount(amount)
                                            .description("Retiro de efectivo")
                                            .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                            .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                            .build();
                                    return walletTransactionRepository.save(transaction);
                                });
                    } else {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                });
    }

    @Override
    public Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    walletEntity.setBalance(walletEntity.getBalance().add(amount));
                    return walletRepository.save(walletEntity)
                            .flatMap(walletEntity1 -> {
                                WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                        .walletId(walletId)
                                        .currencyTypeId(walletEntity.getCurrencyTypeId())
                                        .transactionCategoryId(transactioncatid)
                                        .amount(amount)
                                        .description("Deposito de efectivo")
                                        .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                        .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                        .build();
                                return walletTransactionRepository.save(transaction);
                            });
                });
    }

    @Override
    public Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance().compareTo(amount) >= 0) {
                        walletEntity.setBalance(walletEntity.getBalance().subtract(amount));
                        return walletRepository.save(walletEntity)
                                .flatMap(walletEntity1 -> {
                                    WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                            .walletId(walletId)
                                            .currencyTypeId(walletEntity.getCurrencyTypeId())
                                            .transactionCategoryId(transactioncatid)
                                            .amount(amount)
                                            .description("Pago de servicio")
                                            .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                            .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                            .build();
                                    return walletTransactionRepository.save(transaction);
                                });
                    } else {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                });
    }

    @Override
    public Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    walletEntity.setBalance(walletEntity.getBalance().add(amount));
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    WalletTransactionEntity transactionEntity = WalletTransactionEntity.builder()
                            .walletId(walletId)
                            .currencyTypeId(walletEntity.getCurrencyTypeId())
                            .transactionCategoryId(transactioncatid)
                            .amount(amount)
                            .inicialDate(currentTimestamp)
                            .avalibleDate(currentTimestamp)
                            .description("Recarga de saldo")
                            .build();
                    return walletRepository.save(walletEntity)
                            .then(walletTransactionRepository.save(transactionEntity));
                });
    }





    private String generaRamdomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
