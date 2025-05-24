package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

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
@Table("membership")
public class MembershipDto {
    @Id
    @Column("membershipid")
    private Integer membershipId;
    @Column("userclientid")
    private Integer userclientId;
    @Column("id")
    private int id;
    @Column("familypackagename")
    private String familyPackageName;
    @Column("namesuscription")
    private String nameSuscription;
    @Column("status")
    private String status;
    @Column("idfamilypackage")
    private int idFamilyPackage;
    @Column("idstatus")
    private int idStatus;
    @Column("numberquotas")
    private int numberQuotas;
    @Column("idPackage")
    private int idPackage;
    @Column("creationDate")
    private String creationDate;
    @Column("volumen")
    private Integer volumen;
    @Column("data")
    private int data;
    @Column("datacreate")
    private Timestamp datacreate;
    @Column("dataupdate")
    private Timestamp dataupdate;

}
