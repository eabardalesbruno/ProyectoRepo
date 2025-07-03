package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import com.proriberaapp.ribera.Domain.dto.RewardSubscriptionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GroupedSubscriptionRewardResponse {
    private boolean result;
    private Map<String, List<RewardSubscriptionDto>> data;
}
