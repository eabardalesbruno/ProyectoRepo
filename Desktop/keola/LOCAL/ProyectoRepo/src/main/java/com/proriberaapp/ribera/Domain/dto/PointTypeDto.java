package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PointTypeDto {
    private Integer pointstypeid;
    private String pointstypedesc;
    private double factor;
    
}
