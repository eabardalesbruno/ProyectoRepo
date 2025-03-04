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
@Table("fulldaydetail")
public class FullDayDetailEntity {

    @Id
    @Column("fulldaydetailid")
    private Integer fulldaydetailid;

    @Column("fulldayid")
    private Integer fulldayid;

    @Column("typeperson")
    private String typePerson;

    @Column("quantity")
    private Integer quantity;

    @Column("baseprice")
    private BigDecimal basePrice;

    @Column("foodprice")
    private BigDecimal foodPrice;

    @Column("discountapplied")
    private BigDecimal discountApplied;

    @Column("finalprice")
    private BigDecimal finalPrice;

}
