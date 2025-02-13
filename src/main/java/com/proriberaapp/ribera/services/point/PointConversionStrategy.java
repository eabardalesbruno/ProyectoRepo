package com.proriberaapp.ribera.services.point;

import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.PointConversionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointConversionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PointConversionStrategy implements PointsTransactionStrategy<PointConversionDto> {
    private final PointConversionRepository pointConversionRepository;

    @Override
    public Mono<PointConversionDto> execute(PointConversionDto request) {
        double pointAcredited = request.getPointType().getFactor() * request.getPointDebited();
        PointConversionEntity entity = PointConversionEntity.builder()
                .membreshipname(request.getMembershipName())
                .pointdebited(request.getPointDebited())
                .pointacredited(pointAcredited)
                .userid(request.getUserId())
                .pointtypeid(request.getPointType().getPointstypeid())
                .transactionid(request.getTransactionId())
                .build();
        return this.pointConversionRepository.save(entity).map(d -> {
            request.setId(d.getId());
            return request;
        });
    }

}
