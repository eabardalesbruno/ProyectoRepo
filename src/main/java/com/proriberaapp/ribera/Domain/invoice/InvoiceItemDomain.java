package com.proriberaapp.ribera.Domain.invoice;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.math.RoundingMode;

import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceItemEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceItemDomain {
    private String id;
    private String name;
    private String codProductSunat;
    private String description;
    private int quantity;
    private BigDecimal priceUnit;
    private BigDecimal subtotal;
    private BigDecimal total;
    private BigDecimal igv;
    private Date createdAt;
    private String unitOfMeasurement;
    private double percentajeIgv;

    // Este constructor sirve para crear un item de factura con los datos necesarios
    // para
    public InvoiceItemDomain(String name, String description, int quantity, double percentajeIgv,
            BigDecimal priceUnit) {
        this.name = name;
        this.codProductSunat = "631210";
        this.description = description;
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.percentajeIgv = percentajeIgv;
        this.unitOfMeasurement = "ZZ";
        this.subtotal = priceUnit.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
        this.igv = this.subtotal.multiply(new BigDecimal(percentajeIgv / 100)).setScale(2, RoundingMode.HALF_UP);
        this.total = this.subtotal.add(this.igv).setScale(2, RoundingMode.HALF_UP);
        this.createdAt = DateFormat.getDateInstance().getCalendar().getTime();
    }

    public InvoiceItemDomain(String name, String description, int quantity, BigDecimal priceUnit) {
        this.name = name;
        this.codProductSunat = "631210";
        this.description = description;
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.unitOfMeasurement = "UND";
        this.percentajeIgv = 18;
        this.createdAt = Date.from(Instant.now());
        this.calculatedTotals();
    }

    public void calculatedTotals() {
        this.subtotal = priceUnit.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
        this.igv = this.subtotal.multiply(new BigDecimal(this.percentajeIgv / 100)).setScale(2, RoundingMode.HALF_UP);
        this.total = this.subtotal.add(this.igv).setScale(2, RoundingMode.HALF_UP);
    }

    public InvoiceItemEntity toEntity(UUID idInvoice) {
        return InvoiceItemEntity.builder()
                .name(this.name)
                .code(this.codProductSunat)
                .description(this.description)
                .quantity(this.quantity)
                .priceUnit(this.priceUnit.doubleValue())
                .subtotal(this.subtotal.doubleValue())
                .total(this.total.doubleValue())
                .igv(this.igv.doubleValue())
                .createdAt(this.createdAt)
                .unitOfMeasurement(this.unitOfMeasurement)
                .idInvoice(
                        idInvoice)
                .build();
    }

}
