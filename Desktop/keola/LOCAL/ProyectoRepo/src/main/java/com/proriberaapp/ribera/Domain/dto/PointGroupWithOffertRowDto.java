package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointGroupWithOffertRowDto {
    private float point;
    private Integer pointstypeid;
    private String pointstypedesc;
    private Integer offerttypeid;
}
