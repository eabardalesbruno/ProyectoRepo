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
@Table("companions")
public class CompanionsEntity {
    @Id
    @Column("companionid")
    private Integer companionId;

    @Column("firstname")
    private String firstName;

    @Column("lastname")
    private String lastName;

    @Column("birthdate")
    private Timestamp birthDate;

    @Column("years")
    private Integer years;

    @Column("countryid")
    private Integer countryId;

    @Column("documenttypeid")
    private Integer documentTypeId;

    @Column("documentnumber")
    private String documentNumber;

    @Column("bookingid")
    private Integer bookingId;
}
