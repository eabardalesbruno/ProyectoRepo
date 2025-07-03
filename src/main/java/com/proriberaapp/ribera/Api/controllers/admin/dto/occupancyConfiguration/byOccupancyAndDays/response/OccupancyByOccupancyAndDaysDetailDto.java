package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OccupancyByOccupancyAndDaysDetailDto {
    private Integer id;
    private String ruleName;
    private String description;
    private BigDecimal cashPercentage;
    private BigDecimal rewardsPercentage;
    private Integer status;
    private List<OccupancyDayDto> occupancyDayList;
}
