package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Table("pointconversion")
public class PointConversionEntity {
    @Id()
    private Integer id;
    private Integer userid;
    private double pointacredited;
    private double pointdebited;
    private Integer pointtypeid;
    private Integer transactionid;
    private String membershipname;

    
}
