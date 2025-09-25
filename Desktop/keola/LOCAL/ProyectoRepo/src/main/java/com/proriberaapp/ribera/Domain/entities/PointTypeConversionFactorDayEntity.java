package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("points_coversion_factor_day")
@Setter
@Getter
@Builder
public class PointTypeConversionFactorDayEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("idpointtype")
    private Integer idpointtype;
    @Column("idday")
    private Integer idDay;
    @Column("idconversionfactor")
    private Integer idConversionFactor;
}
