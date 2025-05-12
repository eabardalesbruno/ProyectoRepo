package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;

@Data
public class QuotationDto {
    private Integer quotationId;
    private String quotationDescription;
    private Integer yearId;
    private BigDecimal infantCost;
    private BigDecimal kidCost;
    private BigDecimal adultCost;
    private BigDecimal adultMayorCost;
    private BigDecimal adultExtraCost;
    private BigDecimal kidReward;
    private BigDecimal adultReward;
    private BigDecimal adultMayorReward;
    private BigDecimal adultExtraReward;
    private List<Integer> roomOfferIds;
    private List<quotationDayDto> days;

}
