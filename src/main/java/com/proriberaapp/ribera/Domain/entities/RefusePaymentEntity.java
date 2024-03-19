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
@Table("refusepayment")
public class RefusePaymentEntity {
    @Id
    @Column("refusepaymentid")
    private Integer refusePaymentId;

    @Column("paymentstateid")
    private Integer paymentStateId;

    @Column("refusereasonid")
    private Integer refuseReasonId;

    private String detail;
}