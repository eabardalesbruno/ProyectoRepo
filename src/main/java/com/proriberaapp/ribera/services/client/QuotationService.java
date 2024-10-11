package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.QuotationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QuotationService {
    Flux<QuotationEntity> findAllQuotations();

    Mono<QuotationEntity> saveQuotation(QuotationEntity quotationEntity);

    Mono<QuotationEntity> updateQuotation(QuotationEntity quotationEntity);

    Mono<Void> deleteQuotation(Integer quotationId);
}
