package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentageDto {
    private Integer id_package_detail;
    private Integer own_hotel_total_percentage;
    private Integer own_hotel_weekend_percentage;
    private Integer own_hotel_week_day_percentage;
    private Integer other_hotel_total_percentage;
    private Integer other_hotel_weekend_percentage;
    private Integer other_hotel_week_day_percentage;
}
