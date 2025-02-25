package com.proriberaapp.ribera.Domain.dto;

import java.time.LocalDateTime;

import com.proriberaapp.ribera.services.point.PointTransactionTypeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransactionExchangeDto {
    private Integer id;
    private PointTransactionTypeDto transactionType;
    private PointTypeDto pointType;
    private String membershipName;
    private double pointDebited;
    private double pointAcredited;
    private String createdAt;
}
