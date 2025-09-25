package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.QuotationYearEntity;
import com.proriberaapp.ribera.Infraestructure.repository.QuotationYearRepository;
import com.proriberaapp.ribera.services.client.QuotationYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuotationYearServiceImpl implements QuotationYearService {

    private final QuotationYearRepository quotationYearRepository;

    @Override
    public Mono<QuotationYearEntity> saveQuotationYear(String description) {
        return quotationYearRepository.getQuotationYearByDescription(description)
            .flatMap(Mono::just)
            .switchIfEmpty(
                Mono.defer(() -> {
                    QuotationYearEntity newQuotationYear = new QuotationYearEntity();
                    newQuotationYear.setDescription(description);
                    return quotationYearRepository.save(newQuotationYear);
                })
            );
    }

    @Override
    public Flux<QuotationYearEntity> getAllQuotationYears() {
        return quotationYearRepository.findAll();
    }

    @Override
    public Mono<QuotationYearEntity> getQuotationYearByDescription(String description) {
        return quotationYearRepository.getQuotationYearByDescription(description);
    }
}
