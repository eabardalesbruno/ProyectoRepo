package com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RequestReleaseLogDto {
    private String username;
    private Integer rewardsAmount;
    private String familyPackageName;
    private String detail;
    private Integer userId;
}