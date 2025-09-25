package com.proriberaapp.ribera.Domain.dto;

import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FeedingDto {
    private FeedingEntity feedingEntity;
    private List<Integer> roomOfferIds;
    private List<FeedingItemSaveDto> items;
}
