package com.proriberaapp.ribera.services.client;

import java.util.List;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;

import reactor.core.publisher.Mono;

public interface VerifiedDiscountService {

    public Mono<Float> verifiedPercentajeDiscount(int userId);

}
