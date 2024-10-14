package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.QuotationRoomOfferEntity;
import com.proriberaapp.ribera.services.client.QuotationRoomOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/quotation-room-offer")
@RequiredArgsConstructor
public class QuotationRoomOfferController {

    private final QuotationRoomOfferService quotationRoomOfferService;

    @GetMapping("/{quotationId}")
    public Flux<QuotationRoomOfferEntity> findQuotationRoomOffersByQuotationId(@PathVariable Integer quotationId) {
        return quotationRoomOfferService.findAllByQuotationId(quotationId);
    }

    @GetMapping
    public Flux<QuotationRoomOfferEntity> findAllQuotations() {
        return quotationRoomOfferService.findAllQuotations();
    }

}
