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
@Table("quotation")
public class QuotationEntity {
    @Id
    @Column("quotation_id")
    private Integer quotationId;

    @Column("quotation_description")
    private String quotationDescription;

    @Column("infant_cost")
    private BigDecimal infantCost;

    @Column("kid_cost")
    private BigDecimal kidCost;

    @Column("adult_cost")
    private BigDecimal adultCost;

    @Column("adult_mayor_cost")
    private BigDecimal adultMayorCost;

    @Column("adult_extra_cost")
    private BigDecimal adultExtraCost;
}
