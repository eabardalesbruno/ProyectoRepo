package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
public class UserRewardTransferHistoryResponse {
    private Integer userRewardTransferId;
    private String symbol;
    private Double usdRewardsTransferred;
    private Double usdRewardsRemaining;
    private String fromUserFullName;
    private String toUserFullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transferDate;
    private String subCategory;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
    private String Status;
}