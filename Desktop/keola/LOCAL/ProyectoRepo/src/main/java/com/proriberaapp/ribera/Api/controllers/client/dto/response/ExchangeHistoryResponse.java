package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

@Data
public class ExchangeHistoryResponse {
    private Integer idMovementExchangeHistory;
    private Integer idUser;
    private String movementDate;
    private Integer rewards;
    private Integer pointsUsed;
    private String portfolio;
    private String observation;
}
