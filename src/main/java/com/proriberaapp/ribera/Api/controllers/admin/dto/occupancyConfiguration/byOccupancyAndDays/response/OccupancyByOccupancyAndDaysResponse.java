package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byOccupancyAndDays.response;

import com.proriberaapp.ribera.Domain.entities.OccupancyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OccupancyByOccupancyAndDaysResponse {
    private boolean result;
    private Integer total;
    private List<OccupancyEntity> data;
    private Integer size;
    private Integer page;
}
