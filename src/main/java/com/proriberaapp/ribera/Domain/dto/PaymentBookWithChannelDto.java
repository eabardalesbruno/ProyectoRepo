package com.proriberaapp.ribera.Domain.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.relational.core.mapping.Column;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
public class PaymentBookWithChannelDto  {

    @Column("paymentbookid")
    private Integer paymentBookId;

    @Column("bookingid")
    private Integer bookingId;

    @Column("userclientid")
    private Integer userClientId;

    @Column("refusereasonid")
    private Integer refuseReasonId;
    @Column("cancelreasonid")
    private Integer cancelReasonId;

    @Column("paymentmethodid")
    private Integer paymentMethodId;

    @Column("paymentstateid")
    private Integer paymentStateId;

    @Column("paymenttypeid")
    private Integer paymentTypeId;

    @Column("paymentsubtypeid")
    private Integer paymentSubTypeId;

    @Column("currencytypeid")
    private Integer currencyTypeId;

    @Column("amount")
    private BigDecimal amount;

    @Column("description")
    private String description;

    @Column("paymentdate")
    private Timestamp paymentDate;

    @Column("operationcode")
    private String operationCode;

    @Column("note")
    private String note;

    @Column("totalcost")
    private BigDecimal totalCost;

    @Column("imagevoucher")
    private String imageVoucher;

    @Column("totalpoints")
    private Integer totalPoints;

    @Column("paymentcomplete")
    private Boolean paymentComplete;

    @Column("pendingpay")
    private Integer pendingpay;

    @Column("percentagediscount")
    private double percentageDiscount;

    @Column("totaldiscount")
    private double totalDiscount;

    @Column("totalcostwithoutdiscount")
    private double totalCostWithOutDiscount;

    @Column("invoicedocumentnumber")
    private String invoiceDocumentNumber;
    @Column("invoicetype")
    private String invoiceType;

    @Column("nights")
    private Integer nights;

    @Column("channel")
    private String channel;
    @Column("daybookingend")
    private String dayBookingEnd;
    @Column("daybookinginit")
    private String dayBookingInit;
}
