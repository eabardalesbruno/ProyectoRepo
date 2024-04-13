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
@Table("paymentstate")
public class PaymentStateEntity {
    @Id
    @Column("paymentstateid")
    private Integer paymentStateId;
    @Column("paymentstatename")
    private String paymentStateName;
}

