package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@Builder
public class FoodDetailVisualCountDto {

    @Column("bookingid")
    private Integer bookingId;

    @Column("idfeedingtype")
    private Integer idFeedingType;

    @Column("categoria")
    private String categoria;

    @Column("tipo_comida")
    private String tipoComida;

    @Column("precio_unitario")
    private BigDecimal precioUnitario;

    @Column("cantidad_personas")
    private Integer cantidadPersona;

    @Column("costo_total")
    private BigDecimal costoTotal;
}
