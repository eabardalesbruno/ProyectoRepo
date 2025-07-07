package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StandByRulesResponse {
    private boolean result;
    private Integer total;
    private List<StandByRuleDto> data;
    private Integer size;
    private Integer page;
}
