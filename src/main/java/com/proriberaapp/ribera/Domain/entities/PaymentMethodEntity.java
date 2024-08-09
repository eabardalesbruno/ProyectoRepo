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
@Table("paymentmethod")
public class PaymentMethodEntity {
    @Id
    @Column("paymentmethodid")
    private Integer paymentMethodId;

    @Column("description")
    private String description;

    @Column("state")
    private StatePaymentMethod state;

    public enum StatePaymentMethod {
        ACTIVE, INACTIVE
    }
}
