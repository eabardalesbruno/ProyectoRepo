package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("family_group")
public class FamilyGroupEntity {
    private Integer id;
    private String name;
    private Integer status;
}
