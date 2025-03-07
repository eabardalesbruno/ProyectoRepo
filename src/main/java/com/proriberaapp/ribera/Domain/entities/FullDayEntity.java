package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@Table("fullday")
public class FullDayEntity {

    @Id
    @Column("fulldayid")
    private Integer fulldayid;

    @Column("receptionistid")
    private Integer receptionistId;

    @Column("userpromoterid")
    private Integer userPromoterId;

    @Column("userclientid")
    private Integer userClientId;

    @Column("type")
    private String type;

    @Column("purchasedate")
    private Timestamp purchaseDate;

    @Column("totalprice")
    private BigDecimal totalPrice;

    @Column("bookingstateid")
    private Integer bookingstateid;

    @Column("bookingdate")
    private Timestamp  bookingDate;

}