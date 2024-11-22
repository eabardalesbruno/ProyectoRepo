package com.proriberaapp.ribera.Domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

}
