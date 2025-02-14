package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Table("fulldaytypefood")
public class FullDayTypeFoodEntity {

    @Id
    @Column("fulldaytypefoodid")
    private Integer fullDayTypeFoodId;

    @Column("foodname")
    private String FoodName;

    @Column("fooddescription")
    private String FoodDescription;

    @Column("type")
    private String type;

    @Column("price")
    private BigDecimal price;
}
