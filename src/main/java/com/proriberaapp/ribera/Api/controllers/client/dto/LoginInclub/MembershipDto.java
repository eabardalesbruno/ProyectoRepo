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
    @Column("nameSuscription")
    private String nameSuscription;
    @Column("status")
    private String status;
    @Column("idFamilyPackage")
    private int idFamilyPackage;
    @Column("idStatus")
    private int idStatus;
    @Column("numberQuotas")
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
