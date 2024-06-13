package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferRepository;
import com.proriberaapp.ribera.Infraestructure.viewRepository.RoomOfferViewRepository;
import com.proriberaapp.ribera.services.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomOfferServiceImpl implements RoomOfferService {
    private final RoomOfferRepository roomOfferRepository;
    private final RoomOfferViewRepository roomOfferViewRepository;
    @Value("${room.offer.ratio.base}")
    private Integer RATIO_BASE;
    @Value("${room.offer.ratio.ribera}")
    private Integer RATIO_RIBERA;
    @Value("${room.offer.ratio.inresort}")
    private Integer RATIO_INRESORT;

    @Override
    public Mono<RoomOfferEntity> save(RoomOfferEntity entity) {
        entity.setPoints(calculatePoints(entity.getCost(), RATIO_BASE));
        entity.setInResortPoints(calculatePoints(entity.getCost(), RATIO_INRESORT));
        entity.setRiberaPoints(calculatePoints(entity.getCost(), RATIO_RIBERA));

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
                        entity.stream().peek(roomOfferEntity -> {
                            roomOfferEntity.setPoints(calculatePoints(roomOfferEntity.getCost(), RATIO_BASE));
                            roomOfferEntity.setInResortPoints(calculatePoints(roomOfferEntity.getCost(), RATIO_INRESORT));
                            roomOfferEntity.setRiberaPoints(calculatePoints(roomOfferEntity.getCost(), RATIO_RIBERA));
                        }).filter(entity1 -> !entities.contains(entity1)).toList()
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
    public Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters) {
        return roomOfferViewRepository.viewRoomOfferReturn(filters);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomOfferRepository.deleteById(id);
    }

    @Override
    public Mono<RoomOfferEntity> update(RoomOfferEntity entity) {
        return roomOfferRepository.save(entity);
    }

    private Integer calculatePoints(BigDecimal price, Integer points) {
        return price.intValue() / points;
    }
}
