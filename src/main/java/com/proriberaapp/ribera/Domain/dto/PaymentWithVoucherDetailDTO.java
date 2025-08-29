package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaymentWithVoucherDetailDTO {

    private int paymentbookid;
    private Timestamp paymentdate;
    private String operationcode;
    private String imagevoucher;
    private String methodpayment;
    private String paymenttypedesc;
    private double total;
    private List<PaymentVoucherDTO> vouchers;
}
