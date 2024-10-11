package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.QuotationEntity;
import com.proriberaapp.ribera.Infraestructure.repository.QuotationRepository;
import com.proriberaapp.ribera.services.client.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;

    @Override
    public Flux<QuotationEntity> findAllQuotations() {
        return quotationRepository.findAll();
    }

    @Override
    public Mono<QuotationEntity> saveQuotation(QuotationEntity quotationEntity) {
        return quotationRepository.save(quotationEntity);
    }

    @Override
    public Mono<QuotationEntity> updateQuotation(QuotationEntity quotationEntity) {
        return quotationRepository.findById(quotationEntity.getQuotationId()).flatMap(quotation -> {
            quotation.setQuotationDescription(quotationEntity.getQuotationDescription());
            quotation.setInfantCost(quotationEntity.getInfantCost());
            quotation.setKidCost(quotationEntity.getKidCost());
            quotation.setAdultCost(quotationEntity.getAdultCost());
            quotation.setAdultMayorCost(quotationEntity.getAdultMayorCost());
            quotation.setAdultExtraCost(quotationEntity.getAdultExtraCost());
            return quotationRepository.save(quotation);
        });
    }

    @Override
    public Mono<Void> deleteQuotation(Integer quotationId) {
        return quotationRepository.deleteById(quotationId);
    }

}
