package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.StateRoomRepository;
import com.proriberaapp.ribera.Infraestructure.services.StateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateRoomServiceImpl implements StateRoomService {
    private final StateRoomRepository stateRoomRepository;
    @Override
    public Mono<StateRoomEntity> save(StateRoomEntity entity) {
        return stateRoomRepository.save(entity);
    }

    @Override
    public Flux<StateRoomEntity> saveAll(Flux<StateRoomEntity> entity) {
        return stateRoomRepository.saveAll(entity);
    }

    @Override
    public Mono<StateRoomEntity> findById(Integer id) {
        return stateRoomRepository.findById(id);
    }

    @Override
    public Flux<StateRoomEntity> findAll() {
        return stateRoomRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return stateRoomRepository.deleteById(id);
    }

    @Override
    public Mono<StateRoomEntity> update(StateRoomEntity entity) {
        return stateRoomRepository.save(entity);
    }
}
