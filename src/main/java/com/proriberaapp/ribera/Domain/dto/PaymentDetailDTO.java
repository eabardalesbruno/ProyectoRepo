package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PaymentDetailDTO {

    private int paymentbookid;
    private Timestamp paymentdate;
    private String operationcode;
    private String imagevoucher;
    private String methodpayment;
    private String paymenttypedesc;
    private double total;

}
