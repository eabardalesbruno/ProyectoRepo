package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.RewardPointBORequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.BoResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointDataResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
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

    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    @Value("${inclub.api.url.user}")
    private String urlLoginUserInclub;

    private final WebClient webClient;
    private final ExternalAuthService externalAuthService;
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
                                    .switchIfEmpty(Mono.defer(() -> userClientService.findByUsername(identifier)
                                            .flatMap(user -> {
                                                WalletPointEntity newWalletPoint = WalletPointEntity.builder()
                                                        .userId(user.getUserClientId())
                                                        .points(0.0)
                                                        .build();
                                                return walletPointRepository.save(newWalletPoint);
                                            })))
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
                .switchIfEmpty(Mono.defer(() -> {
                    WalletPointEntity newWalletPoint = WalletPointEntity.builder()
                            .userId(userId)
                            .points(0.0)
                            .build();
                    return walletPointRepository.save(newWalletPoint);
                }))
                .map(walletPointMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<Void> convertPoints(Integer originalUserId, String username, WalletPointRequest walletPointRequest) {
        return conversionRateRepository.findByFamilyId(walletPointRequest.getFamilyId())
                .switchIfEmpty(Mono.error(new RuntimeException("No conversion rate found")))
                .flatMap(rate -> {
                    Double convertedPoints = walletPointRequest.getRewardPoints() * rate.getConversionRate();
                    return externalAuthService.getExternalToken()
                            .zipWith(
                                    webClient.get()
                                            .uri(urlLoginUserInclub + "/" + username)
                                            .retrieve()
                                            .bodyToMono(ResponseInclubLoginDto.class)
                            )
                            .flatMap(tuple -> {
                                String tokenBackOffice = tuple.getT1();
                                ResponseInclubLoginDto responseInclub = tuple.getT2();
                                //Double convertedPoints = walletPointRequest.getRewardPoints() * 1;

                                Integer userIdFromInclub = responseInclub.getData().getId();

                                RewardPointBORequest boRequest = RewardPointBORequest.builder()
                                        .idUser(userIdFromInclub)
                                        .pointsToConvert(convertedPoints)
                                        .idFamily(walletPointRequest.getFamilyId())
                                        .build();

                                return webClient.post()
                                        .uri(urlBackOffice + "/user-points-released/points-released-to-rewards")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBackOffice)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(boRequest)
                                        .retrieve()
                                        .onStatus(HttpStatusCode::isError, clientResponse ->
                                                clientResponse.bodyToMono(String.class)
                                                        .flatMap(errorBody -> {
                                                            System.out.println("Error response: " + errorBody);
                                                            return Mono.error(new RuntimeException("Error in conversion: " + errorBody));
                                                        })
                                        )
                                        .bodyToMono(BoResponse.class)
                                        .flatMap(response -> {
                                            int status = response.getStatus();
                                            Object body = response.getData();

                                            System.out.println("Response status: " + status);
                                            System.out.println("Response body: " + body);

                                            if (status == 200) {
                                                return Mono.just(true);
                                            } else {
                                                return Mono.error(new RuntimeException("Error in conversion: " + body));
                                            }
                                        })
                                        .then(walletPointRepository.findByUserId(originalUserId)) // este se mantiene como el dueño del wallet
                                        .flatMap(wallet -> {
                                            wallet.setPoints(wallet.getPoints() + walletPointRequest.getRewardPoints());
                                            return walletPointRepository.save(wallet);
                                        })
                                        .flatMap(walletPoint -> walletPointHistoryRepository.save(
                                                WalletPointHistoryEntity.builder()
                                                        .userId(originalUserId)
                                                        .points(convertedPoints)
                                                        .walletPointId(walletPoint.getId())
                                                        .build()
                                        ));
                            });
                })
                .then();
    }
}
