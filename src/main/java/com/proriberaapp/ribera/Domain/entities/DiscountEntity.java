package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Table("discount")
@AllArgsConstructor
public class DiscountEntity {
    @Id
    @Column("id")
    private int id;
    @Column("percentage")
    private float percentage;
    @Column("name")
    private String name;
}
