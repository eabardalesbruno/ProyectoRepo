package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.RewardReleaseRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.TransferRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PagedResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PasswordValidationResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardTransferHistoryResponse;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserRewardTransferHistoryEntity;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserRewardTransferHistoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.LoginInclubService;
import com.proriberaapp.ribera.services.client.UserRewardService;
import com.proriberaapp.ribera.services.client.UserRewardTrasferHistoryService;
import com.proriberaapp.ribera.utils.emails.TransferEmailTemplateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRewardTrasferHistoryServiceImpl implements UserRewardTrasferHistoryService {

    private final UserClientRepository userClientRepository;

    private final UserRewardTransferHistoryRepository userRewardTransferHistoryRepository;

    private final WalletPointRepository walletPointRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final LoginInclubService loginInclubService;

    private final UserRewardService userRewardService;


    @Override
    public Mono<Void> transferRewards(TransferRequest request) {
        return Mono.zip(
                findUserByIdentifier(request.fromInput(), "origen"),
                findUserByIdentifier(request.toInput(), "destino")
        ).flatMap(tuple -> {
            UserClientEntity fromUser = tuple.getT1();
            UserClientEntity toUser = tuple.getT2();

            String passwordRaw = request.passwordConfirm().trim();

            return validatePasswor(fromUser, passwordRaw)
                    .flatMap(isValid -> {
                        if (!isValid) {
                            return Mono.error(new RuntimeException("Contraseña incorrecta. Transferencia denegada."));
                        }

                        LocalDateTime now = LocalDateTime.now();
                        LocalDate expirationDate = LocalDate.now().plusMonths(1);

                        return Mono.zip(
                                getOrCreateWallet(fromUser.getUserClientId(), now),
                                getOrCreateWallet(toUser.getUserClientId(), now)
                        ).flatMap(wallets -> {
                            WalletPointEntity fromWallet = wallets.getT1();
                            WalletPointEntity toWallet = wallets.getT2();

                            boolean isInclub = Boolean.TRUE.equals(request.isInclubPoints());

                            if (!isInclub && fromWallet.getPoints() < request.amount()) {
                                return Mono.error(new RuntimeException("Fondos insuficientes en wallet origen"));
                            }
                            if (!isInclub) {
                                updateWalletBalances(fromWallet, toWallet, request.amount(), now);
                            } else {
                                toWallet.setPoints(toWallet.getPoints() + request.amount());
                                toWallet.setUpdatedAt(now);
                            }
                            var salida = buildTransferHistory(fromUser, toUser, fromWallet, toWallet, request, now, expirationDate, "salida", isInclub);
                            var ingreso = buildTransferHistory(fromUser, toUser, toWallet, toWallet, request, now, expirationDate, "ingreso", isInclub);
                            Mono<Void> saveChanges = isInclub ? Mono.when(
                                    walletPointRepository.save(toWallet),
                                    userRewardTransferHistoryRepository.save(salida),
                                    userRewardTransferHistoryRepository.save(ingreso)
                            )
                                    : persistChanges(fromWallet, toWallet, salida, ingreso);
                            Mono<Void> callInclubRelease = Mono.empty();
                            if (isInclub) {
                                callInclubRelease = userRewardService
                                        .getUserIdByUsername(request.username())
                                        .flatMap(inclubUserId -> userRewardService.releaseUserReward(
                                                RewardReleaseRequest.builder()
                                                        .userId(inclubUserId)
                                                        .rewardsAmount(request.amount().intValue() * -1)
                                                        .familyPackageName(request.familyPackageName())
                                                        .detail("Transferencia de rewards a usuario " + toUser.getUserClientId() + " de RiberaApp")
                                                        .build()
                                        ));
                            }

                            return saveChanges
                                    .then(callInclubRelease)
                                    .then(sendSuccessEmail(fromUser, toUser, request.amount(), now));
                        });
                    });
        }).then();
    }

    private Mono<Boolean> validatePasswor(UserClientEntity user, String rawPassword) {
        if (Boolean.TRUE.equals(user.isUserInclub())) {
            return loginInclubService.verifiedCredentialsInclub(user.getUsername(), rawPassword)
                    .map(ResponseValidateCredential::isData);
        } else {
            return Mono.just(passwordEncoder.matches(rawPassword, user.getPassword()));
        }
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
            WalletPointEntity referenceWallet,
            TransferRequest request,
            LocalDateTime now,
            LocalDate expiration,
            String tipo,
            boolean isInclub
    ) {
        String email = tipo.equals("salida") ? fromUser.getEmail() : toUser.getEmail();
        Double remaining = wallet.getPoints();

        UserRewardTransferHistoryEntity history = new UserRewardTransferHistoryEntity();
        history.setTransferDate(now);
        history.setFromUserId(fromUser.getUserClientId());
        history.setToUserId(toUser.getUserClientId());
        history.setSubCategory(request.subCategory());
        history.setUsdRewardsTransferred(request.amount());
        history.setUsdRewardsRemaining(remaining);
        history.setExpirationDate(expiration);
        history.setCreatedAt(now);
        history.setUpdatedAt(now);
        history.setEmail(email);
        history.setType(tipo);
        history.setWalletPointId(wallet.getId());
        history.setStatus("ACTIVO");

        if (isInclub) {
            history.setIsInclubPoints(true);
            history.setFamilyPackageName(request.familyPackageName());
            history.setTotalRewardsBeforeTransfer(request.totalRewardsBeforeTransfer());
        } else {
            history.setIsInclubPoints(false);
        }
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
                .flatMap(user -> {
                    if (Boolean.TRUE.equals(user.isUserInclub())) {
                        return loginInclubService.verifiedCredentialsInclub(user.getUsername(), rawPassword)
                                .map(response -> {
                                    boolean isValid = response.isData();
                                    String message = isValid ? "Contraseña válida (inclub)" : "Contraseña incorrecta (inclub)";
                                    return new PasswordValidationResponse(isValid, message);
                                });
                    } else {
                        boolean isValid = passwordEncoder.matches(rawPassword, user.getPassword());
                        String message = isValid ? "Contraseña válida" : "Contraseña incorrecta";
                        return Mono.just(new PasswordValidationResponse(isValid, message));
                    }
                })
                .switchIfEmpty(Mono.just(new PasswordValidationResponse(false, "Usuario no encontrado")));
    }

    //History of transfers
    public Mono<PagedResponse<UserRewardTransferHistoryResponse>> getFilteredTransfers(String subcategory, String status, LocalDate dateFrom, LocalDate dateTo, int page, int pageSize) {
        int skipCount = (page - 1) * pageSize;

        return userRewardTransferHistoryRepository.findAll()
                .filter(entity -> filterBySubcategory(entity, subcategory))
                .filter(entity -> filterByStatus(entity, status))
                .filter(entity -> filterByDateRange(entity, dateFrom, dateTo))
                .collectList()
                .flatMap(filteredList -> {
                    int totalElements = filteredList.size();
                    int totalPages = (int) Math.ceil((double) totalElements / pageSize);

                    List<UserRewardTransferHistoryEntity> paginated = filteredList.stream()
                            .skip(skipCount)
                            .limit(pageSize)
                            .toList();

                    return Flux.fromIterable(paginated)
                            .flatMap(this::mapToResponseWithUsers)
                            .collectList()
                            .map(content -> PagedResponse.<UserRewardTransferHistoryResponse>builder()
                                    .content(content)
                                    .page(page)
                                    .pageSize(pageSize)
                                    .totalElements(totalElements)
                                    .totalPages(totalPages)
                                    .build());
                });
    }

    private Mono<UserRewardTransferHistoryResponse> mapToResponseWithUsers(UserRewardTransferHistoryEntity entity) {
        Mono<UserClientEntity> fromUserMono = userClientRepository.findByUserClientId(entity.getFromUserId());
        Mono<UserClientEntity> toUserMono = userClientRepository.findByUserClientId(entity.getToUserId());

        return Mono.zip(fromUserMono, toUserMono)
                .map(tuple -> buildResponse(entity, tuple.getT1(), tuple.getT2()));
    }

    private UserRewardTransferHistoryResponse  buildResponse(UserRewardTransferHistoryEntity entity, UserClientEntity fromUser, UserClientEntity toUser) {
        return UserRewardTransferHistoryResponse.builder()
                .userRewardTransferId(entity.getUser_reward_transfer_id())
                .usdRewardsTransferred(entity.getUsdRewardsTransferred())
                .usdRewardsRemaining(entity.getUsdRewardsRemaining())
                .transferDate(entity.getTransferDate())
                .expirationDate(entity.getExpirationDate() != null ? entity.getExpirationDate().atStartOfDay() : null)
                .subCategory(entity.getSubCategory())
                .fromUserFullName(formatFullName(fromUser))
                .toUserFullName(formatFullName(toUser))
                .email(fromUser.getEmail())
                .symbol(determineSymbol(entity.getType()))
                .Status(entity.getStatus())
                .build();
    }

    private String formatFullName(UserClientEntity user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    private String determineSymbol(String type) {
        return "salida".equalsIgnoreCase(type) ? "-"
                : "ingreso".equalsIgnoreCase(type) ? "+" : "";
    }

    private boolean filterBySubcategory(UserRewardTransferHistoryEntity entity, String subcategory) {
        return subcategory == null || subcategory.equalsIgnoreCase(entity.getSubCategory());
    }

    private boolean filterByStatus(UserRewardTransferHistoryEntity entity, String status) {
        return status == null || status.equalsIgnoreCase(entity.getStatus());
    }

    private boolean filterByDateRange(UserRewardTransferHistoryEntity entity, LocalDate from, LocalDate to) {
        if (entity.getTransferDate() == null) return false;
        LocalDate date = entity.getTransferDate().toLocalDate();
        boolean isAfterFrom = (from == null || !date.isBefore(from));
        boolean isBeforeTo = (to == null || !date.isAfter(to));
        return isAfterFrom && isBeforeTo;
    }
}