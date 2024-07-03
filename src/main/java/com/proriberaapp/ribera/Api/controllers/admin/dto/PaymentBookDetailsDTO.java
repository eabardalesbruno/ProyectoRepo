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
    private Integer userClientId;
    private String userClientName;
    private String bookingName;
    private Integer refuseReasonId;
    private Integer paymentMethodId;
    private Integer paymentStateId;
    private Integer paymentTypeId;
    private Integer paymentSubTypeId;
    private Integer currencyTypeId;
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
