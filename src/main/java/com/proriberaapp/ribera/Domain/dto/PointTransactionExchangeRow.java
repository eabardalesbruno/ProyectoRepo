package com.proriberaapp.ribera.Domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransactionExchangeRow extends PointTransactionBaseRowDto {
    private String userid;
    private String membershipname;
    private double pointdebited;
    private double pointacredited;
    private String created_at;
    private double pointtypefactor;
    private String pointtypedesc;
    private Integer pointtypeid;
}
