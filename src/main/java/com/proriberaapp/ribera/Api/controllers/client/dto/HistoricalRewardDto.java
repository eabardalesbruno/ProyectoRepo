package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HistoricalRewardDto {
    private Long idrewardsreleaselog;
    //private Integer userId;
    private Integer rewardsamount;
    private String status;
    private String packagename;
    private String familypackagename;
    //private Integer idPackageDetail;
    private String detail;
    private String releasedat;
    //private String releasedBy;
    //private String updatedAt;
}
