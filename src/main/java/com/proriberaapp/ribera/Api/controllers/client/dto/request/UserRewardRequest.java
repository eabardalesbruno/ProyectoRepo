package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import com.proriberaapp.ribera.Domain.enums.RewardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRewardRequest {
    private Integer userId;
    private Double  points;
    private RewardType type;
    private LocalDateTime expirationDate;
}
