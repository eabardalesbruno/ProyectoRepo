package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.services.client.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;


    @Override
    public Mono<Integer> generateUniqueAccountNumber(Integer userId) {
       Long cardNumber = Long.valueOf(generaRamdomDigits(16));
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
                        .balance(0.0)
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
                        .balance(0.0)
                        .avalible(true)
                        .build()).
                flatMap(walletRepository::save);
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
