package com.proriberaapp.ribera.services.point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointExchangeDto  extends PointTransactionRequestDto{
    private Integer userId;
    private double totalAmount;
    
}
