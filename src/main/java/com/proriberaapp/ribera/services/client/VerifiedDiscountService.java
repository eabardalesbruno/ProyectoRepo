package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;

import reactor.core.publisher.Mono;

public interface VerifiedDiscountService {

    Mono<UserNameAndDiscountDto> verifiedPercentajeDiscount(int userId);

}
