package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupedRewardDetailDto {
    private String familyPackageName;
    private Integer totalRewardsAmount;
    private List<RewardDetailDto> details;
}
