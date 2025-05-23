package com.proriberaapp.ribera.Domain.invoice;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.math.RoundingMode;

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
    private double percentajeDiscount;
    private BigDecimal discount;
    private String descriptionDiscount;

    public InvoiceItemDomain(String name,String codProductSunat, String description, int quantity, BigDecimal priceUnit, String descriptionDiscount) {
        this.name = name;
        this.codProductSunat = codProductSunat;
        this.description = description;
        this.percentajeDiscount = 0;
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.unitOfMeasurement = "ZZ";
        this.percentajeIgv = 0;
        this.createdAt = Date.from(Instant.now());
        this.descriptionDiscount = descriptionDiscount;
        /*
         * this.calculatedTotals(isIgvIncluded);
         */ }

    /*
     * public void calculatedTotals(boolean isIgvIncluded) {
     * double igvValue = this.percentajeIgv / 100;
     * if (isIgvIncluded) {
     * this.total = priceUnit.multiply(new BigDecimal(quantity)).setScale(2,
     * RoundingMode.HALF_UP);
     * this.valorUnitario = priceUnit.divide(BigDecimal.valueOf(1 + igvValue), 2,
     * RoundingMode.HALF_UP);
     * this.subtotal = valorUnitario.multiply(new BigDecimal(quantity)).setScale(2,
     * RoundingMode.HALF_UP);
     * this.igv = subtotal.multiply(BigDecimal.valueOf(igvValue)).setScale(2,
     * RoundingMode.HALF_UP);
     * } else {
     * this.valorUnitario = priceUnit;
     * this.subtotal = priceUnit.multiply(new BigDecimal(quantity)).setScale(2,
     * RoundingMode.HALF_UP);
     * this.igv = subtotal.multiply(BigDecimal.valueOf(igvValue)).setScale(2,
     * RoundingMode.HALF_UP);
     * this.total = this.subtotal.add(this.igv).setScale(2, RoundingMode.HALF_UP);
     * }
     * }
     */
    public void calculatedTotals(boolean isIgvIncluded, double percentageDiscountP) {
        double igvValue = this.percentajeIgv / 100;
        this.percentajeDiscount = percentageDiscountP;
        double discountValue = this.percentajeDiscount / 100;
        if (isIgvIncluded) {
            this.discount = new BigDecimal(priceUnit.doubleValue() * discountValue).setScale(2, RoundingMode.HALF_DOWN);
            double discountPrice = priceUnit.doubleValue() - priceUnit.doubleValue()*discountValue;
            this.valorUnitario = new BigDecimal((discountPrice)/(1 + igvValue)).setScale(2, RoundingMode.HALF_UP);
            //this.valorUnitario = priceUnit.divide(BigDecimal.valueOf(1 + igvValue), 2, RoundingMode.HALF_UP);
            //this.discount = valorUnitario.multiply(BigDecimal.valueOf(discountValue))
            //        .setScale(2, RoundingMode.HALF_UP);
            this.subtotal = valorUnitario.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
            this.igv = new BigDecimal(priceUnit.doubleValue()*quantity - valorUnitario.doubleValue()*quantity).setScale(2, RoundingMode.HALF_UP);
            //this.igv = subtotal.multiply(BigDecimal.valueOf(igvValue)).setScale(2, RoundingMode.HALF_UP);
            /*
             * this.total = priceUnit.multiply(new
             * BigDecimal(quantity)).subtract(discount).setScale(2,
             * RoundingMode.HALF_UP);
             */
            this.total = igv.add(subtotal);
        } else {
            this.valorUnitario = priceUnit;
            this.subtotal = priceUnit.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
            this.igv = subtotal.multiply(BigDecimal.valueOf(igvValue)).setScale(2, RoundingMode.HALF_UP);
            this.discount = subtotal.multiply(BigDecimal.valueOf(discountValue)).setScale(2, RoundingMode.HALF_UP);
            this.total = subtotal.add(igv).subtract(discount).setScale(2, RoundingMode.HALF_UP);
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
                .priceUnit(this.valorUnitario.doubleValue())
                .subtotal(this.subtotal.doubleValue())
                .total(this.total.doubleValue())
                .igv(this.igv.doubleValue())
                .totalDiscount(this.discount.doubleValue())
                .createdAt(this.createdAt)
                .unitOfMeasurement(this.unitOfMeasurement)
                .idInvoice(
                        idInvoice)
                .build();
    }

}
