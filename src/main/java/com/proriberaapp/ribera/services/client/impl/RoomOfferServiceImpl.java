package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOfferFiltro;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.Infraestructure.viewRepository.RoomOfferViewRepository;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import com.proriberaapp.ribera.utils.TransformDate;

import ch.qos.logback.core.helpers.Transform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomOfferServiceImpl implements RoomOfferService {
    private final RoomOfferRepository roomOfferRepository;
    private final BookingRepository bookingRepository;
    private final RoomOfferViewRepository roomOfferViewRepository;
    private final ComfortRoomOfferDetailRepository comfortRoomOfferDetailRepository;
    private final QuotationRoomOfferRepository quotationRoomOfferRepository;
    private final ServicesRepository servicesRepository;
    private final BedroomRepository bedroomRepository;
    private final FeedingRepository feedingRepository;
    @Value("${room.offer.ratio.base}")
    private Integer RATIO_BASE;
    @Value("${room.offer.ratio.ribera}")
    private Integer RATIO_RIBERA;
    @Value("${room.offer.ratio.inresort}")
    private Integer RATIO_INRESORT;

    @Override
    public Mono<RoomOfferEntity> save(RoomOfferEntity entity) {
        /*
         * if (entity.getNumberDays() == null) {
         * entity.setPoints(calculatePoints(entity.getCost(), RATIO_BASE));
         * entity.setInResortPoints(calculatePoints(entity.getCost(), RATIO_INRESORT));
         * entity.setRiberaPoints(calculatePoints(entity.getCost(), RATIO_RIBERA));
         * } else {
         * entity.setPoints(null);
         * entity.setInResortPoints(null);
         * entity.setRiberaPoints(null);
         * }
         */
        entity.setPoints(null);
        entity.setInResortPoints(null);
        entity.setRiberaPoints(null);
        return roomOfferRepository.save(entity);
    }

    @Override
    public Flux<RoomOfferEntity> findByFilters(SearchFiltersRoomOfferFiltro filters) {
        return roomOfferRepository.findByFilters(
                filters.roomTypeId(),
                filters.capacity() != null ? filters.capacity().toString() : null,
                filters.offerTimeInit(),
                filters.offerTimeEnd());
    }

    @Override
    public Flux<ViewRoomOfferReturn> findFilteredV2(Integer roomTypeId, LocalDateTime offerTimeInit,
            LocalDateTime offerTimeEnd,
            Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
            Integer adultExtraCapacity, Integer infantCapacity, List<Integer> feedingsSelected) {
        Integer adultCapacityDefault = adultCapacity == 0 ? 1 : adultCapacity;
        Integer kidCapacityDefault = kidCapacity == 0 ? 1 : adultCapacity;
        Integer totalKidCapacity = kidCapacityDefault + infantCapacity;
        Integer totalAdultCapacity = adultCapacityDefault + adultMayorCapacity + adultExtraCapacity;
        Flux<FeedingEntity> feedings = this.feedingRepository.findAllById(feedingsSelected);
        return roomOfferRepository.findFilteredV2(roomTypeId, offerTimeInit, offerTimeEnd,
                totalKidCapacity,
                totalAdultCapacity)
                .filterWhen(roomOffer -> bookingRepository.findConflictingBookings(
                        roomOffer.getRoomOfferId(), offerTimeInit, offerTimeEnd)
                        .hasElements()
                        .map(hasConflicts -> !hasConflicts))
                .flatMap(roomOffer -> servicesRepository.findAllViewComfortReturn(roomOffer.getRoomOfferId())
                        .collectList()
                        .flatMap(comfortList -> {
                            roomOffer.setTotalPerson(TransformDate.calculatePersonsAdultAndKids(
                                    adultCapacityDefault, kidCapacityDefault, infantCapacity, adultExtraCapacity,
                                    adultMayorCapacity));
                            roomOffer.setListAmenities(comfortList);
                            BigDecimal totalCostKids = roomOffer.getKidcost()
                                    .multiply(BigDecimal.valueOf(kidCapacityDefault))
                                    .add(roomOffer.getInfantcost().multiply(BigDecimal.valueOf(infantCapacity)));
                            BigDecimal totalCostAdult = roomOffer.getAdultextracost()
                                    .multiply(BigDecimal.valueOf(adultExtraCapacity))
                                    .add(roomOffer.getAdultcost().multiply(
                                            BigDecimal.valueOf(
                                                    adultCapacityDefault)))
                                    .add(roomOffer.getAdultmayorcost().multiply(
                                            BigDecimal.valueOf(
                                                    adultMayorCapacity)));
                            roomOffer.setCosttotal(totalCostAdult.add(totalCostKids));
                            return bedroomRepository.findAllViewBedroomReturn(roomOffer.getRoomId())
                                    .collectList()
                                    .map(bedroomList -> {
                                        roomOffer.setListBedroomReturn(bedroomList);
                                        return roomOffer;
                                    });
                        }))
                .collectSortedList(Comparator.comparing(ViewRoomOfferReturn::getRoomOfferId))
                .flatMapMany(Flux::fromIterable)
                .flatMap(roomOffer -> feedings.collectList()
                        .map(feedingList -> {
                            BigDecimal totalCostFeeding = feedingList.stream().reduce(BigDecimal.ZERO,
                                    (subtotal, element) -> subtotal.add(element.getCost()), BigDecimal::add);
                            roomOffer.setCosttotal(roomOffer.getCosttotal().add(totalCostFeeding));
                            roomOffer.setListFeeding(feedingList);
                            return roomOffer;
                        }));

    }

    @Override
    public Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDateTime offerTimeInit,
            LocalDateTime offerTimeEnd,
            Integer infantCapacity, Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
            Integer adultExtra) {
        return roomOfferRepository.findFiltered(roomTypeId, offerTimeInit, offerTimeEnd, infantCapacity, kidCapacity,
                adultCapacity, adultMayorCapacity, adultExtra)
                .filterWhen(roomOffer -> bookingRepository.findConflictingBookings(
                        roomOffer.getRoomOfferId(), offerTimeInit, offerTimeEnd)
                        .hasElements()
                        .map(hasConflicts -> !hasConflicts))
                .flatMap(roomOffer -> servicesRepository.findAllViewComfortReturn(roomOffer.getRoomOfferId())
                        .collectList()
                        .flatMap(comfortList -> {
                            roomOffer.setListAmenities(comfortList);
                            return bedroomRepository.findAllViewBedroomReturn(roomOffer.getRoomId())
                                    .collectList()
                                    .map(bedroomList -> {
                                        roomOffer.setListBedroomReturn(bedroomList);
                                        return roomOffer;
                                    });
                        }))
                .collectSortedList(Comparator.comparing(ViewRoomOfferReturn::getRoomOfferId))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<RoomOfferEntity> saveAll(List<RoomOfferEntity> entity) {
        return roomOfferRepository.findAllByRoomIdInAndOfferTypeIdIn(entity, entity)
                .collectList()
                .flatMapMany(entities -> roomOfferRepository.saveAll(
                        entity.stream().peek(roomOfferEntity -> {
                            roomOfferEntity.setPoints(calculatePoints(roomOfferEntity.getCost(), RATIO_BASE));
                            roomOfferEntity
                                    .setInResortPoints(calculatePoints(roomOfferEntity.getCost(), RATIO_INRESORT));
                            roomOfferEntity.setRiberaPoints(calculatePoints(roomOfferEntity.getCost(), RATIO_RIBERA));
                        }).filter(entity1 -> !entities.contains(entity1)).toList()));
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
                            log.info("Comfort: {}", comfort);
                            service.setListAmenities(comfort);
                            return service;
                        }))
                .flatMap(service -> bedroomRepository.findAllViewBedroomReturn(service.getRoomId())
                        .collectList().map(bedroom -> {
                            service.setListBedroomReturn(bedroom);
                            return service;
                        }));
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bookingRepository.findAllByRoomOfferId(id)
                .hasElements()
                .flatMap(hasElements -> {
                    if (hasElements) {
                        return Mono.error(new IllegalArgumentException("Solo se puede poner inactiva una oferta, " +
                                "no se puede eliminar si tiene reservas"));
                    }
                    return quotationRoomOfferRepository.deleteAllByRoomOfferId(id)
                            .then(comfortRoomOfferDetailRepository.deleteAllByRoomOfferId(id)
                                    .then(roomOfferRepository.deleteById(id)));
                });
    }

    @Override
    public Mono<RoomOfferEntity> update(RoomOfferEntity entity) {
        return roomOfferRepository.findByRoomIdAndState(entity.getRoomId(), 1)
                .flatMap(existingActiveOffer -> {
                    if (!existingActiveOffer.getRoomOfferId().equals(entity.getRoomOfferId())) {
                        return Mono.error(
                                new IllegalArgumentException("Ya existe una oferta activa para este alojamiento."));
                    }
                    return quotationRoomOfferRepository.findAllByRoomOfferId(entity.getRoomOfferId())
                            .hasElements()
                            .flatMap(hasElements -> {
                                if (!hasElements && entity.getState() == 1) {
                                    return Mono.error(new IllegalArgumentException(
                                            "No se puede activar una oferta que no tiene cotizaciones"));
                                }
                                return roomOfferRepository.save(entity);
                            });
                })
                .switchIfEmpty(
                        quotationRoomOfferRepository.findAllByRoomOfferId(entity.getRoomOfferId())
                                .hasElements()
                                .flatMap(hasElements -> {
                                    if (!hasElements && entity.getState() == 1) {
                                        return Mono.error(new IllegalArgumentException(
                                                "No se puede activar una oferta que no tiene cotizaciones"));
                                    }
                                    return roomOfferRepository.save(entity);
                                }));
    }

    private Integer calculatePoints(BigDecimal price, Integer points) {
        return price.intValue() / points;
    }
}
