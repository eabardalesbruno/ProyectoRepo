package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table("beneficiary")
public class BeneficiaryEntity {
    @Id
    private Integer id;
    @Column("name")
    private String name;
    @Column("lastname")
    private String lastName;
    @Column("document_number")
    private String documentNumber;
    @Column("birth_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @Column("email")
    private String email;
    @Column("visits")
    private Integer visits;
    @Column("id_membership")
    private Integer idMembership;
    @Column("username")
    private String username;
    @Column("status")
    private Integer status;
    @Column("last_checkin")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastCheckin;
    @Column("creation_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime creationDate;
}
