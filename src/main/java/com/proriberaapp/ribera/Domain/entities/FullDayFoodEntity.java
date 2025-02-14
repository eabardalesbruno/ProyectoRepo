package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
