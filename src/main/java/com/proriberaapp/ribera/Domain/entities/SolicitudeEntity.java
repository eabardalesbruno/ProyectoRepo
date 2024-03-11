package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.Solicitude;
import com.proriberaapp.ribera.Domain.enums.SolicitudeState;
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
@Table("solicitude")
public class SolicitudeEntity {
    @Id
    @Column("solicitudeid")
    private Integer solicitudeId;
    private String email;
    private String phone;
    private String message;
    @Column("codesend")
    private String codeSend;
    private SolicitudeState status;
    private Solicitude type;
    @Column("createdid")
    private Integer createdId;
    @Column("createdat")
    private Timestamp createdAt;
    @Column("responseid")
    private Integer responseId;
    @Column("responseat")
    private Timestamp responseAt;
}
