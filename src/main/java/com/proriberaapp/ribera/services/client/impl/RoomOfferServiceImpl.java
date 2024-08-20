package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOfferFiltro;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BedroomRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferRepository;
import com.proriberaapp.ribera.Infraestructure.repository.ServicesRepository;
import com.proriberaapp.ribera.Infraestructure.viewRepository.RoomOfferViewRepository;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomOfferServiceImpl implements RoomOfferService {
    private final RoomOfferRepository roomOfferRepository;
    private final RoomOfferViewRepository roomOfferViewRepository;
    private final ServicesRepository servicesRepository;
    private final BedroomRepository bedroomRepository;
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
    public Flux<RoomOfferEntity> findByFilters(SearchFiltersRoomOfferFiltro filters) {
        return roomOfferRepository.findByFilters(
                filters.roomTypeId(),
                filters.capacity() != null ? filters.capacity().toString() : null,
                filters.offerTimeInit(),
                filters.offerTimeEnd()
        );
    }

    @Override
    public Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDateTime offerTimeInit, LocalDateTime offerTimeEnd, Integer capacity) {
        return roomOfferRepository.findFiltered(roomTypeId, offerTimeInit, offerTimeEnd, capacity)
                .flatMap(roomOffer ->
                        servicesRepository.findAllViewComfortReturn(roomOffer.getRoomOfferId())
                                .collectList()
                                .flatMap(comfortList -> {
                                    roomOffer.setListAmenities(comfortList);
                                    return bedroomRepository.findAllViewBedroomReturn(roomOffer.getRoomId())
                                            .collectList()
                                            .map(bedroomList -> {
                                                roomOffer.setListBedroomReturn(bedroomList);
                                                return roomOffer;
                                            });
                                })
                );
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
        return roomOfferViewRepository.viewRoomOfferReturn(filters)
                .flatMap(service -> servicesRepository.findAllViewComfortReturn(service.getRoomOfferId())
                        .collectList().map(comfort -> {
                            service.setListAmenities(comfort);
                            return service;
                        })
                )
                .flatMap(service -> bedroomRepository.findAllViewBedroomReturn(service.getRoomId())
                        .collectList().map(bedroom -> {
                            service.setListBedroomReturn(bedroom);
                            return service;
                        })
                );
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
