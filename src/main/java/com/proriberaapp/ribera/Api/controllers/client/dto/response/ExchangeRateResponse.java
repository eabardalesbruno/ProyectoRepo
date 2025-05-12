package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

@Data
public class ExchangeRateResponse {
    private Integer idExchangeRate;
    private Double buys;
    private Double sale;
}
