package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.QuotationRoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.repository.QuotationRoomOfferRepository;
import com.proriberaapp.ribera.services.client.QuotationRoomOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class QuotationRoomOfferServiceImpl implements QuotationRoomOfferService {

    private final QuotationRoomOfferRepository quotationRoomOfferRepository;

    @Override
    public Flux<QuotationRoomOfferEntity> findAllByQuotationId(Integer quotationId) {
        return quotationRoomOfferRepository.findAllByQuotationId(quotationId);
    }

    @Override
    public Flux<QuotationRoomOfferEntity> findAllQuotations() {
        return quotationRoomOfferRepository.findAll();
    }
}
