package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.QuotationOfferDayDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.dto.*;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.exception.QuoteAndOfferIsAlreadyRegisteredException;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationRoomOfferRepository quotationRoomOfferRepository;
    private final RoomOfferRepository roomOfferRepository;
    private final QuotationDayRepository quotationDayRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    @Override
    public Mono<QuotationObjectDto> findAllQuotations(Integer condition) {
        return roomTypeRepository.findAllRoomTypes()
                .flatMap(roomTypeEntity ->
                        roomRepository.findRoomByRoomTypeId(roomTypeEntity.getRoomTypeId())
                                .flatMap(roomNumberEntity ->
                                        quotationRepository.getAllQuotationByRoomNumber(roomNumberEntity.getRoomNumber(), condition)
                                                .collectList()
                                                .map(quotationOffers -> {
                                                    RoomDto roomDto = new RoomDto();
                                                    roomDto.setRoomNumber(roomNumberEntity.getRoomNumber());
                                                    roomDto.setQuotationOffers(quotationOffers);
                                                    return roomDto;
                                                })
                                )
                                .collectList()
                                .map(roomDtos -> {
                                    RoomTypeDto roomTypeDto = new RoomTypeDto();
                                    roomTypeDto.setRoomTypeId(roomTypeEntity.getRoomTypeId());
                                    roomTypeDto.setRoomType(roomTypeEntity.getRoomType());
                                    roomTypeDto.setRoomTypeName(roomTypeEntity.getRoomTypeName());
                                    roomTypeDto.setRoomTypeDescription(roomTypeEntity.getRoomTypeDescription());
                                    roomTypeDto.setRoomstateid(roomTypeEntity.getRoomstateid());
                                    roomTypeDto.setRoomState(roomTypeEntity.getRoomState());
                                    roomTypeDto.setCategory(roomTypeEntity.getCategory());
                                    roomTypeDto.setRoomnumbers(roomDtos);
                                    return roomTypeDto;
                                })
                )
                .collectList()
                .map(roomTypeDtos -> {
                    QuotationObjectDto response = new QuotationObjectDto();
                    response.setRoomType(roomTypeDtos);
                    return response;
                });
    }
    @Override
    public Mono<QuotationEntity> findQuotationById(Integer quotationId) {
        return quotationRepository.findById(quotationId);
    }

    @Override
    public Mono<QuotationEntity> saveQuotation(QuotationDto quotationDto) {
        QuotationEntity quotationEntity = QuotationEntity.builder()
                .quotationDescription(quotationDto.getQuotationDescription())
                .infantCost(quotationDto.getInfantCost())
                .kidCost(quotationDto.getKidCost())
                .adultCost(quotationDto.getAdultCost())
                .adultMayorCost(quotationDto.getAdultMayorCost())
                .adultExtraCost(quotationDto.getAdultExtraCost())
                .kidReward(quotationDto.getKidReward())
                .adultReward(quotationDto.getAdultReward())
                .adultMayorReward(quotationDto.getAdultMayorReward())
                .adultExtraReward(quotationDto.getAdultExtraReward())
                .build();
        List<Integer> daysSelected = quotationDto.getDays().stream()
                .filter(d -> d.isSelected())
                .map(d -> d.getId()).toList();
        return quotationRepository.getQuotationFindOfferAndDays(quotationDto.getYearId(), quotationDto.getRoomOfferIds(), daysSelected)
                .collectList()
                .flatMap(quotations -> {
                    if (quotations.size() > 0) {
                        QuotationOfferDayDto quotationOfferDayDto = quotations.get(0);
                        return Mono.error(new QuoteAndOfferIsAlreadyRegisteredException(
                                quotationOfferDayDto.getOffername(),
                                quotationOfferDayDto.getQuotation_description(),
                                quotationOfferDayDto.getDayname()));
                    }
                    return quotationRepository.save(quotationEntity)
                            .flatMap(savedQuotation -> {
                                List<QuotationDayEntity> days = quotationDto.getDays()
                                        .stream()
                                        .filter(d -> d.isSelected())
                                        .map(d -> QuotationDayEntity.builder()
                                                .idQuotation(savedQuotation.getQuotationId())
                                                .idDay(d.getId())
                                                .idYear(quotationDto.getYearId())
                                                .build())
                                        .collect(Collectors.toList());
                                List<QuotationRoomOfferEntity> quotationRoomOffers = quotationDto
                                        .getRoomOfferIds().stream()
                                        .map(roomOfferId -> QuotationRoomOfferEntity
                                                .builder()
                                                .quotationId(savedQuotation.getQuotationId())
                                                .roomOfferId(roomOfferId)
                                                .build())
                                        .collect(Collectors.toList());
                                /*
                                 * return quotationRoomOfferRepository.saveAll(
                                 * quotationRoomOffers)
                                 * .flatMap(d -> quotationDayRepository
                                 * .saveAll(days))
                                 * .then(Mono.just(savedQuotation));
                                 */
                                return Flux.zip(quotationRoomOfferRepository.saveAll(quotationRoomOffers)
                                                        .collectList(),
                                                quotationDayRepository.saveAll(days).collectList())
                                        .then(Mono.just(savedQuotation));
                                /*
                                 * return Flux.zip(quotationRoomOfferRepository.saveAll(
                                 * quotationRoomOffers),
                                 * quotationDayRepository.saveAll(days))
                                 * .then(Mono.just(savedQuotation));
                                 */
                            });
                });

    }

    @Override
    public Mono<QuotationEntity> updateQuotation(QuotationDto quotationDto) {
        List<Integer> daysSelected = quotationDto.getDays().stream().filter(d -> d.isSelected())
                .map(d -> d.getId()).toList();
        return quotationRepository.getQuotationFindOfferAndDays(quotationDto.getRoomOfferIds(),
                    daysSelected,
                    quotationDto.getQuotationId())
                .collectList()
                .flatMap(quotations -> {
                    if (quotations.size() > 0) {
                        QuotationOfferDayDto quotationOfferDayDto = quotations.get(0);
                        return Mono.error(new QuoteAndOfferIsAlreadyRegisteredException(
                                quotationOfferDayDto.getOffername(),
                                quotationOfferDayDto.getQuotation_description(),
                                quotationOfferDayDto.getDayname()));
                    }
                    return quotationRepository.findById(quotationDto.getQuotationId())
                            .flatMap(existingQuotation -> {
                                existingQuotation.setQuotationDescription(quotationDto.getQuotationDescription());
                                existingQuotation.setKidCost(quotationDto.getKidCost());
                                existingQuotation.setAdultCost(quotationDto.getAdultCost());
                                existingQuotation.setAdultMayorCost(quotationDto.getAdultMayorCost());
                                existingQuotation.setAdultExtraCost(quotationDto.getAdultExtraCost());
                                existingQuotation.setKidReward(quotationDto.getKidReward());
                                existingQuotation.setAdultReward(quotationDto.getAdultReward());
                                existingQuotation.setAdultMayorReward(quotationDto.getAdultMayorReward());
                                existingQuotation.setAdultExtraReward(quotationDto.getAdultExtraReward());
                                return quotationRepository.save(existingQuotation)
                                        .flatMap(savedQuotation -> quotationRoomOfferRepository
                                                .findAllByQuotationId(
                                                        quotationDto.getQuotationId())
                                                .collectList()
                                                .flatMap(existingQuotationRoomOffers -> {
                                                    List<Integer> existingRoomOfferIds = existingQuotationRoomOffers
                                                            .stream()
                                                            .map(QuotationRoomOfferEntity::getRoomOfferId)
                                                            .toList();
                                                    List<Integer> roomOffersToDeactivate = existingRoomOfferIds
                                                            .stream()
                                                            .filter(roomOfferId -> !quotationDto
                                                                    .getRoomOfferIds()
                                                                    .contains(roomOfferId))
                                                            .collect(Collectors
                                                                    .toList());
                                                    List<QuotationDayEntity> days = quotationDto
                                                            .getDays()
                                                            .stream()
                                                            .filter(d -> d.isSelected())
                                                            .map(d -> QuotationDayEntity
                                                                    .builder()
                                                                    .idQuotation(savedQuotation
                                                                            .getQuotationId())
                                                                    .idDay(d.getId())
                                                                    .idYear(quotationDto.getYearId())
                                                                    .build())
                                                            .collect(Collectors
                                                                    .toList());
                                                    return Flux.fromIterable(
                                                                    roomOffersToDeactivate)
                                                            .flatMap(roomOfferId -> roomOfferRepository
                                                                    .findById(roomOfferId)
                                                                    .flatMap(roomOffer -> {
                                                                        roomOffer.setState(
                                                                                2);
                                                                        return roomOfferRepository
                                                                                .save(roomOffer);
                                                                    }))
                                                            .then(quotationRoomOfferRepository
                                                                    .deleteAllByQuotationId(
                                                                            quotationDto.getQuotationId()))
                                                            .thenMany(Flux.fromIterable(
                                                                            quotationDto.getRoomOfferIds())
                                                                    .map(roomOfferId -> QuotationRoomOfferEntity
                                                                            .builder()
                                                                            .quotationId(quotationDto
                                                                                    .getQuotationId())
                                                                            .roomOfferId(roomOfferId)
                                                                            .build()))
                                                            .collectList()
                                                            .flatMap(entities -> quotationRoomOfferRepository
                                                                    .saveAll(Flux.fromIterable(
                                                                            entities))
                                                                    .then(this.quotationDayRepository
                                                                            .deleteFindQuotationId(
                                                                                    savedQuotation.getQuotationId())
                                                                            .then(this.quotationDayRepository
                                                                                    .saveAll(days)
                                                                                    .then())))
                                                            .then(Mono.just(savedQuotation));
                                                }));
                            });
                });

    }

    @Override
    public Mono<Void> deleteQuotation(Integer quotationId) {
        return quotationRoomOfferRepository.findAllByQuotationId(quotationId).collectList()
                .flatMap(quotationRoomOffers -> {
                    if (!quotationRoomOffers.isEmpty()) {
                        List<Integer> roomOfferIds = quotationRoomOffers.stream()
                                .map(QuotationRoomOfferEntity::getRoomOfferId)
                                .collect(Collectors.toList());
                        return quotationRoomOfferRepository.deleteAllByQuotationId(quotationId)
                                .thenMany(Flux.fromIterable(roomOfferIds)
                                        .flatMap(roomOfferId -> roomOfferRepository
                                                .findById(roomOfferId)
                                                .flatMap(roomOffer -> {
                                                    roomOffer.setState(
                                                            2);
                                                    return roomOfferRepository
                                                            .save(roomOffer);
                                                })))
                                .then()
                                .then(this.quotationDayRepository.deleteFindQuotationId(
                                        quotationId))
                                .then(quotationRepository.deleteById(quotationId));
                    }

                    return Mono.zip(this.quotationDayRepository.deleteFindQuotationId(quotationId),
                            quotationRepository.deleteById(quotationId)).then();
                });
    }

    @Override
    public Flux<quotationDayDto> getQuotationDaySelected(Integer quotationId) {
        return this.quotationRepository.getQuotationDaySelected(quotationId);

    }

    @Override
    public Mono<BigDecimal> calculateTotalRewards(BookingSaveRequest bookingSaveRequest) {
        LocalDate startDate = bookingSaveRequest.getDayBookingInit();
        LocalDate endDate = bookingSaveRequest.getDayBookingEnd();

        List<LocalDate> bookingDates = startDate.datesUntil(endDate.plusDays(1))
                .toList();

        List<Integer> daysOfWeek = bookingDates.stream()
                .map(date -> date.getDayOfWeek().getValue())
                .collect(Collectors.toList());

        return quotationRepository.findQuotationByRoomOfferAndDays(bookingSaveRequest.getRoomOfferId(), daysOfWeek)
                .collectList()
                .map(quotations -> {
                    BigDecimal totalRewards = BigDecimal.ZERO;

                    for (LocalDate date : bookingDates) {
                        int dayOfWeek = date.getDayOfWeek().getValue();

                        Optional<QuotationDetailEntity> quotationOpt = quotations.stream()
                                .filter(q -> q.getIdday() == dayOfWeek)
                                .findFirst();

                        if (quotationOpt.isPresent()) {
                            QuotationDetailEntity quotation = quotationOpt.get();
                            BigDecimal rewardForThisDay = calculateRewardsForQuotation(
                                    quotation,
                                    bookingSaveRequest
                            );
                            totalRewards = totalRewards.add(rewardForThisDay);
                        }
                    }

                    return totalRewards;
                });
    }

    private BigDecimal calculateRewardsForQuotation(QuotationDetailEntity quotation, BookingSaveRequest booking) {

        BigDecimal kidReward = quotation.getKidReward() != null ? quotation.getKidReward() : BigDecimal.ZERO;
        BigDecimal adultReward = quotation.getAdultReward() != null ? quotation.getAdultReward() : BigDecimal.ZERO;
        BigDecimal adultMayorReward = quotation.getAdultMayorReward() != null ? quotation.getAdultMayorReward() : BigDecimal.ZERO;
        BigDecimal adultExtraReward = quotation.getAdultExtraReward() != null ? quotation.getAdultExtraReward() : BigDecimal.ZERO;

        return kidReward.multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberChild())))
                .add(adultReward.multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberAdult()))))
                .add(adultMayorReward.multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberAdultMayor()))))
                .add(adultExtraReward.multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberAdultExtra()))));
        /*
        return quotation.getKidReward()
                .multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberChild())))
                .add(quotation.getAdultReward()
                        .multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberAdult()))))
                .add(quotation.getAdultMayorReward()
                        .multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberAdultMayor()))))
                .add(quotation.getAdultExtraReward()
                        .multiply(BigDecimal.valueOf(getSafeInt(booking.getNumberAdultExtra()))));

         */
    }

    private BigDecimal toBigDecimal(BigInteger value) {
        return value != null ? new BigDecimal(value) : BigDecimal.ZERO;
    }


    private int getSafeInt(Integer value) {
        return value != null ? value : 0;
    }

    private BigDecimal getSafeValue(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}