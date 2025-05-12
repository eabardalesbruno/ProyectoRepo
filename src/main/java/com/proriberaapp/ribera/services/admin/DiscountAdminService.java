package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.utils.constants.DiscountTypeCode;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface DiscountAdminService {

    Mono<UserNameAndDiscountDto> getPercentageDiscount(
            DiscountTypeCode discountType, Integer userId, BigDecimal costTotal, BigDecimal totalAmountFeeding);

}
