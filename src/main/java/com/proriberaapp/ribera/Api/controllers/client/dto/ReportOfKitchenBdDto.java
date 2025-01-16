package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class ReportOfKitchenBdDto {
    private Integer numberadults;
    private Integer numberchildren;
    private Integer numberadultextra;
    private Integer numberadultmayor;
    private Integer totalperson;
    private boolean isalimentation;

}