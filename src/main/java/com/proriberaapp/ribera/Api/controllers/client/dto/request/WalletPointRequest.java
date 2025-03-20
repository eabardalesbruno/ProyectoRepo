package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletPointRequest {
    private Integer userId;

    @NotNull(message = "El campo 'points' es requerido")
    private Double rewardPoints;

    private String pointType;
    private Integer pointTypeId;

}
