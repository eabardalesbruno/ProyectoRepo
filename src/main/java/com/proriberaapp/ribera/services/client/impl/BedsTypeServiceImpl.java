package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BedroomRepository;
import com.proriberaapp.ribera.Infraestructure.repository.BedsTypeRepository;
import com.proriberaapp.ribera.services.client.BedsTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedsTypeServiceImpl implements BedsTypeService {
    private final BedsTypeRepository bedsTypeRepository;
    private final BedroomRepository bedroomRepository;

    @Override
    public Mono<BedsTypeEntity> save(BedsTypeEntity entity) {
        return bedsTypeRepository.save(entity);
    }

    @Override
    public Flux<BedsTypeEntity> saveAll(List<BedsTypeEntity> entity) {
        return bedsTypeRepository.saveAll(entity);
    }

    @Override
    public Mono<BedsTypeEntity> findById(Integer id) {
        return bedsTypeRepository.findById(id);
    }

    @Override
    public Flux<BedsTypeEntity> findAll() {
        return bedsTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bedroomRepository.findAllByBedTypeId(id).hasElements().flatMap(hasElements -> {
            if (hasElements) {
                return Mono.error(new RuntimeException("Se esta usando la cama en las habitaciones, no se puede eliminar"));
            }
            return bedsTypeRepository.deleteById(id);
        });
    }

    @Override
    public Mono<BedsTypeEntity> update(BedsTypeEntity entity) {
        return bedsTypeRepository.save(entity);
    }
}
