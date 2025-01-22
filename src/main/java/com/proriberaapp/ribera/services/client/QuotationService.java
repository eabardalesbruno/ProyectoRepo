package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.dto.QuotationDto;
import com.proriberaapp.ribera.Domain.entities.QuotationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QuotationService {
    Flux<QuotationEntity> findAllQuotations();

    Flux<quotationDayDto> getQuotationDaySelected(Integer quotationId);

    Mono<QuotationEntity> findQuotationById(Integer quotationId);

    Mono<QuotationEntity> saveQuotation(QuotationDto quotationDto);

    Mono<QuotationEntity> updateQuotation(QuotationDto quotationDto);

    Mono<Void> deleteQuotation(Integer quotationId);
}
