package com.proriberaapp.ribera.services.point;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PointTransferRequestDto extends PointTransactionRequestDto {
    private Integer sourceUserId;
    private Integer targetUserId;
    private PointTransactionTypeEnum type;
    private Integer transactionId;
    private double pointsAmount;
    private Integer id;
}
