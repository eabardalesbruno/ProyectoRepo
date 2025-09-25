package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyDataDto {
    private String razonSocial;
    private String direccion;
    private String numeroDocumento;
    private String estado;
    private String condicion;
}
