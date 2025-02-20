package com.proriberaapp.ribera.services.point;

import com.proriberaapp.ribera.Domain.dto.PointTypeDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PointConversionDto extends PointTransactionRequestDto {
    private Integer userId;
    private PointTransactionTypeEnum type;
    private Integer pointDebited;
    private PointTypeDto pointType;
    private String membershipName;
    private Integer membershipId;
    private Integer transactionId;
    private double pointAcredited;
    private Integer id;
}
