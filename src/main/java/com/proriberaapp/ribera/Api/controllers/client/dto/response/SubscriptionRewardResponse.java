package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.proriberaapp.ribera.Domain.dto.RewardSubscriptionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SubscriptionRewardResponse {
    private boolean result;
    private List<RewardSubscriptionDto> data;
}