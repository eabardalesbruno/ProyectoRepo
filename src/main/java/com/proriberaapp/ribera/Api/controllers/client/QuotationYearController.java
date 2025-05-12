package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.QuotationYearEntity;
import com.proriberaapp.ribera.services.client.QuotationYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quotation-year")
@RequiredArgsConstructor
public class QuotationYearController {
    private final QuotationYearService quotationYearService;

    @GetMapping
    public Flux<QuotationYearEntity> findAllQuotations() {
        return quotationYearService.getAllQuotationYears();
    }

    @PostMapping
    public Mono<QuotationYearEntity> saveQuotationYear(@RequestBody String year) {
        return quotationYearService.saveQuotationYear(year);
    }

}
