package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final TypeWalletTransactionRepository typeWalletTransactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final UserClientRepository userClientRepository;

    @Override
    public Mono<Integer> generateUniqueAccountNumber(Integer userId) {
       String cardNumber = "4" + generaRamdomDigits(15);
        return walletRepository.findByCardNumber(cardNumber)
                .flatMap(walletEntity -> generateUniqueAccountNumber(userId))
                .switchIfEmpty(Mono.just(Integer.valueOf(cardNumber)));
    }

    @Override
    public Mono<WalletEntity> createWalletUsuario(Integer userId, Integer currencyId) {
        return  generateUniqueAccountNumber(userId)
                .map(cardNumber -> WalletEntity.builder()
                        .userClientId(userId)
                        .cardNumber(cardNumber.toString())
                        .currencyTypeId(currencyId)
                        .balance(BigDecimal.valueOf(00.00))
                        .avalible(true)
                        .build()).
                flatMap(walletRepository::save);

    }

    @Override
    public Mono<WalletEntity> createWalletPromoter(Integer promoterId, Integer currencyId) {
        return generateUniqueAccountNumber(promoterId)
                .map(cardNumber -> WalletEntity.builder()
                        .userPromoterId(promoterId)
                        .cardNumber(cardNumber.toString())
                        .currencyTypeId(currencyId)
                        .balance(BigDecimal.valueOf(00.00))
                        .avalible(true)
                        .build()).
                flatMap(walletRepository::save);
    }

    @Override
    public Mono<WalletTransactionEntity> makeTransfer(Integer walletIdOrigin, Integer walletIdDestiny ,String emailDestiny, String nameDestiny, BigDecimal amount) {
        // Buscar la wallet de origen
        return walletRepository.findById(walletIdOrigin)
                .flatMap(walletEntityOrigin -> {
                    //Verificacion si el saldo es suficiente
                    if (walletEntityOrigin.getBalance().compareTo(amount) >= 0) {
                        Mono<WalletEntity> walletEntityDestinyMono;

                        // Se busca el destino por id de la wallet
                        if (walletIdDestiny != null) {
                            walletEntityDestinyMono = walletRepository.findById(walletIdDestiny);
                        } else if (emailDestiny != null && !emailDestiny.isEmpty()) {
                            // Busqueda por email del usuario destino
                            walletEntityDestinyMono = userClientRepository.findByEmail(emailDestiny)
                                    .flatMap(userDestiny -> {
                                        if (userDestiny.getWalletId() != null) {
                                            return walletRepository.findById(userDestiny.getWalletId());
                                        } else {
                                            return Mono.error(new Exception("El usuario no tiene una wallet asociada."));
                                        }
                                    });
                        } else if (nameDestiny != null && !nameDestiny.isEmpty()) {
                            // Si no se buca ni por los dos metodos entonces realiza un busqueda por nombre de usuario destino
                            walletEntityDestinyMono = userClientRepository.findByName(nameDestiny)
                                    .flatMap(userDestiny -> {
                                        if (userDestiny.getWalletId() != null) {
                                            return walletRepository.findById(userDestiny.getWalletId());
                                        } else {
                                            return Mono.error(new Exception("El usuario no tiene una wallet asociada."));
                                        }
                                    });
                        } else {
                            return Mono.error(new Exception("Debe proporcionar el ID de la wallet, correo electrónico o nombre para la wallet de destino."));
                        }

                        // Realizar la transferencia
                        return walletEntityDestinyMono.flatMap(walletEntityDestiny -> {
                            walletEntityOrigin.setBalance(walletEntityOrigin.getBalance().subtract(amount));
                            walletEntityDestiny.setBalance(walletEntityDestiny.getBalance().add(amount));


                            // Guarda los cambios en las wallets
                            return walletRepository.save(walletEntityOrigin)
                                    .then(walletRepository.save(walletEntityDestiny))
                                    .map(walletEntity -> WalletTransactionEntity.builder()
                                            .walletId(walletIdOrigin)
                                            .currencyTypeId(walletEntityOrigin.getCurrencyTypeId())
                                            .transactionCategoryId(1)  // Asegúrate de que el ID de la categoría de transacción sea correcto
                                            .amount(amount)
                                            .description("Transferencia a " + walletEntityDestiny.getCardNumber())
                                            .build());
                        });
                    } else {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                });
    }




    @Override
    public Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactionCatId, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance().compareTo(amount) >= 0) {
                        walletEntity.setBalance(walletEntity.getBalance().subtract(amount));
                        return walletRepository.save(walletEntity)
                                .map(savedWallet -> WalletTransactionEntity.builder()
                                        .walletId(walletId)
                                        .currencyTypeId(walletEntity.getCurrencyTypeId())
                                        .transactionCategoryId(transactionCatId)
                                        .amount(amount)
                                        .description("Retiro de efectivo")
                                        .build());
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
                            .map(walletEntity1 -> WalletTransactionEntity.builder()
                                    .walletId(walletId)
                                    .currencyTypeId(walletEntity.getCurrencyTypeId())
                                    .transactionCategoryId(transactioncatid)
                                    .amount(amount)
                                    .description("Deposito de efectivo")
                                    .build());
                });
    }

    @Override
    public Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance().compareTo(amount) >= 0) {
                        walletEntity.setBalance(walletEntity.getBalance().subtract(amount));
                        return walletRepository.save(walletEntity)
                                .map(walletEntity1 -> WalletTransactionEntity.builder()
                                        .walletId(walletId)
                                        .currencyTypeId(walletEntity.getCurrencyTypeId())
                                        .transactionCategoryId(transactioncatid)
                                        .amount(amount)
                                        .description("Pago de servicio")
                                        .build());
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
                    return walletRepository.save(walletEntity)
                            .map(walletEntity1 -> WalletTransactionEntity.builder()
                                    .walletId(walletId)
                                    .currencyTypeId(walletEntity.getCurrencyTypeId())
                                    .transactionCategoryId(transactioncatid)
                                    .amount(amount)
                                    .description("Recarga de saldo")
                                    .build());
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
