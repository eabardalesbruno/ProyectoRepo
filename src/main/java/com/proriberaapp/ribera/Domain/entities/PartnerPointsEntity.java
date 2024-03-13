package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("partnerpoints")
public class PartnerPointsEntity {
    @Id
    @Column("partnerpointid")
    private Integer partnerPointId;
    @Column("userclientid")
    private Integer userClientId;
    @Column("partnerid")
    private Integer partnerPoints;
}
