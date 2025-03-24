package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class VisualCountDetailsDTO {

    @Column("bookingid")
    private Integer bookingId;

    @Column("cantidad_personas")
    private String cantidadPersonas;

    @Column("rango_fechas")
    private String rangoFechas;

    @Column("incluye_desayuno")
    private String incluyeDesayuno;
}
