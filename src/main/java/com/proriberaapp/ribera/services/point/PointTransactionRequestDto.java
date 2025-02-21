package com.proriberaapp.ribera.services.point;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransactionRequestDto {
    protected Integer userId;
    protected PointTransactionTypeEnum type;
    protected Integer transactionId;
  
}
