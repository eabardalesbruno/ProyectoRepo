package com.proriberaapp.ribera.Api.controllers.client.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPersonQuantityDto {

    private Integer familyGroupId;
    private Integer quantity;

    public ItemPersonQuantityDto(Integer familyGroupId, Integer quantity) {
        this.familyGroupId = familyGroupId;
        this.quantity = quantity;
    }
}
