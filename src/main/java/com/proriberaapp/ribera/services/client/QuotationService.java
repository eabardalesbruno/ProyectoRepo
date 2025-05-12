package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.dto.QuotationDto;
import com.proriberaapp.ribera.Domain.dto.QuotationObjectDto;
import com.proriberaapp.ribera.Domain.entities.QuotationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface QuotationService {
    Mono<QuotationObjectDto> findAllQuotations(Integer condition);

    Flux<quotationDayDto> getQuotationDaySelected(Integer quotationId);

    Mono<QuotationEntity> findQuotationById(Integer quotationId);

    Mono<QuotationEntity> saveQuotation(QuotationDto quotationDto);

    Mono<QuotationEntity> updateQuotation(QuotationDto quotationDto);

    Mono<Void> deleteQuotation(Integer quotationId);

    Mono<BigDecimal> calculateTotalRewards(BookingSaveRequest bookingSaveRequest);
}
