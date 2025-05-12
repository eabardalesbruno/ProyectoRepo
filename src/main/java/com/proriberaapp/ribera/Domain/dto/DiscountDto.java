package com.proriberaapp.ribera.Domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Integer id;
    private String name;
    private Float amount;
    private Float percentage;
    private boolean applyToReservation;
    private boolean applyToFood;
    
}
