package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("benefitsubcategorydetail")
public class BenefitSubCategoryDetailEntity {
    @Column("benefitid")
    private Integer benefitId;
    @Column("subcategory")
    private String subCategoryId; //es el id de userLevelEntity
}
