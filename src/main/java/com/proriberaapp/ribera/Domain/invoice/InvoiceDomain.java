package com.proriberaapp.ribera.Domain.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceStatus;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;

import lombok.Data;

@Data
public class InvoiceDomain {
    private int paymentBookId;
    private InvoiceClientDomain client;
    private String keySupplier;
    private String type;
    private String serie;
    private String supplierNote;
    private String linkPdf;
    private int correlative;
    private BigDecimal totalPayment;
    private List<InvoiceItemDomain> items;
    private Date createdAt = Date.from(Instant.now());
    private InvoiceStatus status;
    private InvoiceCurrency currency;
    private double taxPercentaje;
    private UUID id;
    private BigDecimal totalIgv;
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private double percentajeDiscount;
    private double tc = 3.2;

    public InvoiceDomain(InvoiceClientDomain client, Integer paymentBookId, double percentajeIgv,
            InvoiceCurrency currency,
            InvoiceType typeP, double percentajeDiscount) {
        this.client = client;
        if (currency.compareTo(InvoiceCurrency.PEN) == 0) {
            this.tc = 0;
        }
        this.percentajeDiscount = percentajeDiscount;
        this.items = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.paymentBookId = paymentBookId;
        this.correlative = 0;
        this.taxPercentaje = percentajeIgv;
        this.createdAt = Date.from(Instant.now());
        this.currency = currency;
        this.status = InvoiceStatus.PENDINGTOSEND;
        this.type = typeP.name();
        /* this.calculatedTotals(); */
        /*
         * this.calculateInvoiceSerieName();
         */
    }

    public void addItem(InvoiceItemDomain item) {
        item.setPercentajeIgv(this.taxPercentaje);
        item.calculatedTotals(false, this.percentajeDiscount);
        this.items.add(item);
        this.calculatedTotals();

    }

    public void addItemWithIncludedIgv(InvoiceItemDomain item) {
        item.setPercentajeIgv(this.taxPercentaje);
        item.calculatedTotals(true, this.percentajeDiscount);
        this.items.add(item);
        this.calculatedTotals();
    }

    public void setCorrelative(int correlativeP) {
        this.correlative = correlativeP;

    }

    public void calculatedTotals() {
        /*
         * this.subtotal = items.stream()
         * .map(InvoiceItemDomain::getSubtotal)
         * .reduce(BigDecimal.ZERO, BigDecimal::add)
         * .setScale(2, RoundingMode.HALF_UP);
         * 
         * this.totalIgv = items.stream()
         * .map(InvoiceItemDomain::getIgv)
         * .reduce(BigDecimal.ZERO, BigDecimal::add)
         * .setScale(2, RoundingMode.HALF_UP);
         * this.totalDiscount = items.stream()
         * .map(InvoiceItemDomain::getDiscount)
         * .reduce(BigDecimal.ZERO, BigDecimal::add)
         * .setScale(2, RoundingMode.HALF_UP);
         * this.totalPayment =
         * subtotal.add(totalIgv).subtract(totalDiscount).setScale(2,
         * RoundingMode.HALF_UP);
         */
        this.subtotal = items.stream()
                .map(InvoiceItemDomain::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        this.totalIgv = items.stream()
                .map(InvoiceItemDomain::getIgv)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        this.totalDiscount = items.stream()
                .map(InvoiceItemDomain::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        this.totalPayment = items.stream()
                .map(InvoiceItemDomain::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

    }

    public void calculateInvoiceTypeName() {
        this.type = InvoiceType.getInvoiceTypeByLenght(this.client.getIdentifier().length()).name();
    }

    /*
     * public void calculateInvoiceSerieName() {
     * int maxDigits = 4;
     * String prefix = InvoiceSerie.getInvoiceSerieByName(this.getType()).name();
     * String endWith = "0".repeat(maxDigits - prefix.length() -
     * String.valueOf(this.correlative).length());
     * this.serie = prefix.concat(endWith).concat(String.valueOf(this.correlative));
     * }
     */
    @Override
    public String toString() {
        return "InvoiceDomain [client=" + client + ", subtotal=" + subtotal + ", correlative=" + correlative
                + ", createdAt=" + createdAt + ", id="
                + id + ", items=" + items + ", keySupplier=" + keySupplier + ", paymentBookId=" + paymentBookId
                + ", serie=" + serie + ", status=" + status + ", supplierNote=" + supplierNote + ", tc=" + tc
                + ", totalIgv=" + totalIgv + ", totalPayment=" + totalPayment + ", type=" + type + "]";
    }

    // Es una entidad parcial falta los
    public InvoiceEntity toEntity(int idType, int idStatus, int companyId, int idCurrency) {
        if (this.getCorrelative() == 0) {
            throw new IllegalArgumentException("El correlativo no puede ser 0");
        }
        return InvoiceEntity.builder().idPaymentBook(this.paymentBookId)
                .id(this.id)
                .totalIgv(this.totalIgv.doubleValue())
                .idCurrency(idCurrency)
                .tc(this.tc)
                .keySupplier(this.keySupplier)
                .serie(serie)
                .totalDiscount(this.totalDiscount.doubleValue())
                .percentageDiscount(this.percentajeDiscount)
                .idUser(this.client.getId())
                .correlative(this.getCorrelative())
                .supplierNote(this.supplierNote)
                .identifierClient(this.client.getIdentifier())
                .totalPayment(this.totalPayment.doubleValue())
                .createdAt(this.createdAt)
                .idStatus(idStatus)
                .idCompany(companyId)
                .linkpdf(linkPdf)
                .subtotal(this.subtotal.doubleValue())
                .idType(idType)
                .build();

    }

}
