package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StandByRuleDetailDto {
    private Integer idstandbyrule;
    private Integer idreservationtimetype;
    private Integer standbyhours;
    private Integer idvisibilitytype;
}
