package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.PointsTransferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTransferRepository;
import com.proriberaapp.ribera.services.PointsTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PointsTransferServiceImpl implements PointsTransferService {
    private final PointsTransferRepository pointsTransferRepository;

    @Autowired
    public PointsTransferServiceImpl(PointsTransferRepository pointsTransferRepository) {
        this.pointsTransferRepository = pointsTransferRepository;
    }

    @Override
    public Mono<PointsTransferEntity> createPointsTransfer(PointsTransferEntity pointsTransfer) {
        return pointsTransferRepository.save(pointsTransfer);
    }

    @Override
    public Mono<PointsTransferEntity> getPointsTransferById(Integer id) {
        return pointsTransferRepository.findById(id);
    }

    @Override
    public Flux<PointsTransferEntity> getAllPointsTransfers() {
        return pointsTransferRepository.findAll();
    }

    @Override
    public Mono<PointsTransferEntity> updatePointsTransfer(Integer id, PointsTransferEntity pointsTransfer) {
        return pointsTransferRepository.findById(id)
                .flatMap(existingPointsTransfer -> {
                    existingPointsTransfer.setSenderid(pointsTransfer.getSenderid());
                    existingPointsTransfer.setRequesttypeid(pointsTransfer.getRequesttypeid());
                    existingPointsTransfer.setDatetransfer(pointsTransfer.getDatetransfer());
                    existingPointsTransfer.setReceiverid(pointsTransfer.getReceiverid());
                    existingPointsTransfer.setPointstransfered(pointsTransfer.getPointstransfered());
                    existingPointsTransfer.setPointstypeid(pointsTransfer.getPointstypeid());
                    existingPointsTransfer.setSendandreceiveid(pointsTransfer.getSendandreceiveid());
                    return pointsTransferRepository.save(existingPointsTransfer);
                });
    }

    @Override
    public Mono<Void> deletePointsTransfer(Integer id) {
        return pointsTransferRepository.deleteById(id);
    }
}
