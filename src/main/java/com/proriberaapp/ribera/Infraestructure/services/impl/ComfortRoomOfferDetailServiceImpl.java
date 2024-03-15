package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ComfortRoomOfferDetailRepository;
import com.proriberaapp.ribera.Infraestructure.services.BaseService;
import com.proriberaapp.ribera.Infraestructure.services.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComfortRoomOfferDetailServiceImpl implements ComfortRoomOfferDetailService {
    private final ComfortRoomOfferDetailRepository comfortRoomOfferDetailRepository;
    @Override
    public Mono<ComfortRoomOfferDetailEntity> save(ComfortRoomOfferDetailEntity entity) {
        return comfortRoomOfferDetailRepository.save(entity);
    }

    @Override
    public Flux<ComfortRoomOfferDetailEntity> saveAll(Flux<ComfortRoomOfferDetailEntity> entity) {
        return comfortRoomOfferDetailRepository.saveAll(entity);
    }

    @Override
    public Mono<ComfortRoomOfferDetailEntity> findById(Integer id) {
        return comfortRoomOfferDetailRepository.findById(id);
    }

    @Override
    public Flux<ComfortRoomOfferDetailEntity> findAll() {
        return comfortRoomOfferDetailRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return comfortRoomOfferDetailRepository.deleteById(id);
    }

    @Override
    public Mono<ComfortRoomOfferDetailEntity> update(ComfortRoomOfferDetailEntity entity) {
        return comfortRoomOfferDetailRepository.save(entity);
    }
}
