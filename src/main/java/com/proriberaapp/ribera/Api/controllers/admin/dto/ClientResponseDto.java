package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientResponseDto {

        private Integer  tipoCliente;
        private String nombres;
        private String apellidos;
        private String dni;

    }