package com.proriberaapp.ribera.Domain.entities.Invoice;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("invoiceCorrelative")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class InvoiceCorrelativeEntity {
    @Id
    private int id;
    @Column("invoiceTypeId")
    private int invoiceTypeId;
    @Column("correlative")
    private int correlative;
    InvoiceTypeEntity invoiceType;
}
