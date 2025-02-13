package com.proriberaapp.ribera.services.point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransferRequestDto extends PointTransactionRequestDto {
    private Integer sourceUserId;
    private Integer targetUserId;
    private PointTransactionTypeEnum type;
    private Integer transactionId;
    private double pointsAmount;
    private Integer id;
}
