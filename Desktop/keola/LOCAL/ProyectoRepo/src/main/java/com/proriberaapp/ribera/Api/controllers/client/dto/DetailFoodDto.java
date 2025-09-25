package com.proriberaapp.ribera.Api.controllers.client.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetailFoodDto {

    private Integer familygroupid;
    private String feeding;
    private BigDecimal cost;

}
