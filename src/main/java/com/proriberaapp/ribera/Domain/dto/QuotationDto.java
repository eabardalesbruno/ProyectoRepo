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
    private BigInteger kidReward;
    private BigInteger adultReward;
    private BigInteger adultMayorReward;
    private BigInteger adultExtraReward;
    private List<Integer> roomOfferIds;
    private List<quotationDayDto> days;

}
