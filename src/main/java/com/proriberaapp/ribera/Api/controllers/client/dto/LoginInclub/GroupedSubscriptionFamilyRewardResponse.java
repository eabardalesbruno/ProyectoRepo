package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import com.proriberaapp.ribera.Domain.dto.GroupedRewardDetailDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupedSubscriptionFamilyRewardResponse {
    private boolean result;
    private List<GroupedRewardDetailDto> data;
    private String timestamp;
    private int status;
}