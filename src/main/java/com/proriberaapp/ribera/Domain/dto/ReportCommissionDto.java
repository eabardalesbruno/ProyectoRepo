package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@Builder
public class ReportCommissionDto {

    @Column("codigoReserva")
    private String codigoReserva;

    @Column("promotor")
    private String promotor;

    @Column("rucnumber")
    private String rucNumber;

    @Column("comision")
    private BigDecimal comision;

    @Column("facturaLink")
    private String facturaLink;

    @Column("estado")
    private String estado;

    @Column("nombreHabitacion")
    private String nombreHabitacion;

    @Column("estadoHabitacion")
    private String estadoHabitacion;

    @Column("nombreTitular")
    private String nombreTitular;

    @Column("costoFinal")
    private BigDecimal costoFinal;

    @Column("currencytypeid")
    private Integer currencytypeid;

    @Column("costoAlimentos")
    private BigDecimal costoAlimentos;

    @Column("costoSinAlimentos")
    private BigDecimal costoSinAlimentos;
}
