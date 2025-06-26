package com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardReleaseLogResponseDto {
    private Integer idRewardsReleaseLog;
    private Integer userId;
    private Double rewardsAmount;
    private String status;
    private String packageName;
    private String familyPackageName;
    private Integer idPackageDetail;
    private String detail;
    private LocalDateTime releasedAt;
    private String releasedBy;
    private LocalDateTime updatedAt;
}
