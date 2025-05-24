package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class FullDayTypeFoodUpdateDto {
    private String background;
    private Integer currencyTypeId;
    private String dessert;
    private String drink;
    private String entry;
    private String foodDescription;
    private String foodName;
    //private Integer fullDayTypeFoodId
    private BigDecimal price;
    private Integer quantity;
    //private String type;
    private String urlImage;
}
