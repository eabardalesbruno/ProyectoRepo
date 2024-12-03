package com.proriberaapp.ribera.Domain.entities.Invoice;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@Table("invoice")
@AllArgsConstructor
@Builder
public class InvoiceEntity {
    @Column("id")
    private UUID id;

    @Column("keysupplier")
    private String keySupplier;
    @Column("identifierclient")
    private String identifierClient;
    /* @MappedCollection(idColumn = "idInvoce") */
    @Column("serie")
    private String serie;
    @Column("suppliernote")
    private String supplierNote;
    @Column("linkpdf")
    private String linkpdf;
    @Column("totalpayment")
    private double totalPayment;
    @Column("totaligv")
    private double totalIgv;
    @Column("subtotal")
    private double subtotal;
    @Column("createdAt")
    @CreatedDate
    private Date createdAt;
    @Column("idstatus")
    private int idStatus;
    @Column("idcompany")
    private int idCompany;
    @Column("idtype")
    private int idType;
    @Column("tc")
    private double tc;
    @Column("idcurrency")
    private int idCurrency;

    @Column("correlative")
    private int correlative;

    @Column("iduser")
    private int idUser;

    @Column("totaldiscount")
    private double totalDiscount;
    @Column("percentagediscount")
    private double percentageDiscount;
    @Column("nameclient")
    private String nameCliente;

    /* private InvoiceTypeEntity type; */
    /* private InvoiceStatusEntity statusEntity; */
    /* private InvoiceStatusEntity status; */
    @Column("idPaymentBook")
    private int idPaymentBook;
    @MappedCollection(idColumn = "idinvoice")
    @Transient
    private List<InvoiceItemEntity> items;

    InvoiceEntity(
            double tc,
            UUID id,
            String identifierClient,
            double totalIgv,
            int companyId,
            int idType,
            int idStatus,
            int idCurrency,
            int correlative,
            int paymentBookId,
            double percentageDiscount,
            double totalDiscount,
            String serie,
            String supplierNote,
            Double totalPayment,
            Date createdAt,
            List<InvoiceItemEntity> items) {
        this.id = id;
        this.tc = tc;
        this.idCurrency = idCurrency;
        this.totalDiscount = totalDiscount;
        this.percentageDiscount = percentageDiscount;
        this.totalIgv = totalIgv;
        this.idType = idType;
        this.idStatus = idStatus;
        this.identifierClient = identifierClient;
        this.idCompany = companyId;
        this.idPaymentBook = paymentBookId;
        this.serie = serie;
        this.correlative = correlative;
        this.supplierNote = supplierNote;
        this.items = items;
        this.totalPayment = totalPayment;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "InvoiceEntity [correlative=" + correlative + ", createdAt=" + createdAt + ", id=" + id + ", idCompany="
                + idCompany + ", idCurrency=" + idCurrency + ", idPaymentBook=" + idPaymentBook + ", idStatus="
                + idStatus
                + ", idType=" + idType + ", identifierClient=" + identifierClient + ", keySupplier=" + keySupplier
                + ", serie=" + serie + ", supplierNote=" + supplierNote + ", tc=" + tc + ", totalIgv=" + totalIgv
                + ", totalPayment=" + totalPayment + "]";
    }
    /*
     * public InvoiceDomain toDomain() {
     * return new InvoiceDomain(this.paymentBookId,this.keySupplier,)
     * }
     */
}
