package com.proriberaapp.ribera.services.point;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.PointTransactionConversionEntity;
import com.proriberaapp.ribera.Infraestructure.exception.UserNotFoundException;
import com.proriberaapp.ribera.Infraestructure.repository.PointConversionRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PointConversionStrategy implements PointsTransactionStrategy<PointConversionDto> {
    private final PointConversionRepository pointConversionRepository;
    private final UserClientRepository userClientRepository;

    @Override
    public Mono<PointConversionDto> execute(PointConversionDto request) {
        double pointAcredited = request.getPointType().getFactor() * request.getPointDebited();
        PointTransactionConversionEntity entity = PointTransactionConversionEntity.builder()
                .membershipname(request.getMembershipName())
                .pointdebited(request.getPointDebited())
                .pointacredited(pointAcredited)
                .userid(request.getUserId())
                .pointtypeid(request.getPointType().getPointstypeid())
                .transactionid(request.getTransactionId())
                .created_at(LocalDateTime.now())
                .build();
        request.setPointAcredited(pointAcredited);
        // Llamada para sacar los puntos ,para saber si el usuario tiene puntos a
        // cambiar
        Mono<PointConversionDto> savePointConversion = Mono.defer(() -> {
            return this.pointConversionRepository.save(entity).map(d -> {
                request.setId(d.getId());
                return request;
            });
        });
        return this.userClientRepository.findById(request.getUserId())
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(user -> {
                    double pointsAcredited = user.getRewardPoints() + request.getPointAcredited();
                    user.setRewardPoints(pointsAcredited);
                    return this.userClientRepository.save(user).then(savePointConversion);
                });

    }

}
