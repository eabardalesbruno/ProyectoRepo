package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CommissionService {

    Mono<CommissionEntity> calculateAndSaveCommission(PaymentBookEntity paymentBook, Integer caseType);

    Mono<BigDecimal> getTotalCommissionByPromoterId(Integer promoterId);

}
