package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class PaymentBookDetailsDTO {

    @Column("paymentBookId")
    private Integer paymentBookId;
    @Column("bookingId")
    private Integer bookingId;
    @Column("userClientId")
    private Integer userClientId;
    @Column("refuseReasonId")
    private Integer refuseReasonId;
    @Column("paymentMethodId")
    private Integer paymentMethodId;
    @Column("paymentStateId")
    private Integer paymentStateId;
    @Column("paymentTypeId")
    private Integer paymentTypeId;
    @Column("paymentSubTypeId")
    private Integer paymentSubTypeId;
    @Column("currencyTypeId")
    private Integer currencyTypeId;
    @Column("amount")
    private BigDecimal amount;
    @Column("description")
    private String description;
    @Column("paymentDate")
    private Timestamp paymentDate;
    @Column("operationCode")
    private String operationCode;
    @Column("note")
    private String note;
    @Column("totalCost")
    private BigDecimal totalCost;
    @Column("imageVoucher")
    private String imageVoucher;
    @Column("totalPoints")
    private Integer totalPoints;
    @Column("paymentComplete")
    private Boolean paymentComplete;
    @Column("pendingPay")
    private Integer pendingPay;
    @Column("userClientName")
    private String userClientName;
    @Column("userClientLastName")
    private String userClientLastName;
    @Column("userClientEmail")
    private String userClientEmail;
    @Column("userDocumentNumber")
    private String userDocumentNumber;
    @Column("userDocumentType")
    private Integer userDocumentType;
    @Column("userCellphoneNumber")
    private String userCellphoneNumber;
    @Column("bookingName")
    private String bookingName;
    @Column("paymentMethod")
    private String paymentMethod;
    @Column("paymentState")
    private String paymentState;
    @Column("paymentType")
    private String paymentType;
    @Column("paymentSubtype")
    private String paymentSubtype;
    @Column("currencyType")
    private String currencyType;
    @Column("invoiceSerie")
    private String invoiceSerie;
    @Column("invoiceLinkPdf")
    private String invoiceLinkPdf;
    @Column("percentagediscount")
    private double percentageDiscount;
    @Column("totaldiscount")
    private double totalDiscount;
    @Column("totalcostwithoutdiscount")
    private double totalCostWithOutDiscount;

    @Column("bookingstatename")
    private String bookingStateName;
    @Column("bookingstateid")
    private Integer bookingStateId;
    @Column("channel")
    private String channel;
    @Column("invoicedocumentnumber")
    private String invoiceDocumentNumber;
    @Column("invoicetype")
    private String invoiceType;
    @Column("nights")
    private Integer nights;
    @Column("daybookingend")
    private String dayBookingEnd;
    @Column("daybookinginit")
    private String dayBookingInit;
    @Column("createdat")
    private Timestamp createdat;

    @Override
    public String toString() {
        return "PaymentBookDetailsDTO{" +
                "paymentBookId=" + paymentBookId +
                ", bookingId=" + bookingId +
                ", userClientId=" + userClientId +
                ", refuseReasonId=" + refuseReasonId +
                ", paymentMethodId=" + paymentMethodId +
                ", paymentStateId=" + paymentStateId +
                ", paymentTypeId=" + paymentTypeId +
                ", paymentSubTypeId=" + paymentSubTypeId +
                ", currencyTypeId=" + currencyTypeId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", paymentDate=" + paymentDate +
                ", operationCode='" + operationCode + '\'' +
                ", note='" + note + '\'' +
                ", totalCost=" + totalCost +
                ", imageVoucher='" + imageVoucher + '\'' +
                ", totalPoints=" + totalPoints +
                ", paymentComplete=" + paymentComplete +
                ", pendingPay=" + pendingPay +
                ", userClientName='" + userClientName + '\'' +
                ", userClientLastName='" + userClientLastName + '\'' +
                ", userClientEmail='" + userClientEmail + '\'' +
                ", userDocumentNumber='" + userDocumentNumber + '\'' +
                ", userDocumentType=" + userDocumentType +
                ", userCellphoneNumber='" + userCellphoneNumber + '\'' +
                ", bookingName='" + bookingName + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentState='" + paymentState + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", paymentSubtype='" + paymentSubtype + '\'' +
                ", currencyType='" + currencyType + '\'' +
                '}';
    }
}
