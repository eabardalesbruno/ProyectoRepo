package com.proriberaapp.ribera.services.discount;

import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.enums.DiscountTypeEnum;

@Service
public class DiscountForPoint implements IDiscount {
    private double percentage;
    private String description;

    public DiscountForPoint() {

        this.percentage = 70;
        this.description = "Descuento por puntos";
    }

    @Override
    public Double calculated(double amount) {
        return (amount * percentage / 100);
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Double getPercentage() {
        return percentage;
    }

    @Override
    public DiscountTypeEnum getType() {
        return DiscountTypeEnum.POINTS;
    }


}
