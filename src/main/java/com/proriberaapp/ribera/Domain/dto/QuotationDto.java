package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;

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
    private List<quotationDayDto> days;

}
