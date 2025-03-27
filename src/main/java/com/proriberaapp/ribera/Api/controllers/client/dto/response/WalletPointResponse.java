package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

@Data
public class WalletPointResponse {
    private Long id;
    private Integer userId;
    private Integer userInclubId;
    private Integer points;
    private Integer liberatedPoints;
    private Integer idFamily;

}
