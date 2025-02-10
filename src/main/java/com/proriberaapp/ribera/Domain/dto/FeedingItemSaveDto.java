package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

@Data
public class FeedingItemSaveDto {
    private Integer feedingTypeId;
    private Integer familyGroupId;
    private Integer feedingId;
    private float value;
}