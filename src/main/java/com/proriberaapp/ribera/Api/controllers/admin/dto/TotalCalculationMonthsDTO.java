package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalCalculationMonthsDTO {

    private Long totalMonth;

    private Long totalLastMonth;

    public TotalCalculationMonthsDTO() {

    }
}
