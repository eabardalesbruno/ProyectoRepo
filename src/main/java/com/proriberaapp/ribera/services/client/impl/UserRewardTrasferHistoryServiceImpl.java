package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.TransferRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PasswordValidationResponse;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserRewardTransferHistoryEntity;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserRewardTransferHistoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.UserRewardTrasferHistoryService;
import com.proriberaapp.ribera.utils.emails.TransferEmailTemplateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;


    @Override
    public Mono<Void> transferRewards(TransferRequest request) {
        return Mono.zip(
                        findUserByIdentifier(request.fromInput(), "origen"),
                        findUserByIdentifier(request.toInput(), "destino")
                ).flatMap(tuple -> {
                    UserClientEntity fromUser = tuple.getT1();
                    UserClientEntity toUser = tuple.getT2();

                    String passwordRaw = request.passwordConfirm().trim();

                    if (!passwordEncoder.matches(passwordRaw, fromUser.getPassword())) {
                        return Mono.error(new RuntimeException("Contraseña incorrecta. Transferencia denegada."));
                    }

                    LocalDateTime now = LocalDateTime.now();

                    return Mono.zip(
                            getOrCreateWallet(fromUser.getUserClientId(), now),
                            getOrCreateWallet(toUser.getUserClientId(), now)
                    ).flatMap(wallets -> {
                        WalletPointEntity fromWallet = wallets.getT1();
                        WalletPointEntity toWallet = wallets.getT2();

                        if (fromWallet.getPoints() < request.amount()) {
                            return Mono.error(new RuntimeException("Fondos insuficientes en wallet origen"));
                        }

                        updateWalletBalances(fromWallet, toWallet, request.amount(), now);

                        LocalDate expirationDate = LocalDate.now().plusMonths(1);
                        var salida = buildTransferHistory(fromUser, toUser, fromWallet, request.amount(), request.subCategory(), now, expirationDate, "salida");
                        var ingreso = buildTransferHistory(fromUser, toUser, toWallet, request.amount(), request.subCategory(), now, expirationDate, "ingreso");
                        return persistChanges(fromWallet, toWallet, salida, ingreso)
                                .then(sendSuccessEmail(fromUser, toUser, request.amount(), now));
                    });
                })
                .then();
    }

    private Mono<UserClientEntity> findUserByIdentifier(String input, String tipo) {
        Mono<UserClientEntity> searchById = Mono.empty();
        if (input.matches("\\d+")) {
            searchById = userClientRepository.findById(Integer.parseInt(input));
        }
        return searchById
                .switchIfEmpty(userClientRepository.findByIdentifier(input))
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

    private Mono<Void> sendSuccessEmail(UserClientEntity fromUser, UserClientEntity toUser, Double amount, LocalDateTime dateTime) {
        String htmlBody = TransferEmailTemplateBuilder.buildTransferSuccessEmail(
                String.valueOf(amount),
                toUser.getFirstName()+" "+toUser.getLastName(),
                dateTime
        );

        String subject = "Transferencia de Rewards exitosa";
        return emailService.sendEmail(fromUser.getEmail(), subject, htmlBody);
    }

    @Override
    public Mono<PasswordValidationResponse> validatePassword(String email, String rawPassword) {
        return userClientRepository.findByEmail(email)
                .map(user -> {
                    boolean isValid = passwordEncoder.matches(rawPassword, user.getPassword());
                    String message = isValid ? "Contraseña válida" : "Contraseña incorrecta";
                    return new PasswordValidationResponse(isValid, message);
                })
                .switchIfEmpty(Mono.just(new PasswordValidationResponse(false, "Usuario no encontrado")));
    }
}