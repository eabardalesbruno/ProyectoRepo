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
@Table("benefit")
public class BenefitEntity {
    @Id
    @Column("benefitid")
    private Integer benefitId;
    @Column("benefitname")
    private String benefitName;
    private String description;
    @Column("imageurl")
    private String imageUrl;
}
