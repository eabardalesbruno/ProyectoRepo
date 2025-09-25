package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("discount_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DicountItemEntity {
    @Id
    private int id;
    @Column("idDiscount")
    private int discountId;
    @Column("name")
    private String name;
    @Column("code")
    private String code;
    @Column("idPackage")
    private int idPackage;

}
