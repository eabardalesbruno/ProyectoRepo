package com.proriberaapp.ribera.Domain.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointSaveQuotationDto {
    private PointTypeDto pointType;
    private double costPerNight;
    private List<PointDaysDto> days;
    private Integer id;

}
