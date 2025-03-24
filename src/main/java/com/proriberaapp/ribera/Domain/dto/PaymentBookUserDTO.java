package com.proriberaapp.ribera.Domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@AllArgsConstructor
public class PaymentBookUserDTO {
    private Integer paymentbookid;
    private String operationcode;
    private String note;
    private BigDecimal totalCost;
    private String imagevoucher;
    private Integer totalpoints;
    private Boolean paymentcomplete;
    private Integer pendingpay;
    private Integer userclientid;
    private String username;
    private String useremail;
    private String useraddress;
    private String userphone;
    private String useridentifierclient;
    private String roomname;
    private Integer roomnumber;
    private Integer paymentstateid;
    private String currencytypename;
    private int currencytypeid;
    private double percentagediscount;
    private double totalcostwithoutdiscount;
    private String invoicedocumentnumber;
    private String invoicetype;
    @Column("bookingid")
    private Integer bookingid;

    @Column("fulldayid")
    private Integer fulldayid;

    @Column("type")
    private String type;

}
