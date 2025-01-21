package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DiscountDto {
    private Integer id;
    private String name;
    private Float percentage;
    private boolean applyToReservation;
    private boolean applyToFood;
    
}
