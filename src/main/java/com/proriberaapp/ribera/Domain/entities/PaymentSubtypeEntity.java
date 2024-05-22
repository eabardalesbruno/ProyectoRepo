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
@Table("paymentsubtype")
public class PaymentSubtypeEntity {
    @Id
    @Column("paymentsubtypeid")
    private Integer paymentSubtypeId;
    @Column("paymentsubtypedesc")
    private String paymentSubtypeDesc;
    @Column("paymenttypeid")
    private Integer paymentTypeId;
    private Double soles;
    private Double dollars;
    private Double percentage;
    @Column("statussoles")
    private Integer statusSoles;
    @Column("statusdollars")
    private Integer statusDollars;
}
