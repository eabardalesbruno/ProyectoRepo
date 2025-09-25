package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PointConversionRateDto {
    private Integer id;
    private Integer idFamily;
    private String familyName;
    private Double conversionRate;
    private String createdAt;
    private Integer status;
}
