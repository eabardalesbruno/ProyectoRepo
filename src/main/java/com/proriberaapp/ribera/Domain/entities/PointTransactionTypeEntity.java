package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("pointtransactiontype")
@Getter
@Setter
@Builder
public class PointTransactionTypeEntity {
    @Id()
    private Integer id;
    private String name;
    private String color;
    private Integer status;

}
