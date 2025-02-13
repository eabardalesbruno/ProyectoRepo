package com.proriberaapp.ribera.services.point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransactionRequestDto {
    private Integer userId;
    private Integer bookingId;
    private PointTransactionTypeEnum type;
    private Integer transactionId;
  
}
