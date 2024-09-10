package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("restaurant")
public class RestaurantEntity {
    @Id
    @Column("restaurantid")
    private Integer restaurantId;

    @Column("categoryid")
    private Integer categoryId;

    @Column("categoryname")
    private String categoryName;

    @Column("productname")
    private String productName;

    @Column("productdescri")
    private String productDescri;

    @Column("currencytypename")
    private String currencyTypeName;

    @Column("price")
    private Double price;

    @Column("percentage")
    private Double percentage;

    @Column("pointsribera")
    private Integer pointsRibera;

    @Column("pointsinresort")
    private Integer pointsInResort;

    @Column("image")
    private String image;

    @Column("status")
    private String status;
}
