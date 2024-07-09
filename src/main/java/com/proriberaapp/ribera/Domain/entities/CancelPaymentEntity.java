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
@Table("cancelpayment")
public class CancelPaymentEntity {

    @Id
    @Column("cancelpaymentid")
    private Integer cancelPaymentId;

    @Column("paymentbookid")
    private Integer paymentBookId;

    @Column("cancelreasonid")
    private Integer cancelReasonId;

    private String detail;
}