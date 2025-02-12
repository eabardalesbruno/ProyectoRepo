package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("points_conversion_factor")
@Setter
@Getter
@Builder
public class PointTypeConversionFactorEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("idpointtype")
    private Integer idpointtype;
    @Column("costpernight")
    private double costPerNight;
    @Column("state")
    private Integer state;
}
