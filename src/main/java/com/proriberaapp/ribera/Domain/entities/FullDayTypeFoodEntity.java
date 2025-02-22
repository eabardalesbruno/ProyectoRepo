package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    //nuevas entidiades
    @Column("urlimage")
    private String urlImage;

    @Column("entry")
    private String Entry;

    @Column("background")
    private String Background;

    @Column("drink")
    private String Drink;

    @Column("dessert")
    private String Dessert;

    @Column("quantity")
    private Integer quantity;

    @Column("currencytypeid")
    private Integer currencyTypeId;

}