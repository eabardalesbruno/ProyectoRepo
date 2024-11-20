package com.proriberaapp.ribera.Domain.entities.Invoice;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;

import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("invoicetype")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class InvoiceTypeEntity {
    @Id
    private int id;
    @Column("name")
    private String name;
    @Column("serie")
    private String serie;
    @Column("correlative")
    private int correlative;
}
