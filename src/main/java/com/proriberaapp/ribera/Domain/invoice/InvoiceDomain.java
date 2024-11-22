package com.proriberaapp.ribera.Domain.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.Instant;

import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceItemEntity;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceSerie;
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
    private double tc = 3.2;

    // Este constructor sirve para crear una factura con los datos necesarios para
    // enviarla a la API
    public InvoiceDomain(
            InvoiceClientDomain client,
            int paymentBookId,
            double igvPercentaje,
            int lastCorrelative,
            InvoiceCurrency currency,
            List<InvoiceItemDomain> items) {
        this.taxPercentaje = igvPercentaje;
        if (currency.compareTo(InvoiceCurrency.PEN) == 0) {
            this.tc = 0;
        }
        this.client = client;
        this.items = items;
        this.id = UUID.randomUUID();
        this.paymentBookId = paymentBookId;
        this.correlative = lastCorrelative + 1;
        this.createdAt = Date.from(Instant.now());
        this.currency = currency;
        this.status = InvoiceStatus.PENDINGTOSEND;
        this.calculatedTotals();
        this.calculateInvoiceTypeName();
        this.calculateInvoiceSerieName();
    }

    public InvoiceDomain(InvoiceClientDomain client,
            int paymentBookId,
            int lastCorrelative,
            InvoiceCurrency currency,
            List<InvoiceItemDomain> items) {
        this.client = client;
        this.items = items;
        if (currency.compareTo(InvoiceCurrency.PEN) == 0) {
            this.tc = 0;
        }
        this.id = UUID.randomUUID();
        this.paymentBookId = paymentBookId;
        this.correlative = lastCorrelative + 1;
        this.taxPercentaje = 18;
        this.createdAt = Date.from(Instant.now());
        this.currency = currency;
        this.status = InvoiceStatus.PENDINGTOSEND;
        this.calculatedTotals();
        this.calculateInvoiceTypeName();
        this.calculateInvoiceSerieName();
    }

    public InvoiceDomain(InvoiceClientDomain client,
            int paymentBookId,
            int lastCorrelative,
            InvoiceCurrency currency) {
        this.client = client;
        if (currency.compareTo(InvoiceCurrency.PEN) == 0) {
            this.tc = 0;
        }
        this.items = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.paymentBookId = paymentBookId;
        this.correlative = lastCorrelative + 1;
        this.taxPercentaje = 18;
        this.createdAt = Date.from(Instant.now());
        this.currency = currency;
        this.status = InvoiceStatus.PENDINGTOSEND;
        this.calculatedTotals();
        this.calculateInvoiceTypeName();
        this.calculateInvoiceSerieName();
    }

    public InvoiceDomain(InvoiceClientDomain client,
            int paymentBookId,
            double percentajeIgv,
            int lastCorrelative,
            InvoiceCurrency currency) {
        this.client = client;
        if (currency.compareTo(InvoiceCurrency.PEN) == 0) {
            this.tc = 0;
        }
        this.items = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.paymentBookId = paymentBookId;
        this.correlative = lastCorrelative + 1;
        this.taxPercentaje = percentajeIgv;
        this.createdAt = Date.from(Instant.now());
        this.currency = currency;
        this.status = InvoiceStatus.PENDINGTOSEND;
        this.calculatedTotals();

        this.calculateInvoiceTypeName();
        this.calculateInvoiceSerieName();
    }

    public void addItem(InvoiceItemDomain item) {
        item.setPercentajeIgv(this.taxPercentaje);
        item.calculatedTotals(false);
        this.items.add(item);

    }

    public void addItemWithIncludedIgv(InvoiceItemDomain item) {
        item.setPercentajeIgv(this.taxPercentaje);
        item.calculatedTotals(true);
        this.calculatedTotals();
        this.items.add(item);

    }

    public void setCorrelative(int correlative) {
        this.correlative = correlative;
        this.calculateInvoiceSerieName();
    }

    public void calculatedTotals() {
        this.subtotal = items.stream()
                .map(InvoiceItemDomain::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        this.totalIgv = items.stream()
                .map(InvoiceItemDomain::getIgv)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        this.totalPayment = subtotal.add(totalIgv).setScale(2, RoundingMode.HALF_UP);

    }

    /* private void calculatedTotalPayment() { */
    /*
     * this.totalPayment = this.items.stream().map(item ->
     * item.getTotal()).reduce(new BigDecimal(
     */
    /* 0.0), BigDecimal::add).setScale(2, RoundingMode.HALF_UP); */
    /* } */

    /* private void calculatedTotalIgv() { */
    /*
     * this.totalIgv = this.items.stream().map(item -> item.getIgv()).reduce(new
     * BigDecimal(
     */
    /* 0.0), BigDecimal::add).setScale(2, RoundingMode.HALF_UP); */
    /* } */

    public void calculateInvoiceTypeName() {
        this.type = InvoiceType.getInvoiceTypeByLenght(this.client.getIdentifier().length()).name();
    }

    public void calculateInvoiceSerieName() {
        int maxDigits = 4;
        String prefix = InvoiceSerie.getInvoiceSerieByName(this.getType()).name();
        String endWith = "0".repeat(maxDigits - prefix.length() - String.valueOf(this.correlative).length());
        this.serie = prefix.concat(endWith).concat(String.valueOf(this.correlative));
    }

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
        return InvoiceEntity.builder().idPaymentBook(this.paymentBookId)
                .id(this.id)
                .totalIgv(this.totalIgv.doubleValue())
                .idCurrency(idCurrency)
                .tc(this.tc)
                .keySupplier(this.keySupplier)
                .identifierClient(this.client.getIdentifier())
                .serie(this.serie)
                .correlative(this.correlative)
                .supplierNote(this.supplierNote)
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
