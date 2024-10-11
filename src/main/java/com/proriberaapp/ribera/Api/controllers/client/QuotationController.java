package com.proriberaapp.ribera.Api.controllers.client;

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

    @PostMapping
    public Mono<QuotationEntity> saveQuotation(@RequestBody QuotationEntity quotationEntity) {
        System.out.println(quotationEntity.getQuotationDescription());
        System.out.println(quotationEntity.getInfantCost());
        System.out.println(quotationEntity.getKidCost());
        System.out.println(quotationEntity.getAdultCost());
        System.out.println(quotationEntity.getAdultMayorCost());
        System.out.println(quotationEntity.getAdultExtraCost());
        return quotationService.saveQuotation(quotationEntity);
    }

    @PutMapping
    public Mono<QuotationEntity> updateQuotation(@RequestBody QuotationEntity quotationEntity) {
        return quotationService.updateQuotation(quotationEntity);
    }

    @DeleteMapping("/{quotationId}")
    public Mono<Void> deleteQuotation(@PathVariable Integer quotationId) {
        return quotationService.deleteQuotation(quotationId);
    }

}
