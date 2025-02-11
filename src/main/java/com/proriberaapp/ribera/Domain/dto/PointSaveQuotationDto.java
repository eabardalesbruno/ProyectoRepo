package com.proriberaapp.ribera.Domain.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointSaveQuotationDto {
    private Integer idPointType;
    private double factor;
    private List<PointDaysDto> days;
    private Integer id;

}
