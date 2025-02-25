package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletPointRequest {
    private Integer userId;

    @NotNull(message = "El campo 'points' es requerido")
    private Double points;

    @NotBlank(message = "Tipo de punto es requerido")
    private String pointType;
    private Integer pointTypeId;

    @NotBlank(message = "Debe seleccionar una membresia")
    private Integer membershipId;
}
