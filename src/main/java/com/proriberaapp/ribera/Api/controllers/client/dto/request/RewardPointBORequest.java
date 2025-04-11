package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardPointBORequest {
    private Integer idUser;
    private Integer idFamily;
    private Double pointsToConvert;
}
