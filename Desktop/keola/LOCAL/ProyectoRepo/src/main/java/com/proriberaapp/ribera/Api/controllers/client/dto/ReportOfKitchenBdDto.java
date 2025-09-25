package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class ReportOfKitchenBdDto {

    private String roomname;
    private String roomnumber;
    private Integer numberadults;
    private Integer numberchildren;
    private Integer numberadultsextra;
    private Integer numberadultsmayor;
    private Integer numberinfants;
    private Integer totalperson;
    private Integer totaldinner;
    private Integer totalbreakfast;
    private Integer totallunch;
    private boolean isalimentation;
    private String checkin;
    private String checkout;

}