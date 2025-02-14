package com.proriberaapp.ribera.services.point;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PointTransactionTypeDto {
    private Integer id;
    private String name;
    private String color;
}
