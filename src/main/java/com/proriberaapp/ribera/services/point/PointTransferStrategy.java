package com.proriberaapp.ribera.services.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.PointTransferEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTransferRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;

import reactor.core.publisher.Mono;

@Service
public class PointTransferStrategy implements PointsTransactionStrategy<PointTransferRequestDto> {
    @Autowired
    private PointsTransferRepository pointTransferRepository;

    @Autowired
    private UserClientRepository userClientRepository;

    @Override
    public Mono<PointTransferRequestDto> execute(PointTransferRequestDto request) {
        PointTransferEntity pointTransferEntity = PointTransferEntity.builder()
                .receiveruserid(request.getTargetUserId())
                .senderuserid(request.getSourceUserId())
                .pointsamount(request.getPointsAmount())
                .transactionid(request.getTransactionId())
                .build();
        if (request.getSourceUserId() == request.getTargetUserId()) {
            return Mono.error(new IllegalArgumentException("Los usuarios no pueden ser iguales"));
        }
   /*      Mono.zip(this.userClientRepository.findById(request.getSourceUserId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario origen no encontrado"))),
                this.userClientRepository.findById(request.getTargetUserId()))
                .flatMap(tuples -> {
                    UserClientEntity user = tuples.getT1();
                    UserClientEntity userTarget = tuples.getT2();
                    if (user.getRewardPoints() < request.getPointsAmount()) {
                        return Mono.error(new IllegalArgumentException("El usuario no tiene suficientes puntos"));
                    }
                    user.setRewardPoints(user.getRewardPoints() - request.getPointsAmount());
                    userTarget.setRewardPoints(userTarget.getRewardPoints() + request.getPointsAmount());
                    return this.userClientRepository.save(user)
                            .then(this.userClientRepository.save(userTarget));
                }); */

        return this.pointTransferRepository.save(pointTransferEntity).map(d -> {
            request.setId(d.getId());
            return request;
        });

    }

}
