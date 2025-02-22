package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.FeedingItemsGrouped;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOfferFiltro;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.dto.PointGroupWithOffertRowDto;
import com.proriberaapp.ribera.Domain.dto.QuotationOfferDto;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.Infraestructure.viewRepository.RoomOfferViewRepository;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import com.proriberaapp.ribera.services.discount.DiscountForPoint;
import com.proriberaapp.ribera.utils.GeneralMethods;
import com.proriberaapp.ribera.utils.TransformDate;

import ch.qos.logback.core.helpers.Transform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        private final PointsTypeConversionFactorDayRepository pointsTypeConversionFactorDayRepository;
        @Value("${room.offer.ratio.base}")
        private Integer RATIO_BASE;
        @Value("${room.offer.ratio.ribera}")
        private Integer RATIO_RIBERA;
        @Value("${room.offer.ratio.inresort}")
        private Integer RATIO_INRESORT;
        private final DiscountForPoint discountForPoint;

        @Override
        public Mono<RoomOfferEntity> save(RoomOfferEntity entity) {
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
        public Flux<ViewRoomOfferReturn> findFilteredV2(Integer roomTypeId,
                        String categoryName, LocalDate offerTimeInit,
                        LocalDate offerTimeEnd,
                        Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
                        Integer adultExtraCapacity, Integer infantCapacity, List<Integer> feedingsSelected,
                        boolean isFirstState) {
                int totalCapacityWithOutInfant = kidCapacity + adultCapacity + adultMayorCapacity + adultExtraCapacity;
                Flux<FeedingItemsGrouped> feedingsGrouped = Flux.defer(() -> {
                        if (feedingsSelected.size() > 0) {
                                return this.feedingRepository.groupingByFamilyGroup(feedingsSelected);
                        }
                        return Flux.empty();
                });
                Flux<FeedingEntity> feedings = Flux.defer(() -> {
                        if (feedingsSelected.size() > 0) {
                                return this.feedingRepository.findAllById(feedingsSelected);
                        }
                        return Flux.empty();
                });
                Mono<List<QuotationOfferDto>> quotationOfferDto = Mono.defer(() -> {
                        return this.roomOfferRepository
                                        .getQuotationByRangeDateAndRoomOfferId(offerTimeInit, offerTimeEnd)
                                        .collectList();
                });
                Mono<List<PointGroupWithOffertRowDto>> listPointGroup = Mono.defer(() -> {
                        return this.pointsTypeConversionFactorDayRepository
                                        .getTotalPointWithRangeDateSelected(offerTimeInit, offerTimeEnd).collectList();
                });
                Mono<List<ViewRoomOfferReturn>> viewReturn = Mono.defer(() -> {
                        return roomOfferRepository.findFilteredV2(
                                        isFirstState,
                                        roomTypeId,
                                        categoryName, offerTimeInit, offerTimeEnd,
                                        kidCapacity, adultCapacity, adultMayorCapacity, adultExtraCapacity,
                                        infantCapacity)
                                        .filterWhen(roomOffer -> bookingRepository.findConflictingBookings(
                                                        roomOffer.getRoomOfferId(), offerTimeInit, offerTimeEnd)
                                                        .hasElements()
                                                        .map(hasConflicts -> !hasConflicts))
                                        .collectList();
                });
                /*
                 * return Mono.zip(viewReturn, quotationOfferDto)
                 * .flatMap(tuple -> {
                 * List<ViewRoomOfferReturn> returnView = tuple.getT1();
                 * List<QuotationOfferDto> quotationOfferDtos = tuple.getT2();
                 * 
                 * for (ViewRoomOfferReturn viewRoomOfferReturn : returnView) {
                 * QuotationOfferDto quotation = quotationOfferDtos.stream()
                 * .filter(quotationOfferDto1 -> quotationOfferDto1
                 * .getRoom_offer_id()
                 * .equals(viewRoomOfferReturn
                 * .getRoomOfferId()))
                 * .findFirst().orElse(null);
                 * 
                 * if (quotation != null) {
                 * 
                 * viewRoomOfferReturn.setAdultcost(
                 * BigDecimal.valueOf(quotation.getAdult_cost()
                 * / viewRoomOfferReturn
                 * .getNumberofnights()));
                 * viewRoomOfferReturn.setKidcost(
                 * BigDecimal.valueOf(quotation.getKid_cost()
                 * / viewRoomOfferReturn
                 * .getNumberofnights()));
                 * viewRoomOfferReturn.setAdultextracost(
                 * BigDecimal.valueOf(quotation
                 * .getAdult_extra_cost()
                 * / viewRoomOfferReturn
                 * .getNumberofnights()));
                 * viewRoomOfferReturn.setAdultmayorcost(
                 * BigDecimal.valueOf(quotation
                 * .getAdult_mayor_cost()
                 * / viewRoomOfferReturn
                 * .getNumberofnights()));
                 * viewRoomOfferReturn.setInfantcost(
                 * BigDecimal.valueOf(quotation.getInfant_cost()
                 * / viewRoomOfferReturn
                 * .getNumberofnights()));
                 * 
                 * }
                 * }
                 * return Mono.just(returnView);
                 * 
                 * }).flatMapMany(Flux::fromIterable)
                 * .map(roomOffer -> {
                 * roomOffer.setKidsReserve(kidCapacity);
                 * roomOffer.setAdultsReserve(
                 * isFirstState ? roomOffer.getMincapacity() : adultCapacity);
                 * roomOffer.setAdultsMayorReserve(adultMayorCapacity);
                 * roomOffer.setAdultsExtraReserve(adultExtraCapacity);
                 * roomOffer.setInfantsReserve(infantCapacity);
                 * roomOffer.setTotalPerson(TransformDate.calculatePersons(
                 * roomOffer.getAdultsReserve(),
                 * roomOffer.getKidsReserve(), roomOffer.getInfantsReserve(),
                 * roomOffer.getAdultsExtraReserve(),
                 * roomOffer.getAdultsMayorReserve()));
                 * 
                 * BigDecimal totalCostPerson = roomOffer.getAdultextracost().multiply(
                 * BigDecimal.valueOf(roomOffer.getAdultsExtraReserve()))
                 * .add(roomOffer.getAdultmayorcost()
                 * .multiply(BigDecimal.valueOf(roomOffer
                 * .getAdultsMayorReserve())))
                 * .add(roomOffer.getAdultcost()
                 * .multiply(BigDecimal.valueOf(
                 * roomOffer.getAdultsReserve())))
                 * .add(roomOffer.getKidcost().multiply(BigDecimal
                 * .valueOf(roomOffer.getKidsReserve())));
                 * roomOffer.setTotalCapacity(roomOffer.getAdultcapacity()
                 * + roomOffer.getKidcapacity()
                 * + roomOffer.getAdultextra()
                 * + roomOffer.getAdultmayorcapacity());
                 * totalCostPerson = totalCostPerson
                 * .multiply(BigDecimal.valueOf(roomOffer.getNumberofnights()));
                 * roomOffer.setCosttotal(totalCostPerson);
                 * return roomOffer;
                 * })
                 * .flatMap(roomOffer -> servicesRepository
                 * .findAllViewComfortReturn(roomOffer.getRoomOfferId())
                 * .collectList()
                 * .flatMap(comfortList -> {
                 * roomOffer.setListAmenities(comfortList);
                 * return bedroomRepository
                 * .findAllViewBedroomReturn(roomOffer.getRoomId())
                 * .collectList()
                 * .map(bedroomList -> {
                 * roomOffer.setListBedroomReturn(
                 * bedroomList);
                 * return roomOffer;
                 * });
                 * }))
                 * .collectSortedList(Comparator.comparing(ViewRoomOfferReturn::getRoomOfferId))
                 * .flatMapMany(Flux::fromIterable)
                 * .flatMap(roomOffer -> Mono.zip(feedings
                 * .collectList(), feedingsGrouped.collectList())
                 * .map(zipMono -> {
                 * List<FeedingEntity> feedingList = zipMono.getT1();
                 * List<FeedingItemsGrouped> feedingGroupedList = zipMono.getT2();
                 * 
                 * 
                 * Integer totalPerson = roomOffer.getAdultsReserve()
                 * + roomOffer.getAdultsExtraReserve()
                 * + roomOffer.getAdultsMayorReserve()
                 * + roomOffer.getKidsReserve();
                 * BigDecimal totalCostFeeding = GeneralMethods
                 * .calculatedTotalAmountFeeding(feedingList,
                 * feedingGroupedList,
                 * roomOffer.getAdultsReserve(),
                 * roomOffer.getAdultsExtraReserve(),
                 * roomOffer.getAdultsMayorReserve(),
                 * roomOffer.getKidsReserve());
                 * roomOffer.setCosttotal(
                 * roomOffer.getCosttotal().add(totalCostFeeding));
                 * roomOffer.setListFeeding(feedingList);
                 * roomOffer.setAmountFeeding(totalCostFeeding);
                 * return roomOffer;
                 * }));
                 */ return Mono.zip(viewReturn, quotationOfferDto, feedings
                                .collectList(),
                                feedingsGrouped.collectList(),
                                listPointGroup)
                                .flatMap(tuple -> {
                                        List<ViewRoomOfferReturn> returnView = tuple.getT1();
                                        List<QuotationOfferDto> quotationOfferDtos = tuple.getT2();
                                        List<FeedingEntity> feedingList = tuple.getT3();
                                        List<FeedingItemsGrouped> feedingGroupedList = tuple.getT4();
                                        List<PointGroupWithOffertRowDto> pointGroupWithOffertRowDtos = tuple.getT5();
                                        for (ViewRoomOfferReturn viewRoomOfferReturn : returnView) {
                                                Double totalPointRibera = pointGroupWithOffertRowDtos.stream().filter(
                                                                pointGroupWithOffertRowDto -> pointGroupWithOffertRowDto
                                                                                .getOfferttypeid()
                                                                                .equals(viewRoomOfferReturn
                                                                                                .getOffertypeid())
                                                                                && pointGroupWithOffertRowDto
                                                                                                .getPointstypedesc()
                                                                                                .equals(
                                                                                                                "Ribera"))
                                                                .mapToDouble(PointGroupWithOffertRowDto::getPoint)
                                                                .sum();
                                                Double totalPointRiberaTop = pointGroupWithOffertRowDtos
                                                                .stream().filter(
                                                                                pointGroupWithOffertRowDto -> pointGroupWithOffertRowDto
                                                                                                .getOfferttypeid()
                                                                                                .equals(viewRoomOfferReturn
                                                                                                                .getOffertypeid())
                                                                                                && pointGroupWithOffertRowDto
                                                                                                                .getPointstypedesc()
                                                                                                                .contains(
                                                                                                                                "Ribera top"))
                                                                .mapToDouble(PointGroupWithOffertRowDto::getPoint)
                                                                .sum();
                                                viewRoomOfferReturn.setKidsReserve(kidCapacity);
                                                viewRoomOfferReturn.setAdultsReserve(
                                                                isFirstState ? viewRoomOfferReturn.getMincapacity()
                                                                                : adultCapacity);
                                                viewRoomOfferReturn.setAdultsMayorReserve(adultMayorCapacity);
                                                viewRoomOfferReturn.setAdultsExtraReserve(adultExtraCapacity);
                                                viewRoomOfferReturn.setInfantsReserve(infantCapacity);
                                                viewRoomOfferReturn.setTotalPerson(TransformDate.calculatePersons(
                                                                viewRoomOfferReturn.getAdultsReserve(),
                                                                viewRoomOfferReturn.getKidsReserve(),
                                                                viewRoomOfferReturn.getInfantsReserve(),
                                                                viewRoomOfferReturn.getAdultsExtraReserve(),
                                                                viewRoomOfferReturn.getAdultsMayorReserve()));

                                                BigDecimal totalCostPerson = viewRoomOfferReturn
                                                                .getAdultextracost().multiply(
                                                                                BigDecimal.valueOf(viewRoomOfferReturn
                                                                                                .getAdultsExtraReserve()))
                                                                .add(viewRoomOfferReturn.getAdultmayorcost()
                                                                                .multiply(BigDecimal.valueOf(
                                                                                                viewRoomOfferReturn
                                                                                                                .getAdultsMayorReserve())))
                                                                .add(viewRoomOfferReturn.getAdultcost()
                                                                                .multiply(BigDecimal.valueOf(
                                                                                                viewRoomOfferReturn
                                                                                                                .getAdultsReserve())))
                                                                .add(viewRoomOfferReturn.getKidcost()
                                                                                .multiply(BigDecimal
                                                                                                .valueOf(viewRoomOfferReturn
                                                                                                                .getKidsReserve())));
                                                viewRoomOfferReturn.setTotalCapacity(viewRoomOfferReturn
                                                                .getAdultcapacity()
                                                                + viewRoomOfferReturn.getKidcapacity()
                                                                + viewRoomOfferReturn.getAdultextra()
                                                                + viewRoomOfferReturn.getAdultmayorcapacity());
                                                totalCostPerson = totalCostPerson
                                                                .multiply(BigDecimal.valueOf(
                                                                                viewRoomOfferReturn
                                                                                                .getNumberofnights()));
                                                viewRoomOfferReturn.setCosttotal(totalCostPerson);
                                                viewRoomOfferReturn.setOriginalcosttotal(totalCostPerson);
                                                QuotationOfferDto quotation = quotationOfferDtos.stream()
                                                                .filter(quotationOfferDto1 -> quotationOfferDto1
                                                                                .getRoom_offer_id()
                                                                                .equals(viewRoomOfferReturn
                                                                                                .getRoomOfferId()))
                                                                .findFirst().orElse(null);

                                                if (quotation != null) {

                                                        viewRoomOfferReturn.setAdultcost(
                                                                        BigDecimal.valueOf(quotation.getAdult_cost()
                                                                                        / viewRoomOfferReturn
                                                                                                        .getNumberofnights()));
                                                        viewRoomOfferReturn.setKidcost(
                                                                        BigDecimal.valueOf(quotation.getKid_cost()
                                                                                        / viewRoomOfferReturn
                                                                                                        .getNumberofnights()));
                                                        viewRoomOfferReturn.setAdultextracost(
                                                                        BigDecimal.valueOf(quotation
                                                                                        .getAdult_extra_cost()
                                                                                        / viewRoomOfferReturn
                                                                                                        .getNumberofnights()));
                                                        viewRoomOfferReturn.setAdultmayorcost(
                                                                        BigDecimal.valueOf(quotation
                                                                                        .getAdult_mayor_cost()
                                                                                        / viewRoomOfferReturn
                                                                                                        .getNumberofnights()));
                                                        viewRoomOfferReturn.setInfantcost(
                                                                        BigDecimal.valueOf(quotation.getInfant_cost()
                                                                                        / viewRoomOfferReturn
                                                                                                        .getNumberofnights()));
                                                        BigDecimal totalCostFeeding = GeneralMethods
                                                                        .calculatedTotalAmountFeeding(feedingList,
                                                                                        feedingGroupedList,
                                                                                        viewRoomOfferReturn
                                                                                                        .getAdultsReserve(),
                                                                                        viewRoomOfferReturn
                                                                                                        .getAdultsExtraReserve(),
                                                                                        viewRoomOfferReturn
                                                                                                        .getAdultsMayorReserve(),
                                                                                        viewRoomOfferReturn
                                                                                                        .getKidsReserve());
                                                        viewRoomOfferReturn.setCosttotal(
                                                                        viewRoomOfferReturn.getCosttotal()
                                                                                        .add(totalCostFeeding));
                                                        viewRoomOfferReturn.setListFeeding(feedingList);
                                                        viewRoomOfferReturn.setAmountFeeding(totalCostFeeding);
                                                        viewRoomOfferReturn.setTotalPointsRibera(
                                                                        totalPointRibera.intValue());
                                                        viewRoomOfferReturn.setTotalPointsRiberaTop(
                                                                        totalPointRiberaTop.intValue());
                                                        if (totalPointRibera > 0 || totalPointRiberaTop > 0) {
                                                                Double totalDiscount = this.discountForPoint
                                                                                .calculated(viewRoomOfferReturn
                                                                                                .getCosttotal()
                                                                                                .doubleValue());
                                                                viewRoomOfferReturn.setTotalDiscount(totalDiscount);
                                                                viewRoomOfferReturn.setCosttotal(
                                                                                viewRoomOfferReturn.getCosttotal()
                                                                                                .subtract(BigDecimal
                                                                                                                .valueOf(totalDiscount)));
                                                                viewRoomOfferReturn.setTotalPercentageDiscount(
                                                                                this.discountForPoint.getPercentage());
                                                        }
                                                        viewRoomOfferReturn.setTotalPointsReward(
                                                                        totalPointRibera.intValue()
                                                                                        + totalPointRiberaTop
                                                                                                        .intValue());

                                                }
                                        }
                                        return Mono.just(returnView);

                                }).flatMapMany(Flux::fromIterable)
                                .flatMap(roomOffer -> servicesRepository
                                                .findAllViewComfortReturn(roomOffer.getRoomOfferId())
                                                .collectList()
                                                .flatMap(comfortList -> {
                                                        roomOffer.setListAmenities(comfortList);
                                                        return bedroomRepository
                                                                        .findAllViewBedroomReturn(roomOffer.getRoomId())
                                                                        .collectList()
                                                                        .map(bedroomList -> {
                                                                                roomOffer.setListBedroomReturn(
                                                                                                bedroomList);
                                                                                return roomOffer;
                                                                        });
                                                }));

                // .collectSortedList(Comparator.comparing(ViewRoomOfferReturn::getRoomOfferId))

        }

        @Override
        public Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId,
                        LocalDate offerTimeInit,
                        LocalDate offerTimeEnd,
                        Integer infantCapacity, Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
                        Integer adultExtra) {
                return roomOfferRepository
                                .findFiltered(roomTypeId, offerTimeInit, offerTimeEnd, infantCapacity,
                                                kidCapacity,
                                                adultCapacity, adultMayorCapacity, adultExtra)
                                .filterWhen(roomOffer -> bookingRepository.findConflictingBookings(
                                                roomOffer.getRoomOfferId(), offerTimeInit, offerTimeEnd)
                                                .hasElements()
                                                .map(hasConflicts -> !hasConflicts))
                                .flatMap(roomOffer -> servicesRepository
                                                .findAllViewComfortReturn(roomOffer.getRoomOfferId())
                                                .collectList()
                                                .flatMap(comfortList -> {
                                                        roomOffer.setListAmenities(comfortList);
                                                        return bedroomRepository
                                                                        .findAllViewBedroomReturn(roomOffer.getRoomId())
                                                                        .collectList()
                                                                        .map(bedroomList -> {
                                                                                roomOffer.setListBedroomReturn(
                                                                                                bedroomList);
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
                                                        roomOfferEntity.setPoints(calculatePoints(
                                                                        roomOfferEntity.getCost(), RATIO_BASE));
                                                        roomOfferEntity
                                                                        .setInResortPoints(calculatePoints(
                                                                                        roomOfferEntity.getCost(),
                                                                                        RATIO_INRESORT));
                                                        roomOfferEntity.setRiberaPoints(calculatePoints(
                                                                        roomOfferEntity.getCost(), RATIO_RIBERA));
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
                                .flatMap(service -> servicesRepository
                                                .findAllViewComfortReturn(service.getRoomOfferId())
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
                                                return Mono.error(new IllegalArgumentException(
                                                                "Solo se puede poner inactiva una oferta, " +
                                                                                "no se puede eliminar si tiene reservas"));
                                        }
                                        return quotationRoomOfferRepository.deleteAllByRoomOfferId(id)
                                                        .then(comfortRoomOfferDetailRepository
                                                                        .deleteAllByRoomOfferId(id)
                                                                        .then(roomOfferRepository.deleteById(id)));
                                });
        }

        @Override
        public Mono<RoomOfferEntity> update(RoomOfferEntity entity) {
                return roomOfferRepository.findByRoomIdAndState(entity.getRoomId(), 1)
                                .flatMap(existingActiveOffer -> {
                                        if (!existingActiveOffer.getRoomOfferId().equals(entity.getRoomOfferId())) {
                                                return Mono.error(
                                                                new IllegalArgumentException(
                                                                                "Ya existe una oferta activa para este alojamiento."));
                                        }
                                        return quotationRoomOfferRepository
                                                        .findAllByRoomOfferId(entity.getRoomOfferId())
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
                                                quotationRoomOfferRepository
                                                                .findAllByRoomOfferId(entity.getRoomOfferId())
                                                                .hasElements()
                                                                .flatMap(hasElements -> {
                                                                        if (!hasElements && entity.getState() == 1) {
                                                                                return Mono.error(
                                                                                                new IllegalArgumentException(
                                                                                                                "No se puede activar una oferta que no tiene cotizaciones"));
                                                                        }
                                                                        return roomOfferRepository.save(entity);
                                                                }));
        }

        private Integer calculatePoints(BigDecimal price, Integer points) {
                return price.intValue() / points;
        }

        @Override
        public Mono<ViewRoomOfferReturn> findRoomOfferById(Integer roomOfferId) {
                return Mono.zip(this.roomOfferRepository.findViewRoomOfferReturnByRoomOfferId(roomOfferId),
                                servicesRepository
                                                .findAllViewComfortReturn(
                                                                roomOfferId)
                                                .collectList(),
                                bedroomRepository.findAllViewBedroomReturn(
                                                roomOfferId)
                                                .collectList())
                                .flatMap(tuple -> {
                                        ViewRoomOfferReturn viewRoomOfferReturn = tuple.getT1();
                                        viewRoomOfferReturn.setListAmenities(tuple.getT2());
                                        viewRoomOfferReturn.setListBedroomReturn(tuple.getT3());
                                        return Mono.just(viewRoomOfferReturn);
                                }).switchIfEmpty(
                                                Mono.error(new IllegalArgumentException("No se encontró la oferta")));
        }
}
