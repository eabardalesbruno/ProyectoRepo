package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.byRanges.response;

import com.proriberaapp.ribera.Domain.entities.OccupancyByRangeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OcupancyByRangesResponse {
    private boolean result;
    private Integer total;
    private List<OccupancyByRangeEntity> data;
    private Integer size;
    private Integer page;
}
