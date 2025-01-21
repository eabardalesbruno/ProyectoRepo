package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table("day")
@Setter
@Getter
public class dayEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("name")
    private String name;
}
