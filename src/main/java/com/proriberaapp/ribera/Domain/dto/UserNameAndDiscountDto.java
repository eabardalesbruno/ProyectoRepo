package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserNameAndDiscountDto {
    private String username;
    private Float percentage;

}
