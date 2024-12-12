package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TotalSalesDTO {

    private BigDecimal totalMonth;

    private BigDecimal totalLastMonth;

    public TotalSalesDTO() {

    }
}
