package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExchangeHistoryRequest {
    private Integer bookingId;//lo uasare para sacar el  roomofer para la habitacion que se uso apraq la descripcion
    private Integer userId;
    private String username;//viene del aprobar ,cuando ssea vacio o length = 0 poner null
    private String exchangeDate;//viene de aprobar
    //private String exchangeType; esto sera una cosntante que diga canje de RECOMPENSA DE USD REWARDS
    //private String exchangeCode; sera null
    //private String service;estp sera una cosntante que diga alojamiento
    //private String description; se usara el bookingId para sacar la descripcion
    private String checkInDate;//viene del aprobar
    private String checkOutDate;//viene del aprobar
    private Double usdRewards;//viene del aprobar
}
