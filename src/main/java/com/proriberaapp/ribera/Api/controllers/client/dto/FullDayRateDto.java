package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class FullDayRateDto {
    private Integer rateId;
    private String title;
    private BigDecimal price;
    private String description;
    private String userCategory;
    private List<String> rateType;
    private Boolean rateStatus = true;
}
