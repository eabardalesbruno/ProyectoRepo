package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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

    @Column("voucher_amount")
    private Double voucherAmount;

    @Column("voucher_operationcode")
    private String voucherOperationcode;

    @Column("voucher_imagevoucher")
    private String voucherImagevoucher;

    @Column("voucher_note")
    private String voucherNote;

    @Column("voucher_currencyname")
    private String voucherCurrencyname;

    @Column("voucher_paymentsubtypename")
    private String voucherPaymentsubtypename;
}
