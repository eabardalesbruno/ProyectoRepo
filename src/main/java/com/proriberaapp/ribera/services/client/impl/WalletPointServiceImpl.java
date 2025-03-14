package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
import com.proriberaapp.ribera.Domain.entities.WalletPointHistoryEntity;
import com.proriberaapp.ribera.Domain.mapper.WalletPointMapper;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointHistoryRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointRepository;
import com.proriberaapp.ribera.services.client.PointsTypeService;
import com.proriberaapp.ribera.services.client.WalletPointService;
import com.proriberaapp.ribera.utils.WalletPointUtils;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletPointServiceImpl implements WalletPointService {
    private final WalletPointRepository walletPointRepository;
    private final WalletPointMapper walletPointMapper;
    private final TransactionalOperator transactionalOperator;
    private final WalletPointHistoryRepository walletPointHistoryRepository;
    private final WalletPointUtils walletPointUtils;

    @Override
    public Mono<WalletPointResponse> createWalletPoint(WalletPointRequest walletPointRequest) {
        return walletPointUtils.calculatePoints(walletPointRequest)
                .flatMap(calculatedPoints -> {
                    walletPointRequest.setPoints(calculatedPoints);
                    return walletPointRepository.save(walletPointMapper.toEntity(walletPointRequest))
                            .map(walletPointMapper::toDto);
                })
                .doOnNext(response -> log.info("Successfully created wallet point: {}", response))
                .doOnError(e -> log.error("Error creating wallet point", e))
                .onErrorResume(e -> Mono.error(new RequestException("Error creating wallet point: " + e.getMessage())))
                .as(transactionalOperator::transactional);
    }

    public Mono<WalletPointResponse> updateWalletPoints(Integer userId, Integer points) {
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


    private Mono<Void> saveWalletHistory(Integer userId, Integer points) {
        WalletPointHistoryEntity history = walletPointUtils.buildWalletHistory(userId, points);
        return walletPointHistoryRepository.save(history)
                .doOnNext(saved -> log.info("Wallet history saved for user: {}", userId))
                .then();
    }

    public Mono<WalletPointResponse> getWalletByUserId(Integer userId) {
        return walletPointRepository.findByUserId(userId)
                .map(walletPointMapper::toDto)
                .switchIfEmpty(Mono.error(new RequestException("Wallet not found for user: " + userId)))
                .doOnError(e -> log.error("Error fetching wallet for user: {}", userId))
                .onErrorResume(e -> Mono.error(new RequestException("Error fetching wallet: " + e.getMessage())));
    }
}
