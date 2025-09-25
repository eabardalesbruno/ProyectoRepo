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
@Table("refusepayment")
public class RefusePaymentEntity {

    @Id
    @Column("refusepaymentid")
    private Integer refusePaymentId;

    @Column("paymentbookid")
    private Integer paymentBookId;

    @Column("refusereasonid")
    private Integer refuseReasonId;

    private String detail;
}