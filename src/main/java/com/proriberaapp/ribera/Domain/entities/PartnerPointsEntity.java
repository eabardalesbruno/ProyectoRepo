package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("partnerpoints")
public class PartnerPointsEntity {
    private Integer partnerPointId;
    private Integer userId;
    private Integer partnerPoints;
}
