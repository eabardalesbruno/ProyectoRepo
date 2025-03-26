package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

@Data
public class UserPointDataResponse {
    private Long id;
    private Long idUser;
    private int liberatedPoints;
    private int rewards;
    private Long idFamily;
}
