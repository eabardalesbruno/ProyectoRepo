package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table("complaintsbook")
public class ComplaintsBookEntity {
    @Id
    @Column("id")
    private Integer id;

    @Column("persontype")
    private String personType;

    @Column("businessname")
    private String businessName;

    @Column("ruc")
    private String ruc;

    @Column("firstname")
    private String firstName;

    @Column("lastname")
    private String lastName;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("isadult")
    private Boolean isAdult;

    @Column("address")
    private String address;

    @Column("acceptedterms")
    private Boolean acceptedTerms;

    @Column("detailcomplaintsid")
    private Integer detailComplaintId;

    @Column("servicescomplaintsid")
    private Integer servicesComplaintId;

    @Column("complaintsdescription")
    private String complaintsDescription;

    @Column("askingdescription")
    private String askingDescription;

    @Column("datesaved")
    private LocalDateTime dateSaved;
}