package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardReleaseRequest {
    private Integer userId;
    private Integer rewardsAmount;
    private String familyPackageName;
    private String detail;
}