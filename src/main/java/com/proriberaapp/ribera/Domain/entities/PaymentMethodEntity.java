package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("paymentmethod")
public class PaymentMethodEntity {
    private Integer paymentMethodId;
    private String description;
}
