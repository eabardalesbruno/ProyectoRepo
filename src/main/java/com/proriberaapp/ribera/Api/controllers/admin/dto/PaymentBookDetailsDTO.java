package com.proriberaapp.ribera.Api.controllers.admin.dto;

import io.r2dbc.spi.Parameter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class PaymentBookDetailsDTO {
    private Integer paymentBookId;
    private Integer bookingId;
    private Integer userClientId;
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

    private String userClientName;
    private String bookingName;
    private String paymentMethod;
    private String paymentState;
    private String paymentType;
    private String paymentSubtype;
    private String currencyType;
}
