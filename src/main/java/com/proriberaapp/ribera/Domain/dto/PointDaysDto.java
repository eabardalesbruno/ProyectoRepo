package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointDaysDto {
    private Integer id;
    private Integer idPointType;
    private Integer idDay;
    private boolean selected;
}
