package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentDetailsPromoterDTO {

    @Column("clientname")
    private String clientName;
    @Column("paymentAmount")
    private BigDecimal paymentAmount;
    @Column("paymentDescription")
    private String paymentDescription;
    @Column("promoterName")
    private String promoterName;
    @Column("promoterWallet")
    private Integer promoterWallet;
    @Column("paymentBookId")
    private Integer paymentBookId;
    @Column("bookingId")
    private Integer bookingId;
    @Column("clientId")
    private Integer clientId;
    @Column("totalCost")
    private BigDecimal totalCost;

    public PaymentDetailsPromoterDTO() {}
}
