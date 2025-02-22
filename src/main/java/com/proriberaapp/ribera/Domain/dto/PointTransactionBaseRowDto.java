package com.proriberaapp.ribera.Domain.dto;

import com.proriberaapp.ribera.services.point.PointTransactionTypeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransactionBaseRowDto {
    private Integer id;
    private String transactiontypename;
    private Integer transactiontypeid;
    private String transactiontypecolor;
}
