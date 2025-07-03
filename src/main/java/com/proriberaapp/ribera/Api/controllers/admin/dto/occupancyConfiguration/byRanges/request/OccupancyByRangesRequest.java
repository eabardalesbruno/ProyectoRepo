package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OccupancyByRangesRequest {
    private Boolean hasDateRange;
    private String rangeFromDate;
    private String rangeToDate;
    private Boolean hasTimeRange;
    private Integer rangeFromHour;
    private Integer rangeFromMinute;
    private BigDecimal maxRewardsPercentage;
    private BigDecimal exceptionRewardsPercentage;
    private BigDecimal newMaxRewardsPercentage;
}
