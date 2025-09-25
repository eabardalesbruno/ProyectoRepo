package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientCountResponseDto {
    private String tipoCliente;
    private Long cantidadClientes;
}
