package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.dto.QuotationDto;
import com.proriberaapp.ribera.Domain.entities.QuotationEntity;
import com.proriberaapp.ribera.services.client.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quotation")
@RequiredArgsConstructor
public class QuotationController {
    private final QuotationService quotationService;

    @GetMapping
    public Flux<QuotationEntity> findAllQuotations() {
        return quotationService.findAllQuotations();
    }

    @GetMapping("/{quotationId}")
    public Mono<QuotationEntity> findQuotationById(@PathVariable Integer quotationId) {
        return quotationService.findQuotationById(quotationId);
    }

    @PostMapping
    public Mono<QuotationEntity> saveQuotation(@RequestBody QuotationDto quotationEntity) {
        return quotationService.saveQuotation(quotationEntity);
    }

    @PutMapping
    public Mono<QuotationEntity> updateQuotation(@RequestBody QuotationDto quotationDto) {
        return quotationService.updateQuotation(quotationDto);
    }

    @DeleteMapping("/{quotationId}")
    public Mono<Void> deleteQuotation(@PathVariable Integer quotationId) {
        return quotationService.deleteQuotation(quotationId);
    }

}
