package com.proriberaapp.ribera.Domain.entities.Invoice;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("invoiceitem")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class InvoiceItemEntity {
    @Id
    private int id;
    @Column("name")
    private String name;
    @Column("code")
    private String code;
    @Column("description")
    private String description;
    @Column("quantity")
    private Integer quantity;
    @Column("priceunit")
    private Double priceUnit;
    @Column("subtotal")
    private Double subtotal;

    @Column("igv")
    private Double igv;
    @Column("unitofmeasurement")
    private String unitOfMeasurement;

    @Column("total")
    private Double total;

    @Column("createdat")
    private Date createdAt;
    @Column("idinvoice")
    private UUID idInvoice;

    @Column("serienumber")
    private String serieNumber;

    public InvoiceItemEntity(
            UUID idInvoice,
            String name,
            String description,
            String unitOfMeasure,
            Integer quantity,
            Double priceUnit, Double subtotal,
            Double total,
            Double igv,
            Date createdAt) {
        this.createdAt = createdAt;
        this.subtotal = subtotal;
        this.igv = igv;
        this.idInvoice = idInvoice;
        this.description = description;
        this.name = name;
        this.unitOfMeasurement = unitOfMeasure;
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.total = total;
    }

    /*
     * public InvoiceItemDomain toDomain() {
     * return new InvoiceItemDomain(this.id, this.name, this.code, this.description,
     * this.quantity,
     * BigDecimal.valueOf(this.priceUnit),
     * BigDecimal.valueOf(this.subtotal), BigDecimal.valueOf(this.total),
     * BigDecimal.valueOf(this.igv),
     * this.createdAt, this.unitOfMeasurement);
     * }
     */
}
