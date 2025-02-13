package com.proriberaapp.ribera.services.point;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proriberaapp.ribera.Domain.entities.PointTransactionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointTransactionRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PointTransactionTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTypeRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PointsTransactionService {
    private final Map<PointTransactionTypeEnum, PointsTransactionStrategy<?>> strategies;
    private final PointTransactionRepository pointTransactionRepository;
    private final PointTransactionTypeRepository pointsTransactionTypeRepository;

    @SuppressWarnings("unchecked")
    @Transactional
    public <T extends PointTransactionRequestDto> Mono<T> processTransaction(T request) {
        PointsTransactionStrategy<T> strategy = (PointsTransactionStrategy<T>) strategies.get(request.getType());
        if (strategy == null) {
            return Mono.error(new IllegalArgumentException("Invalid transaction type"));
        }
        return this.pointsTransactionTypeRepository.findByName(request.getType().getDescription())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Not found transaction type")))
                .flatMap(transactionType -> {
                    PointTransactionEntity pointTransactionEntity = PointTransactionEntity.builder()
                            .userid(request.getUserId())
                            .transactiontypeid(transactionType.getId())
                            .build();
                    return this.pointTransactionRepository.save(pointTransactionEntity);
                })
                .flatMap(requestMap -> {
                    request.setTransactionId(requestMap.getId());
                    return strategy.execute(request)
                            .onErrorResume(ex -> Mono.error(new RuntimeException("Error processing transaction", ex)));
                });

    }

    // Método helper para transferencias
    public Mono<PointTransferRequestDto> transferPoints(PointTransferRequestDto request) {
        request.setType(PointTransactionTypeEnum.TRANSFER);
        return processTransaction(request);
    }

    public Mono<PointConversionDto> convertPoints(PointConversionDto request) {
        request.setType(PointTransactionTypeEnum.EXCHANGE);
        return processTransaction(request);
    }

}
