package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TotalUsersDTO {

    private Long totalMonth;

    private Long totalLastMonth;

    public TotalUsersDTO() {

    }
}
