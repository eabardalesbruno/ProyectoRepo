package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("point_conversion_rate")
public class PointConversionRateEntity extends Auditable {
    @Id
    private Integer id;

    @Column("family_id")
    private Integer familyId;

    @Column("conversion_rate")
    private Double conversionRate;
}
