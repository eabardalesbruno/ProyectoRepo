package com.proriberaapp.ribera.services.discount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountRequestDto {
    private double percentage;
    private String description;
}
