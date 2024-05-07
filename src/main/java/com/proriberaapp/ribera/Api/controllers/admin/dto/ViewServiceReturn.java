package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ViewServiceReturn {

    private Integer roomofferid;
    private Integer roomid;
    private String roomname;
    private String roomtypename;
    private String capacity;

    private BigDecimal costweekly;
    private BigDecimal costweekend;

    private Integer riberapointweekly;
    private Integer riberapointweekend;
    private Integer inresortpointweekly;
    private Integer inresortpointweekend;
    private String squaremeters;
    private String bedrooms;

    private Boolean oceanviewbalcony;
    private Boolean balconyoverlookingpool;

    private List<ListAmenities> listAmenities;
    private List<ListAmenities> listService;

    private String image;
    private String stateroomname;

    @Data
    @Builder
    public static class ListAmenities {
        private String comforttypename;
        private String comforttypedescription;
        private Boolean active;
    }
}
