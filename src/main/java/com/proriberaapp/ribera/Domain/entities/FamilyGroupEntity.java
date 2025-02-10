package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("family_group")
public class FamilyGroupEntity {
    private Integer id;
    private String name;
    private Integer status;
}
