package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserRewardTransferHistoryEntity;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserRewardTransferHistoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointRepository;
import com.proriberaapp.ribera.services.client.UserRewardTrasferHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRewardTrasferHistoryServiceImpl implements UserRewardTrasferHistoryService {

    private final UserClientRepository userClientRepository;

    private final UserRewardTransferHistoryRepository userRewardTransferHistoryRepository;

    private final WalletPointRepository walletPointRepository;


    @Override
    public Mono<Void> transferRewards(String fromInput, String toInput, Double amount, String subCategory) {
        return Mono.zip(
                findUserByIdentifier(fromInput, "origen"),
                findUserByIdentifier(toInput, "destino")
        ).flatMap(tuple -> {
            UserClientEntity fromUser = tuple.getT1();
            UserClientEntity toUser = tuple.getT2();
            LocalDateTime now = LocalDateTime.now();

            return Mono.zip(
                    getOrCreateWallet(fromUser.getUserClientId(), now),
                    getOrCreateWallet(toUser.getUserClientId(), now)
            ).flatMap(wallets -> {
                WalletPointEntity fromWallet = wallets.getT1();
                WalletPointEntity toWallet = wallets.getT2();

                if (fromWallet.getPoints() < amount) {
                    return Mono.error(new RuntimeException("Fondos insuficientes en wallet origen"));
                }

                updateWalletBalances(fromWallet, toWallet, amount, now);

                LocalDate expirationDate = LocalDate.now().plusMonths(1);
                var salida = buildTransferHistory(fromUser, toUser, fromWallet, amount, subCategory, now, expirationDate, "salida");
                var ingreso = buildTransferHistory(fromUser, toUser, toWallet, amount, subCategory, now, expirationDate, "ingreso");

                return persistChanges(fromWallet, toWallet, salida, ingreso);
            });
        }).then();
    }

    private Mono<UserClientEntity> findUserByIdentifier(String identifier, String tipo) {
        return userClientRepository.findByIdentifier(identifier)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario " + tipo + " no encontrado")));
    }

    private Mono<WalletPointEntity> getOrCreateWallet(Integer userId, LocalDateTime now) {
        return walletPointRepository.findByUserId(userId)
                .switchIfEmpty(Mono.defer(() -> {
                    WalletPointEntity newWallet = WalletPointEntity.builder()
                            .userId(userId)
                            .points(0.0)
                            .build();
                    newWallet.setCreatedAt(now);
                    newWallet.setUpdatedAt(now);
                    return walletPointRepository.save(newWallet);
                }));
    }

    private void updateWalletBalances(WalletPointEntity from, WalletPointEntity to, Double amount, LocalDateTime now) {
        from.setPoints(from.getPoints() - amount);
        to.setPoints(to.getPoints() + amount);
        from.setUpdatedAt(now);
        to.setUpdatedAt(now);
    }

    private UserRewardTransferHistoryEntity buildTransferHistory(
            UserClientEntity fromUser,
            UserClientEntity toUser,
            WalletPointEntity wallet,
            Double amount,
            String subCategory,
            LocalDateTime now,
            LocalDate expiration,
            String tipo
    ) {
        String email = tipo.equals("salida") ? fromUser.getEmail() : toUser.getEmail();
        Double remaining = wallet.getPoints();

        UserRewardTransferHistoryEntity history = new UserRewardTransferHistoryEntity();
        history.setTransferDate(now);
        history.setFromUserId(fromUser.getUserClientId());
        history.setToUserId(toUser.getUserClientId());
        history.setSubCategory("SILVER");
        history.setUsdRewardsTransferred(amount);
        history.setUsdRewardsRemaining(remaining);
        history.setExpirationDate(expiration);
        history.setCreatedAt(now);
        history.setUpdatedAt(now);
        history.setEmail(email);
        history.setType(tipo);
        history.setWalletPointId(wallet.getId());

        return history;
    }

    private Mono<Void> persistChanges(
            WalletPointEntity fromWallet,
            WalletPointEntity toWallet,
            UserRewardTransferHistoryEntity salida,
            UserRewardTransferHistoryEntity ingreso
    ) {
        return Mono.when(
                walletPointRepository.save(fromWallet),
                walletPointRepository.save(toWallet),
                userRewardTransferHistoryRepository.save(salida),
                userRewardTransferHistoryRepository.save(ingreso)
        );
    }

}



