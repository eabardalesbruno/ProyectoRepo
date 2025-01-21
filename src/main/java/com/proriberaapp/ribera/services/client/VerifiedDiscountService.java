package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;

import reactor.core.publisher.Mono;

public interface VerifiedDiscountService {

    public Mono<UserNameAndDiscountDto> verifiedPercentajeDiscount(int userId);

}
