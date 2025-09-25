package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

@Data
public class UserPointDataResponse {
    private Integer id;
    private Integer idUser;
    private int liberatedPoints;
    private int rewards;
    private Integer idFamily;
}
