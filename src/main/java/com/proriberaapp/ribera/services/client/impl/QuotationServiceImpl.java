package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.QuotationOfferDayDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.dto.QuotationDto;
import com.proriberaapp.ribera.Domain.entities.QuotationDayEntity;
import com.proriberaapp.ribera.Domain.entities.QuotationEntity;
import com.proriberaapp.ribera.Domain.entities.QuotationRoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.exception.QuoteAndOfferIsAlreadyRegisteredException;
import com.proriberaapp.ribera.Infraestructure.repository.QuotationDayRepository;
import com.proriberaapp.ribera.Infraestructure.repository.QuotationRepository;
import com.proriberaapp.ribera.Infraestructure.repository.QuotationRoomOfferRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomOfferRepository;
import com.proriberaapp.ribera.services.client.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationRoomOfferRepository quotationRoomOfferRepository;
    private final RoomOfferRepository roomOfferRepository;
    private final QuotationDayRepository quotationDayRepository;

    @Override
    public Flux<QuotationEntity> findAllQuotations(Integer condition) {
        return quotationRepository.getAllQuotationByDays(condition);
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
        // Obtener fechas de reserva
        LocalDate startDate = bookingSaveRequest.getDayBookingInit();
        LocalDate endDate = bookingSaveRequest.getDayBookingEnd();

        // Obtener los días de la semana entre el rango de fechas (Lunes = 1, ..., Domingo = 7)
        List<Integer> daysOfWeek = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> date.getDayOfWeek().getValue())
                .distinct() // Evita días repetidos en la lista
                .collect(Collectors.toList());

        // Consultar cotizaciones filtrando por la oferta de habitación y los días de la semana
        return quotationRepository.findQuotationByRoomOfferAndDays(bookingSaveRequest.getRoomOfferId(), daysOfWeek)
                .collectList()
                .map(quotations -> quotations.stream()
                        .map(this::calculateRewardsForQuotation)
                        .reduce(BigDecimal.ZERO, BigDecimal::add) // Sumar todas las recompensas
                );
    }

    private BigDecimal calculateRewardsForQuotation(QuotationEntity quotation) {
        return getSafeValue(quotation.getKidReward())
                .add(getSafeValue(quotation.getAdultReward()))
                .add(getSafeValue(quotation.getAdultMayorReward()))
                .add(getSafeValue(quotation.getAdultExtraReward()));
    }

    private BigDecimal getSafeValue(BigInteger value) {
        return value != null ? new BigDecimal(value) : BigDecimal.ZERO;
    }
}