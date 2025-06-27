package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardDetailDto {
    private String packageName;
    private Integer rewardsAmount;
    private String status;
    private String detail;
    private String releasedAt;
}
