package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("cancelpayment")
@AllArgsConstructor
@NoArgsConstructor
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