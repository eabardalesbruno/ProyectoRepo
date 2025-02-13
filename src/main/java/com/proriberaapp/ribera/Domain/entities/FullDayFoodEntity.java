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
@Table("fulldayfood")
public class FullDayFoodEntity {

    @Id
    @Column("fulldayfoodid")
    private Integer fulldayfoodid;

    @Column("fulldaydetailid")
    private Integer fulldaydetailid;

    @Column("fulldaytypefoodid")
    private Integer fulldayTypefoodid;

    @Column("quantity")
    private Integer quantity;

}
