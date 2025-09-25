package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class PointRedemptionHistoryDto {
    private Integer id;
    private Integer idUser;
    private String redemptionType;   //Tipo de canje (Promocional, etc.)
    private String redemptionCode;   //Código de canje
    private String serviceType;      //Tipo de servicio (Tienda online, etc.)
    private String description;
    private String usageDate;      //Fecha de uso
    private String checkInDate;    //Fecha de check-in
    private String checkOutDate;  //Fecha de check-out
    private Integer rewards;     //Puntos rewards
    private Integer usedPoints; //Puntos usados

}
