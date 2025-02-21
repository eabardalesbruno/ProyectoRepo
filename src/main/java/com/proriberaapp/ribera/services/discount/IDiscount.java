package com.proriberaapp.ribera.services.discount;

import com.proriberaapp.ribera.Domain.enums.DiscountTypeEnum;

import reactor.core.publisher.Mono;

public interface IDiscount {
    Double calculated(double amount);

    String getDescription();

    Double getPercentage();

    DiscountTypeEnum getType();

}
