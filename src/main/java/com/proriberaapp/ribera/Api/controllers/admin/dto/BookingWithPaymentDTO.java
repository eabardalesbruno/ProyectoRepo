package com.proriberaapp.ribera.Api.controllers.admin.dto;

import java.math.BigDecimal;
// import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingWithPaymentDTO {
    @Column("bookingid")
    private Integer bookingId;
    @Column("roomofferid")
    private Integer roomOfferId;
    @Column("bookingstateid")
    private Integer bookingStateId;
    @Column("userclientid")
    private Integer userClientId;
    @Column("userpromotorid")
    private Integer userPromotorId;
    @Column("costfinal")
    private BigDecimal costFinal;
    @Column("booking_createdat")
    private LocalDateTime createDatBooking;
    @Column("totalcost")
    private BigDecimal totalCost;
    @Column("paymentbookid")
    private Integer paymentBookId;
    @Column("paymentmethodid")
    private Integer paymentMethodId;
    @Column("description")
    private String description;
    @Column("paymentsubtypeid")
    private Integer paymentSubtypeId;
    @Column("paymentsubtypedesc")
    private String paymentsubtypedesc;
    @Column("roomtypename")
    private String roomTypeName;
    @Column("keysupplier")
    private String keySupplier;
    @Column("id")
    private Integer id;
    @Column("serie")
    private String serie;
    @Column("correlative")
    private Integer correlative;
    @Column("subtotal")
    private BigDecimal subtotal;
    @Column("identifierclient")
    private String identifierClient;
    @Column("invoice_createdat")
    private LocalDateTime createDatInvoice;
    @Column("idtype")
    private Integer idType;
    @Column("idcurrency")
    private Integer idCurrency;
    @Column("tc")
    private BigDecimal tc;
    @Column("totaligv")
    private BigDecimal totalIgv;
    @Column("totalpayment")
    private BigDecimal totalPayment;
    @Column("iduser")
    private Integer idUser;
    @Column("nameclient")
    private String nameClient;
    @Column("username")
    private String username;
    @Column("documenttypeid")
    private Integer documentTypeId;
}
