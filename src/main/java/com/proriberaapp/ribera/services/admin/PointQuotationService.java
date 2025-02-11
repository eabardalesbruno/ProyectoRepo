package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointQuotationDayDto;
import com.proriberaapp.ribera.Domain.dto.PointSaveQuotationDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PointQuotationService {
    Mono<Void> save(PointSaveQuotationDto pointSaveQuotationDto);

    Mono<Void> update(PointSaveQuotationDto pointSaveQuotationDto);

    Flux<PointQuotationDayDto> getQuotationDaySelected(Integer idpointtype);

    Flux<PointSaveQuotationDto> findAll();
}
