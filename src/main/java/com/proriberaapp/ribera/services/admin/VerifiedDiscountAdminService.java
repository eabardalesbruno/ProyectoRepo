package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface VerifiedDiscountAdminService {

    Mono<UserNameAndDiscountDto> verifiedPercentajeDiscountAdmin(Integer userId, BigDecimal costTotal);
}
