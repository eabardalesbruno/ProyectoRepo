package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.TransactionCategoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.TypeWalletTransactionRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletTransactionRepository;
import com.proriberaapp.ribera.services.client.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final TypeWalletTransactionRepository typeWalletTransactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;

    @Override
    public Mono<Integer> generateUniqueAccountNumber(Integer userId) {
       Long cardNumber = Long.parseLong("4" + generaRamdomDigits(15));
        return walletRepository.findByCardNumber(cardNumber)
                .flatMap(walletEntity -> generateUniqueAccountNumber(userId))
                .switchIfEmpty(Mono.just(cardNumber.intValue()));
    }

    @Override
    public Mono<WalletEntity> createWalletUsuario(Integer userId, Integer currencyId) {
        return  generateUniqueAccountNumber(userId)
                .map(cardNumber -> WalletEntity.builder()
                        .userClientId(userId)
                        .cardNumber(cardNumber.longValue())
                        .currencyTypeId(currencyId)
                        .balance(00.00)
                        .avalible(true)
                        .build()).
                flatMap(walletRepository::save);

    }

    @Override
    public Mono<WalletEntity> createWalletPromoter(Integer promoterId, Integer currencyId) {
        return generateUniqueAccountNumber(promoterId)
                .map(cardNumber -> WalletEntity.builder()
                        .userPromoterId(promoterId)
                        .cardNumber(cardNumber.longValue())
                        .currencyTypeId(currencyId)
                        .balance(00.00)
                        .avalible(true)
                        .build()).
                flatMap(walletRepository::save);
    }

    @Override
    public Mono<WalletTransactionEntity> makeTransfer(Integer walletIdOrigin, Integer walletIdDestiny, Double amount) {
        return walletRepository.findById(walletIdOrigin)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance() >= amount) {
                        return walletRepository.findById(walletIdDestiny)
                                .flatMap(walletEntityDestiny -> {
                                    walletEntity.setBalance(walletEntity.getBalance() - amount);
                                    walletEntityDestiny.setBalance(walletEntityDestiny.getBalance() + amount);
                                    return walletRepository.save(walletEntity)
                                            .then(walletRepository.save(walletEntityDestiny))
                                            .map(walletEntity1 -> WalletTransactionEntity.builder()
                                                    .walletId(walletIdOrigin)
                                                    .currencyTypeId(walletEntity.getCurrencyTypeId())
                                                    .transactionCategoryId(1)
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
    public Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactioncatid, Double amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance() >= amount) {
                        walletEntity.setBalance(walletEntity.getBalance() - amount);
                        return walletRepository.save(walletEntity)
                                .map(walletEntity1 -> WalletTransactionEntity.builder()
                                        .walletId(walletId)
                                        .currencyTypeId(walletEntity.getCurrencyTypeId())
                                        .transactionCategoryId(transactioncatid)
                                        .amount(amount)
                                        .description("Retiro de efectivo")
                                        .build());
                    } else {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                });
    }

    @Override
    public Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, Double amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    walletEntity.setBalance(walletEntity.getBalance() + amount);
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
    public Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, Double amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance() >= amount) {
                        walletEntity.setBalance(walletEntity.getBalance() - amount);
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
    public Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, Double amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    walletEntity.setBalance(walletEntity.getBalance() + amount);
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
