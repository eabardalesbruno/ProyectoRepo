package com.proriberaapp.ribera.Api.controllers.admin.dto.views;

import io.r2dbc.spi.Readable;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ViewAdminBookingInventoryReturn(
        Integer roomId,
        Integer roomOfferId,
        Integer roomTypeId,
        //TODO: propondo cambiar de funcion, donde ahora se marcara la fecha de inicio y fin
        LocalDateTime dateInit, // 11/01/2024
        LocalDateTime dateEnd, // 11/01/2024
        String dateString, // 11/01/2024 11:46
        Integer item,
        String numberRoom,
        String typeRoom,
        /*
        * TODO: no veo posible este campo, ya que no se puede ofrecer el mismo cuarto,
        * en todo caso, se podria cambiar el *numberRoom* para mostrar una lista de habitaciones */
        Integer stock,
        BigDecimal costRegular, //Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
        String costRegularString, //Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
        BigDecimal costTotal,
        String costTotalString,
        BigDecimal costExchange, //Cost. Adult. canje: S/.50.00
        String costExchangeString, //Cost. Adult. canje: S/.50.00
        BigDecimal costTotalExchange,
        String costTotalExchangeString,
        Integer pointRibera, //Pts sem: 200 pts Pts fds: 300
        String pointRiberaString, //Pts sem: 200 pts Pts fds: 300
        Integer pointInResort, //Pts sem: 200 pts Pts fds: 300
        String pointInResortString //Pts sem: 200 pts Pts fds: 300
) {
    public static ViewAdminBookingInventoryReturn convertTo(Readable row) {
        return new ViewAdminBookingInventoryReturn(
                row.get("roomid", Integer.class),
                row.get("roomofferid", Integer.class),
                row.get("roomtypeid", Integer.class),
                row.get("dateinit", LocalDateTime.class),
                row.get("dateend", LocalDateTime.class),
                row.get("datestring", String.class),
                row.get("item", Integer.class),
                row.get("numberroom", String.class),
                row.get("typeroom", String.class),
                row.get("stock", Integer.class),
                row.get("costregular", BigDecimal.class),
                row.get("costregularstring", String.class),
                row.get("costtotal", BigDecimal.class),
                row.get("costtotalstring", String.class),
                row.get("costexchange", BigDecimal.class),
                row.get("costexchangestring", String.class),
                row.get("costtotalexchange", BigDecimal.class),
                row.get("costtotalexchangestring", String.class),
                row.get("pointribera", Integer.class),
                row.get("pointriberastring", String.class),
                row.get("pointinresort", Integer.class),
                row.get("pointinresortstring", String.class)
        );
    }
}
