package com.proriberaapp.ribera.Api.controllers.admin.dto.views;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.ListAmenities;
import io.r2dbc.spi.Readable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ViewRoomOfferReturn {
        /*
        Integer roomId;
        Integer roomOfferId;
        Integer roomTypeId;

        Integer item;

        //TODO: propondo cambiar de funcion, donde ahora se marcara la fecha de inicio y fin
        //LocalDateTime offerTimeInit, // 11/01/2024
        //LocalDateTime offerTimeEnd, // 11/01/2024
        //String offerTimeString, // 11/01/2024 11:46

        String numberRoom;
        String typeRoom;

        String image;
        String capacity;

        String description;
        Integer bedrooms;
        String squareMeters;
        Boolean oceanViewBalcony;
        Boolean balconyOverLookingPool;

        List<BedroomReturn> listBedroomReturn;
        List<ListAmenities> listAmenities;

        BigDecimal costRegular; //Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
        String costRegularString; //Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
        BigDecimal costTotal;
        String costTotalString;
        BigDecimal costExchange; //Cost. Adult. canje: S/.50.00
        String costExchangeString; //Cost. Adult. canje: S/.50.00
        BigDecimal costTotalExchange;
        String costTotalExchangeString;
        Integer pointRibera; //Pts sem: 200 pts Pts fds: 300
        String pointRiberaString; //Pts sem: 200 pts Pts fds: 300
        Integer pointInResort; //Pts sem: 200 pts Pts fds: 300
        String pointInResortString; //Pts sem: 200 pts Pts fds: 300


    public static ViewRoomOfferReturn convertTo(Readable row) {
        return ViewRoomOfferReturn.builder()
                .roomId(row.get("roomid", Integer.class))
                .roomOfferId(row.get("roomofferid", Integer.class))
                .roomTypeId(row.get("roomtypeid", Integer.class))
                .item(row.get("item", Integer.class))
                //.offerTimeInit(row.get("offertimeinit", LocalDateTime.class))
                //.offerTimeEnd(row.get("offertimeend", LocalDateTime.class))
                //.offerTimeString(row.get("offertimestring", String.class))
                .numberRoom(row.get("numberroom", String.class))
                .typeRoom(row.get("typeroom", String.class))
                .image(row.get("image", String.class))
                .capacity(row.get("capacity", String.class))
                .description(row.get("description", String.class))
                .bedrooms(row.get("bedrooms", Integer.class))
                .squareMeters(row.get("squaremeters", String.class))
                .oceanViewBalcony(row.get("oceanviewbalcony", Boolean.class))
                .balconyOverLookingPool(row.get("balconyoverlookingpool", Boolean.class))
                .costRegular(row.get("costregular", BigDecimal.class))
                .costRegularString(row.get("costregularstring", String.class))
                .costTotal(row.get("costtotal", BigDecimal.class))
                .costTotalString(row.get("costtotalstring", String.class))
                .costExchange(row.get("costexchange", BigDecimal.class))
                .costExchangeString(row.get("costexchangestring", String.class))
                .costTotalExchange(row.get("costtotalexchange", BigDecimal.class))
                .costTotalExchangeString(row.get("costtotalexchangestring", String.class))
                .pointRibera(row.get("pointribera", Integer.class))
                .pointRiberaString(row.get("pointriberastring", String.class))
                .pointInResort(row.get("pointinresort", Integer.class))
                .pointInResortString(row.get("pointinresortstring", String.class))
                .build();
    }
         */
        Integer roomId;
        Integer roomOfferId;
        Integer roomtypeid;

        Integer item;

        // Nuevos campos para las fechas de la oferta
        LocalDateTime offertimeinit; // Fecha de inicio de la oferta
        LocalDateTime offertimeend; // Fecha de fin de la oferta
        String offertimestring; // Cadena que representa la fecha de la oferta

        String numberroom;
        String typeroom;

        String image;
        Integer capacity;

        String description;
        Integer bedrooms;
        String squaremeters;
        Boolean oceanviewbalcony;
        Boolean balconyoverlookingpool;

        List<BedroomReturn> listBedroomReturn;
        List<ListAmenities> listAmenities;

        BigDecimal costregular; // Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
        String costregularstring; // Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
        BigDecimal costtotal;
        String costtotalstring;
        BigDecimal costexchange; // Cost. Adult. canje: S/.50.00
        String costexchangestring; // Cost. Adult. canje: S/.50.00
        BigDecimal costtotalexchange;
        String costtotalexchangestring;
        Integer pointribera; // Pts sem: 200 pts Pts fds: 300
        String pointriberastring; // Pts sem: 200 pts Pts fds: 300
        Integer pointinresort; // Pts sem: 200 pts Pts fds: 300
        String pointinresortstring; // Pts sem: 200 pts Pts fds: 300

        public static ViewRoomOfferReturn convertTo(Readable row) {
                return ViewRoomOfferReturn.builder()
                        .roomId(row.get("roomid", Integer.class))
                        .roomOfferId(row.get("roomofferid", Integer.class))
                        .roomtypeid(row.get("roomtypeid", Integer.class))
                        .item(row.get("item", Integer.class))
                        .offertimeinit(row.get("offertimeinit", LocalDateTime.class))
                        .offertimeend(row.get("offertimeend", LocalDateTime.class))
                        .offertimestring(row.get("offertimestring", String.class))
                        .numberroom(row.get("numberroom", String.class))
                        .typeroom(row.get("typeroom", String.class))
                        .image(row.get("image", String.class))
                        .capacity(row.get("capacity", Integer.class))
                        .description(row.get("description", String.class))
                        .bedrooms(row.get("bedrooms", Integer.class))
                        .squaremeters(row.get("squaremeters", String.class))
                        .oceanviewbalcony(row.get("oceanviewbalcony", Boolean.class))
                        .balconyoverlookingpool(row.get("balconyoverlookingpool", Boolean.class))
                        .costregular(row.get("costregular", BigDecimal.class))
                        .costregularstring(row.get("costregularstring", String.class))
                        .costtotal(row.get("costtotal", BigDecimal.class))
                        .costtotalstring(row.get("costtotalstring", String.class))
                        .costexchange(row.get("costexchange", BigDecimal.class))
                        .costexchangestring(row.get("costexchangestring", String.class))
                        .costtotalexchange(row.get("costtotalexchange", BigDecimal.class))
                        .costtotalexchangestring(row.get("costtotalexchangestring", String.class))
                        .pointribera(row.get("pointribera", Integer.class))
                        .pointriberastring(row.get("pointriberastring", String.class))
                        .pointinresort(row.get("pointinresort", Integer.class))
                        .pointinresortstring(row.get("pointinresortstring", String.class))
                        .build();
        }
}

