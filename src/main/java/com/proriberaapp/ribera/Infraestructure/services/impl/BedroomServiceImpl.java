package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BedroomRepository;
import com.proriberaapp.ribera.Infraestructure.services.BedroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedroomServiceImpl implements BedroomService {
    private final BedroomRepository bedroomRepository;
    @Override
    public Mono<BedroomEntity> save(BedroomEntity entity) {
        return bedroomRepository.save(entity);
    }

    @Override
    public Flux<BedroomEntity> saveAll(List<BedroomEntity> entity) {
        return bedroomRepository.saveAll(entity);
    }

    @Override
    public Mono<BedroomEntity> findById(Integer id) {
        return bedroomRepository.findById(id);
    }

    @Override
    public Flux<BedroomEntity> findAll() {
        return bedroomRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bedroomRepository.deleteById(id);
    }

    @Override
    public Mono<BedroomEntity> update(BedroomEntity entity) {
        return bedroomRepository.save(entity);
    }
}
