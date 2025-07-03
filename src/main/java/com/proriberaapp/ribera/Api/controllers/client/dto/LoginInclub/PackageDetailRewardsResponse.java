package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import com.proriberaapp.ribera.Domain.dto.PercentageData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDetailRewardsResponse {
    private boolean result;
    private PercentageData data;
    private Long timestamp;
    private int status;
}
