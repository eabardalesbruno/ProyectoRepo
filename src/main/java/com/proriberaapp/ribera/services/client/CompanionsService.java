package com.proriberaapp.ribera.services.client;


import com.proriberaapp.ribera.Domain.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CompanionsService  {

    Flux<CompanionsEntity> getCompanionsByBookingId(Integer bookingId);

    Mono<CompanionsEntity> addCompanionBooking(CompanionsEntity companionsEntity);

    Mono<Void> validateTotalCompanions(Integer bookingId, Flux<CompanionsEntity> companionsEntity);

    Mono<CompanionsDto> fetchCompanionByDni(String dni);

    Mono<CompanionsEntity> calculateAgeandSave(CompanionsEntity companionsEntity);

}
