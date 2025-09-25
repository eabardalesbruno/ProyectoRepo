package com.proriberaapp.ribera.Domain.entities;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
@Table("paymenttoken")
public class PaymentTokenEntity {

    @Id
    @Column("paymenttokenid")
    private Integer paymentTokenId;

    @Column("paymenttoken")
    private String paymentToken;

    @Column("startdate")
    private Timestamp startDate;

    @Column("enddate")
    private Timestamp endDate;

    @Column("bookingid")
    private Integer bookingId;

    @Column("paymentbookid")
    private Integer paymentBookId;

}