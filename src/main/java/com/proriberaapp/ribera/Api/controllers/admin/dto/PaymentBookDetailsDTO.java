package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class PaymentBookDetailsDTO {
    private Integer paymentBookId;
    private Integer bookingId;
    private String bookingName;
    private Integer userClientId;
    private String userClientName;
    private Integer refuseReasonId;
    private String refuseReason;
    private Integer paymentMethodId;
    private String paymentMethod;
    private Integer paymentStateId;
    private String paymentState;
    private Integer paymentTypeId;
    private String paymentType;
    private Integer paymentSubTypeId;
    private String paymentSubType;
    private Integer currencyTypeId;
    private String currencyType;
    private BigDecimal amount;
    private String description;
    private Timestamp paymentDate;
    private String operationCode;
    private String note;
    private BigDecimal totalCost;
    private String imageVoucher;
    private Integer totalPoints;
    private Boolean paymentComplete;
    private Integer pendingPay;
}
