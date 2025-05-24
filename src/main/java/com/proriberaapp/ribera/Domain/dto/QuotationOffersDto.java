package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationOffersDto {

    private Integer quotationId;

    private Integer roomofferid;

    private Integer idyear;

    private String year;

    private String offername;

    private String quotationDescription;

    private BigDecimal infantCost;

    private BigDecimal kidCost;

    private BigDecimal adultCost;

    private BigDecimal adultMayorCost;

    private BigDecimal adultExtraCost;

    private BigDecimal kidReward;

    private BigDecimal adultReward;

    private BigDecimal adultMayorReward;

    private BigDecimal adultExtraReward;
}
