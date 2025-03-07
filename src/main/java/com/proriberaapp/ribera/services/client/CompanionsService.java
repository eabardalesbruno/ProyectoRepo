package com.proriberaapp.ribera.services.client;


import com.proriberaapp.ribera.Domain.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CompanionsService  {

    //Booking
    Flux<CompanionsEntity> getCompanionsByBookingId(Integer bookingId);

    Mono<CompanionsEntity> addCompanionBooking(CompanionsEntity companionsEntity);

    Mono<Void> validateTotalCompanions(Integer bookingId, Flux<CompanionsEntity> companionsEntity);

    Mono<CompanionsDto> fetchCompanionByDni(String dni);

    Mono<CompanionsEntity> calculateAgeandSave(CompanionsEntity companionsEntity);

    Flux<CompanionsEntity> updateMultipleCompanions(Integer bookingId, List<CompanionsEntity> companions);

    Flux<CompanionsEntity> updateCompanion(Integer bookingId, List<CompanionsEntity> companionsEntities);

    //Fullday
    Flux<CompanionsEntity> getCompanionsByFulldayId(Integer fulldayId);

    Mono<CompanionsEntity> addCompanionFullday(CompanionsEntity companionsEntity);

    Mono<Void> validateTotalCompanionsFullday(Integer fulldayId, Flux<CompanionsEntity> companionsEntity);

    Flux<CompanionsEntity> updateMultipleCompanionsFullday(Integer fulldayId, List<CompanionsEntity> companions);

    Flux<CompanionsEntity> calculateAgeAndSaveFullDay(List<CompanionsEntity> companionsEntities);


}
