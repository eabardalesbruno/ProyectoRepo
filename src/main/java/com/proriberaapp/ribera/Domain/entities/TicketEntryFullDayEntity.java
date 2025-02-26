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
@Table("ticketentryfullday")
public class TicketEntryFullDayEntity {

    @Id
    @Column("ticketentryfulldayid")
    private Integer ticketEntryFullDayId;

    @Column("category")
    private String category;

    @Column("subcategory")
    private String subCategory;

    @Column("type")
    private String type;

    @Column("startdate")
    private Timestamp startDate;

    @Column("enddate")
    private Timestamp endDate;

    @Column("adultprice")
    private BigDecimal adultPrice;

    @Column("seniorprice")
    private BigDecimal seniorPrice;

    @Column("childprice")
    private BigDecimal childPrice;

    @Column("infantprice")
    private BigDecimal infantPrice;

}
