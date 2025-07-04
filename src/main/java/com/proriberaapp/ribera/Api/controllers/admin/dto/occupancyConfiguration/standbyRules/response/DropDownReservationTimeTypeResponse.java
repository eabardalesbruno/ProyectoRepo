package com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DropDownReservationTimeTypeResponse {
    private Integer idreservationtimetype;
    private String reservationtimetypename;
    private Integer standbyhours;
}
