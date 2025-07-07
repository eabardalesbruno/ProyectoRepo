package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StandByRulesRequest {
    Integer idReservationTimeType;
    Integer standbyHours;
    Integer idVisibilityType;
}
