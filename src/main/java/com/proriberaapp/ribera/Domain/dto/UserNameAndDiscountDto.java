package com.proriberaapp.ribera.Domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserNameAndDiscountDto {
    private String username;
    private float percentage;
    private List<DiscountDto> discounts;

    public static UserNameAndDiscountDto empty() {
        return new UserNameAndDiscountDto("", 0.0f, List.of());
    }

    public void calculatedPercentage() {
        this.percentage = this.discounts.stream().map(DiscountDto::getPercentage).reduce(0.0f, Float::sum);
    }
    
}
