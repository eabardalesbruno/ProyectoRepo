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
@Table("paymenttype")
public class PaymentTypeEntity {
    @Id
    @Column("paymenttypeid")
    private Integer paymentTypeId;
    @Column("paymenttypedesc")
    private String paymentTypeDesc;
    @Column("countryid")
    private Integer countryId;
    @Column("paymentmethodid")
    private Integer paymentMethodId;
    @Column("owner")
    private String owner;
}
