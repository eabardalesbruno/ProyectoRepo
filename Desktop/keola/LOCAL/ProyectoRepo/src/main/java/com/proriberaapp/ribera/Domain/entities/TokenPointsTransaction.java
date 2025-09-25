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
@Table("tokenpointstransaction")
public class TokenPointsTransaction {

    @Id
    @Column("tokenpointstransactionid")
    private Integer tokenPointsTransactionId;

    @Column("codigotoken")
    private String codigoToken;

    @Column("datecreated")
    private Timestamp dateCreated;

    @Column("expirationdate")
    private Timestamp expirationDate;

    @Column("partnerpointid")
    private Integer partnerPointId;

    @Column("bookingid")
    private Integer bookingId;
}