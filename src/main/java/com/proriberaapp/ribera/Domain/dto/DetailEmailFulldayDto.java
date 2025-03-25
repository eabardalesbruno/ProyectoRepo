package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@Data
public class DetailEmailFulldayDto {

    @Column("userEmail")
    private String email;

    @Column("titularName")
    private String name;

    @Column("reservationCode")
    private Integer fulldayid;

    @Column("checkInDateEntry")
    private String checkinEntry;

    @Column("checkInDateSalida")
    private String checkinExit;

    @Column("type")
    private String typefullday;

    @Column("adults")
    private Integer Adults;

    @Column("children")
    private Integer Children;
}