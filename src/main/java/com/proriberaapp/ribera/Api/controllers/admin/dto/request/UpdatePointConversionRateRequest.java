package com.proriberaapp.ribera.Api.controllers.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdatePointConversionRateRequest {

    private Integer idPointConversionRate;
    private Double conversionRate;
    private Integer status;
}
