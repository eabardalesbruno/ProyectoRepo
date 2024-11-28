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
    private BigDecimal valorUnitario;

    // Este constructor sirve para crear un item de factura con los datos necesarios
    // para
    /*
     * public InvoiceItemDomain(String name, String description, int quantity,
     * double percentajeIgv,
     * BigDecimal priceUnit) {
     * this.name = name;
     * this.codProductSunat = "631210";
     * this.description = description;
     * if (quantity <= 0) {
     * throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
     * }
     * this.quantity = quantity;
     * this.priceUnit = priceUnit;
     * this.percentajeIgv = percentajeIgv;
     * this.unitOfMeasurement = "ZZ";
     * this.createdAt = DateFormat.getDateInstance().getCalendar().getTime();
     * this.calculatedTotals(false);
     * }
     */
    public InvoiceItemDomain(String name, String description, int quantity, BigDecimal priceUnit) {
        this.name = name;
        this.codProductSunat = "631210";
        this.description = description;
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.unitOfMeasurement = "ZZ";
        this.percentajeIgv = 0;
        this.createdAt = Date.from(Instant.now());
        /*
         * this.calculatedTotals(isIgvIncluded);
         */ }

    public void calculatedTotals(boolean isIgvIncluded) {
        double percentajeIgvValue = this.percentajeIgv / 100;
        if (isIgvIncluded) {
            /*
             * this.total = priceUnit.multiply(new BigDecimal(quantity)).setScale(2,
             * RoundingMode.HALF_UP);
             * this.valorUnitario = priceUnit
             * .divide(new BigDecimal(percentajeIgvValue).add(new BigDecimal(1)), 2,
             * RoundingMode.HALF_UP);
             * this.subtotal = valorUnitario.multiply(new BigDecimal(quantity)).setScale(2,
             * RoundingMode.HALF_UP);
             * this.igv = subtotal.multiply(new BigDecimal(percentajeIgvValue)).setScale(2,
             * RoundingMode.HALF_UP);
             */
            this.total = priceUnit.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
            this.igv = total.multiply(new BigDecimal(percentajeIgvValue)).setScale(2, RoundingMode.HALF_UP);
            this.valorUnitario = priceUnit
                    .divide(new BigDecimal(percentajeIgvValue).add(new BigDecimal(1)), 2,
                            RoundingMode.HALF_UP);
            this.subtotal = total.subtract(igv).setScale(2, RoundingMode.HALF_UP);
        } else {
            this.valorUnitario = priceUnit;
            this.subtotal = priceUnit.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
            this.igv = subtotal.multiply(new BigDecimal(percentajeIgvValue)).setScale(2, RoundingMode.HALF_UP);
            this.total = this.subtotal.add(this.igv).setScale(2, RoundingMode.HALF_UP);
            /*
             * this.subtotal = valorUnitario.multiply(new BigDecimal(quantity)).setScale(2,
             * RoundingMode.HALF_UP);
             */
            /*
             * this.igv = subtotal.multiply(new BigDecimal(percentajeIgvValue)).setScale(2,
             * RoundingMode.HALF_UP);
             */
            /* this.total = subtotal.add(igv).setScale(2, RoundingMode.HALF_UP); */
        }
    }

    /* public void calculatedTotals(boolean isIgvIncluded) { */
    /* if (isIgvIncluded) { */
    /*
     * this.total = priceUnit.multiply(new BigDecimal(quantity)).setScale(2,
     * RoundingMode.HALF_UP);
     */
    /*
     * this.igv = this.total.multiply(new BigDecimal(this.percentajeIgv /
     * 100)).setScale(2,
     */
    /* RoundingMode.HALF_UP); */
    /*
     * this.subtotal = this.total.subtract(this.igv).setScale(2,
     * RoundingMode.HALF_UP);
     */

    /* } else { */
    /*
     * this.subtotal = priceUnit.multiply(new BigDecimal(quantity)).setScale(2,
     * RoundingMode.HALF_UP);
     */
    /*
     * this.igv = this.subtotal.multiply(new BigDecimal(this.percentajeIgv /
     * 100)).setScale(2,
     */
    /* RoundingMode.HALF_UP); */
    /*
     * this.total = this.subtotal.add(this.igv).setScale(2, RoundingMode.HALF_UP);
     */

    /* } */

    /* } */

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
