package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Mono<WalletEntity> findByUserClientId(Integer userClientId) {
        return walletRepository.findByUserClientId(userClientId);
    }

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


    private String generaRamdomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}