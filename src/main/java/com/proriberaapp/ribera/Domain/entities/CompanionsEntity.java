package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("companions")
public class CompanionsEntity {
    @Id
    @Column("companionid")
    private Integer companionId;

    @Column("category")
    private String category;

    @Column("firstname")
    private String firstname;

    @Column("lastname")
    private String lastname;

    @Column("birthdate")
    private Timestamp birthdate;

    @Column("years")
    private Integer years;

    @Column("typedocumentid")
    private Integer typeDocumentId;

    @Column("documentnumber")
    private String documentNumber;

    @Column("genderid")
    private Integer genderId;

    @Column("countryid")
    private Integer countryId;

    @Column("cellphone")
    private String cellphone;

    @Column("email")
    private String email;

    @Column("bookingid")
    private Integer bookingId;

    @Column("istitular")
    private boolean isTitular = false;

    @Column("fulldayid")
    private Integer fulldayid;
}
