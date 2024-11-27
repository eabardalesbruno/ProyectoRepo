package com.proriberaapp.ribera.services.client;

import reactor.core.publisher.Mono;

public interface VerifiedDiscountService {

    public Mono<Float> verifiedPercentajeDiscount(int userId);
}
