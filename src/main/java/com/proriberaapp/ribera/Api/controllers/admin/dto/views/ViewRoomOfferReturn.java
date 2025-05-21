package com.proriberaapp.ribera.Api.controllers.admin.dto.views;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.ListAmenities;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;

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
    Integer adultcapacity;
    Integer adultextra;
    String descriptionroom;
    Integer kidcapacity;
    Integer adultmayorcapacity;
    Integer infantcapacity;
    BigDecimal infantcost;
    BigDecimal kidcost;
    BigDecimal adultcost;
    BigDecimal adultmayorcost;
    BigDecimal adultextracost;
    Integer state;
    Integer numberdays;

    String description;
    Integer bedrooms;
    String squaremeters;
    Boolean oceanviewbalcony;
    Boolean balconyoverlookingpool;
    Boolean poolview;

    List<BedroomReturn> listBedroomReturn;
    List<ListAmenities> listAmenities;
    List<FeedingEntity> listFeeding;

    BigDecimal costregular; // Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
    String costregularstring; // Cost. Adulto: S/.60.00 TODO: no tenemos tabla de costos
    BigDecimal costtotal;
    BigDecimal originalcosttotal;
    String costtotalstring;
    BigDecimal costexchange; // Cost. Adult. canje: S/.50.00
    String costexchangestring; // Cost. Adult. canje: S/.50.00
    BigDecimal costtotalexchange;
    String costtotalexchangestring;
    Integer pointribera; // Pts sem: 200 pts Pts fds: 300
    String totalPerson;
    BigDecimal amountFeeding;
    Integer infantsReserve;
    Integer kidsReserve;
    Integer adultsReserve;
    Integer adultsMayorReserve;
    Integer adultsExtraReserve;
    Integer totalCapacity;
    Integer mintotalcapacity;
    Integer maxtotalcapacity;
    Integer mincapacity;
    Boolean isbooking;
    Integer numberofnights;
    BigDecimal totalreward;
    Integer totalPointsRibera;
    Integer totalPointsRiberaTop;
    Integer totalPointsRequired;
    Double totalPercentageDiscount;

    Integer offertypeid;
    Double totalDiscount;

    Integer totalPointsReward;
    Integer quotationId;

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
                .descriptionroom(row.get("descriptionroom", String.class))
                .infantcapacity(row.get("infantcapacity", Integer.class))
                .kidcapacity(row.get("kidcapacity", Integer.class))
                .adultcapacity(row.get("adultcapacity", Integer.class))
                .adultmayorcapacity(row.get("adultmayorcapacity", Integer.class))
                .adultextra(row.get("adultextra", Integer.class))
                .infantcost(row.get("infantcost", BigDecimal.class))
                .kidcost(row.get("kidcost", BigDecimal.class))
                .adultcost(row.get("adultcost", BigDecimal.class))
                .adultmayorcost(row.get("adultmayorcost", BigDecimal.class))
                .adultextracost(row.get("adultextracost", BigDecimal.class))
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
                .quotationId(row.get("quotationid", Integer.class))
                .build();
    }

}
