package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuotationDto {
    private Integer quotationId;
    private String quotationDescription;
    private BigDecimal infantCost;
    private BigDecimal kidCost;
    private BigDecimal adultCost;
    private BigDecimal adultMayorCost;
    private BigDecimal adultExtraCost;
    private List<Integer> roomOfferIds;
}
