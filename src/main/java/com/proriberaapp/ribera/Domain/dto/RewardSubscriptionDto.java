package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RewardSubscriptionDto {
    private Long idRewardsSubscription;
    private Long userId;
    private Long subscriptionId;
    private String familyPackageName;
    private String packageName;
    private Integer totalUsedRewards;
    private List<Integer> createdAt;
    private List<Integer> updatedAt;
    private Long idPackageDetail;
}
