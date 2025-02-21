package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OffertTypeDto {
    private Integer offerTypeId;
    private String offerTypeName;
}
