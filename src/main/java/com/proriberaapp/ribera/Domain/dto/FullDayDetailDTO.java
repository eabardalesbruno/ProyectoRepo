package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@Builder
public class FullDayDetailDTO {

    @Column("fulldaydetailid")
    private Integer fulldayDetailId;

    @Column("typeperson")
    private String typePerson;

    @Column("quantity")
    private Integer quantity;

    @Column("finalprice")
    private BigDecimal finalPrice;

    @Column("type")
    private String type;
}
