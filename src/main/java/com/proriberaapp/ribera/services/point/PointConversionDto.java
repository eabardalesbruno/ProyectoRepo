package com.proriberaapp.ribera.services.point;

import com.proriberaapp.ribera.Domain.dto.PointTypeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointConversionDto extends PointTransactionRequestDto {
    private Integer userId;
    private PointTransactionTypeEnum type;
    private Integer pointDebited;
    private PointTypeDto pointType;
    private String membershipName;
    private Integer transactionId;
    private Integer id;
}
