package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferRepository;
import com.proriberaapp.ribera.Infraestructure.services.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomOfferServiceImpl implements RoomOfferService {
    private final RoomOfferRepository roomOfferRepository;
    @Override
    public Mono<RoomOfferEntity> save(RoomOfferEntity entity) {
        return roomOfferRepository.findByRoomIdAndOfferTypeId(entity.getRoomId(), entity.getOfferTypeId()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("RoomOffer already exists"))
                        : roomOfferRepository.save(entity));
    }

    @Override
    public Flux<RoomOfferEntity> saveAll(List<RoomOfferEntity> entity) {
        return roomOfferRepository.findAllByRoomIdInAndOfferTypeIdIn(entity, entity)
                .collectList()
                .flatMapMany(entities -> roomOfferRepository.saveAll(
                        entity.stream().filter(entity1 -> !entities.contains(entity1)).toList()
                ));
    }

    @Override
    public Mono<RoomOfferEntity> findById(Integer id) {
        return roomOfferRepository.findById(id);
    }

    @Override
    public Flux<RoomOfferEntity> findAll() {
        return roomOfferRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomOfferRepository.deleteById(id);
    }

    @Override
    public Mono<RoomOfferEntity> update(RoomOfferEntity entity) {
        return roomOfferRepository.save(entity);
    }
}
