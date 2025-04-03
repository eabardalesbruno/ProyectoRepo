package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import com.proriberaapp.ribera.Domain.entities.WalletPointHistoryEntity;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.mapper.WalletPointMapper;
import com.proriberaapp.ribera.Infraestructure.repository.PointConversionRateRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointHistoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointRepository;
import com.proriberaapp.ribera.services.client.UserClientService;
import com.proriberaapp.ribera.services.client.WalletPointService;
import com.proriberaapp.ribera.services.point.user.UserPointService;
import com.proriberaapp.ribera.utils.WalletPointUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletPointServiceImpl implements WalletPointService {
    private final WalletPointRepository walletPointRepository;
    private final WalletPointMapper walletPointMapper;
    private final TransactionalOperator transactionalOperator;
    private final WalletPointHistoryRepository walletPointHistoryRepository;
    private final UserPointService userPointService;
    private final WalletPointUtils walletPointUtils;
    private final PointConversionRateRepository conversionRateRepository;
    private final UserClientService userClientService;

    @Override
    public Mono<WalletPointResponse> createWalletPoint(WalletPointRequest walletPointRequest) {
        return walletPointUtils.calculatePoints(walletPointRequest)
                .flatMap(calculatedPoints -> {
                    walletPointRequest.setRewardPoints(calculatedPoints);
                    return walletPointRepository.save(walletPointMapper.toEntity(walletPointRequest))
                            .map(walletPointMapper::toDto);
                })
                .doOnNext(response -> log.info("Successfully created wallet point: {}", response))
                .doOnError(e -> log.error("Error creating wallet point", e))
                .onErrorResume(e -> Mono.error(new RequestException("Error creating wallet point: " + e.getMessage())))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<WalletPointResponse> updateWalletPoints(Integer userId, WalletPointRequest walletPointRequest) {
        Double points = walletPointRequest.getRewardPoints();
        return walletPointRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RequestException("Wallet not found for user: " + userId)))
                .flatMap(wallet -> {
                    wallet.setPoints(wallet.getPoints() + points);
                    return walletPointRepository.save(wallet);
                })
                .flatMap(updatedWallet -> saveWalletHistory(userId, points)
                        .thenReturn(updatedWallet))
                .map(walletPointMapper::toDto)
                .doOnNext(response -> log.info("Successfully updated wallet points for user: {}", userId))
                .doOnError(e -> log.error("Error updating wallet points", e))
                .onErrorResume(e -> Mono.error(new RequestException("Error updating wallet points: " + e.getMessage())))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<WalletPointResponse> buyPoints(Integer userId, WalletPointRequest walletPointRequest) {
        return updateWalletPoints(userId, walletPointRequest);
    }

    private Mono<Void> saveWalletHistory(Integer userId, Double points) {
        WalletPointHistoryEntity history = walletPointUtils.buildWalletHistory(userId, points);
        return walletPointHistoryRepository.save(history)
                .doOnNext(saved -> log.info("Wallet history saved for user: {}", userId))
                .then();
    }
    @Override
    public Mono<WalletPointResponse> getWalletByIdentifier(String identifier, Role role, Integer idFamily, String tokenBackOffice) {
        return switch (role) {
            case ROLE_PARTNER -> userPointService.getUserPoints(identifier, idFamily, tokenBackOffice)
                    .doOnNext(userPointDataResponse -> log.info("UserPointDataResponse: {}", userPointDataResponse))
                    .flatMap(userPointDataResponse ->
                            walletPointRepository.findByUsername(identifier)
                                    .switchIfEmpty(Mono.defer(() -> {
                                        WalletPointEntity newWalletPoint = WalletPointEntity.builder()
                                                .userId(Integer.valueOf(identifier))
                                                .points(0.0)
                                                .build();
                                        return walletPointRepository.save(newWalletPoint);
                                    }))
                                    .map(walletPointEntity -> {
                                        WalletPointResponse walletPointResponse = walletPointMapper.toDto(walletPointEntity);
                                        walletPointResponse.setUserInclubId(userPointDataResponse.getIdUser());
                                        walletPointResponse.setIdFamily(userPointDataResponse.getIdFamily());
                                        walletPointResponse.setLiberatedPoints(userPointDataResponse.getLiberatedPoints());
                                        return walletPointResponse;
                                    })
                    )
                    .doOnError(e -> log.error("Error fetching wallet for identifier: {}", identifier, e))
                    .onErrorResume(e -> Mono.error(new RequestException("Error fetching wallet: " + e.getMessage())));

            case ROLE_USER -> walletPointRepository.findByUserId(Integer.valueOf(identifier))
                    .switchIfEmpty(Mono.defer(() -> {
                        WalletPointEntity newWalletPoint = WalletPointEntity.builder()
                                .userId(Integer.valueOf(identifier))
                                .points(0.0)
                                .build();
                        return walletPointRepository.save(newWalletPoint);
                    }))
                    .map(walletPointMapper::toDto);

            default -> Mono.empty();
        };
    }

    @Override
    public Mono<WalletPointResponse> getWalletByUserId(Integer userId) {
        return walletPointRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RequestException("Wallet not found for user: " + userId)))
                .map(walletPointMapper::toDto);
    }

    @Override
    public Mono<Void> convertPoints(Integer userId, WalletPointRequest walletPointRequest) {
        return conversionRateRepository.findByFamilyId(walletPointRequest.getFamilyId())
                .switchIfEmpty(Mono.error(new RuntimeException("No conversion rate found")))
                .flatMap(rate -> {
                    Double convertedPoints = walletPointRequest.getRewardPoints() * rate.getConversionRate();
                    return walletPointRepository.findByUserId(userId)
                            .flatMap(wallet -> {
                                wallet.setPoints(wallet.getPoints() - walletPointRequest.getRewardPoints());
                                return walletPointRepository.save(wallet);
                            })
                            .then(walletPointHistoryRepository.save(
                                    WalletPointHistoryEntity.builder()
                                            .userId(userId)
                                            .points(convertedPoints)
                                            .walletPointId(walletPointRequest.getFamilyId()) // idFamily
                                            .build()
                            ));
                }).then();
    }


}
