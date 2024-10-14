package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.QuotationRoomOfferEntity;
import reactor.core.publisher.Flux;

public interface QuotationRoomOfferService {
    Flux<QuotationRoomOfferEntity> findAllByQuotationId(Integer quotationId);

    Flux<QuotationRoomOfferEntity> findAllQuotations();
}
