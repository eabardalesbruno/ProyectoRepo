package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("feeding_type")
public class FeedingTypeEntity {
    private Integer id;
    private String name;
    private Integer status;
}
