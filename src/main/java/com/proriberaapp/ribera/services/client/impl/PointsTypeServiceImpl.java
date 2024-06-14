package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PointsTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTypeRepository;
import com.proriberaapp.ribera.services.client.PointsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PointsTypeServiceImpl implements PointsTypeService {
    private final PointsTypeRepository pointsTypeRepository;

    @Autowired
    public PointsTypeServiceImpl(PointsTypeRepository pointsTypeRepository) {
        this.pointsTypeRepository = pointsTypeRepository;
    }

    @Override
    public Mono<PointsTypeEntity> createPointsType(PointsTypeEntity pointsType) {
        return pointsTypeRepository.save(pointsType);
    }

    @Override
    public Mono<PointsTypeEntity> getPointsTypeById(Integer id) {
        return pointsTypeRepository.findById(id);
    }

    @Override
    public Flux<PointsTypeEntity> getAllPointsTypes() {
        return pointsTypeRepository.findAll();
    }

    @Override
    public Mono<PointsTypeEntity> updatePointsType(Integer id, PointsTypeEntity pointsType) {
        return pointsTypeRepository.findById(id)
                .flatMap(existingPointsType -> {
                    existingPointsType.setPointstypedesc(pointsType.getPointstypedesc());
                    return pointsTypeRepository.save(existingPointsType);
                });
    }

    @Override
    public Flux<PointsTypeEntity> findByPointstypedescContaining(String keyword) {
        return pointsTypeRepository.findByPointstypedescContaining(keyword);
    }

    @Override
    public Mono<Void> deletePointsType(Integer id) {
        return pointsTypeRepository.deleteById(id);
    }
}