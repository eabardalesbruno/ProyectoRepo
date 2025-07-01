package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OccupancyDayDto {
    private Integer dayId;
    private Integer status;
}
