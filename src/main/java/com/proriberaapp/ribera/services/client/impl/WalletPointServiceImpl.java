package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
import com.proriberaapp.ribera.Domain.mapper.WalletPointMapper;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointRepository;
import com.proriberaapp.ribera.services.client.PointsTypeService;
import com.proriberaapp.ribera.services.client.WalletPointService;
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
    private final PointsTypeService pointsTypeService;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<WalletPointResponse> createWalletPoint(WalletPointRequest walletPointRequest) {
        return calculatePoints(walletPointRequest)
                .flatMap(calculatedPoints -> {
                    walletPointRequest.setPoints(calculatedPoints);
                    return walletPointRepository.save(walletPointMapper.toEntity(walletPointRequest))
                            .map(walletPointMapper::toDto);
                })
                .doOnNext(response -> log.info("Successfully created wallet point: {}", response))
                .doOnError(e -> log.error("Error creating wallet point", e))
                .onErrorResume(e -> Mono.error(new RequestException("Error creating wallet point" + e.getMessage())))
                .as(transactionalOperator::transactional);
    }

    private Mono<Double> calculatePoints(WalletPointRequest request) {
        if (request.getPoints() == null) {
            return Mono.just(0.0);
        }
        return getFactorForPointType(request.getPointTypeId())
                .map(factor -> request.getPoints() * factor);
    }

    private Mono<Double> getFactorForPointType(Integer pointTypeId) {
        return pointsTypeService.getPointsTypeById(pointTypeId)
                .map(pointType -> {
                    if (Objects.nonNull(pointType)) {
                        return pointType.getFactor();
                    } else {
                        return 0.0;
                    }
                });
    }
}