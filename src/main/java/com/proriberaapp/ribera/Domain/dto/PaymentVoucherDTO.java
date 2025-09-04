package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PaymentVoucherDTO {
    private Double voucherAmount;
    private String voucherOperationcode;
    private String voucherImagevoucher;
    private String voucherNote;
    private String voucherCurrencyname;
    private String voucherPaymentsubtypename;
}
