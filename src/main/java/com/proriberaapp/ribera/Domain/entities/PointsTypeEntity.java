package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pointstype")
public class PointsTypeEntity {
    @Id
    private Integer pointstypeid;
    private String pointstypedesc;
}