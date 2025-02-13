package com.proriberaapp.ribera.services.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.PointTransferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTransferRepository;

import reactor.core.publisher.Mono;

@Service
public class PointTransferStrategy implements PointsTransactionStrategy<PointTransferRequestDto> {
    @Autowired
    private PointsTransferRepository pointTransferRepository;

    @Override
    public Mono<PointTransferRequestDto> execute(PointTransferRequestDto request) {
        PointTransferEntity pointTransferEntity = PointTransferEntity.builder()
                .receiveruserid(request.getTargetUserId())
                .senderuserid(request.getSourceUserId())
                .pointsamount(request.getPointsAmount())
                .transactionid(request.getTransactionId())
                .build();
        return this.pointTransferRepository.save(pointTransferEntity).map(d -> {
            request.setId(d.getId());
            return request;
        });

    }

}
