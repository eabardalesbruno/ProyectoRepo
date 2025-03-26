package com.proriberaapp.ribera.utils;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Domain.entities.WalletPointHistoryEntity;
import com.proriberaapp.ribera.services.client.PointsTypeService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class WalletPointUtils {
    private final PointsTypeService pointsTypeService;

    public WalletPointUtils(PointsTypeService pointsTypeService) {
        this.pointsTypeService = pointsTypeService;
    }

    public Mono<Double> calculatePoints(WalletPointRequest request) {
        if (request.getRewardPoints() == null) {
            return Mono.just(0.0);
        }
        return getFactorForPointType(request.getPointTypeId())
                .map(factor -> request.getRewardPoints() * factor);
    }

    public Mono<Double> getFactorForPointType(Integer pointTypeId) {
        return pointsTypeService.getPointsTypeById(pointTypeId)
                .map(pointType -> Objects.nonNull(pointType) ? pointType.getFactor() : 0.0);
    }

    public WalletPointHistoryEntity buildWalletHistory(Integer userId, Double points) {
        return WalletPointHistoryEntity.builder()
                .userId(userId)
                .points(points)
                .build();
    }
}
