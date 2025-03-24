package com.proriberaapp.ribera.services.client;
import com.proriberaapp.ribera.Domain.entities.QuotationYearEntity;
import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QuotationYearService {
    Mono<QuotationYearEntity> saveQuotationYear(String year);
    Flux<QuotationYearEntity> getAllQuotationYears();
    Mono<QuotationYearEntity> getQuotationYearByDescription(String description);
}