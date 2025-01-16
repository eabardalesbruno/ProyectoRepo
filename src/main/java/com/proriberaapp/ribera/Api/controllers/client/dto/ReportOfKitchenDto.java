package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class ReportOfKitchenDto {
    private Integer numberAdults;
    private Integer numberChildren;
    private Integer numberAdultMayor;
    private Integer totalPerson;
    private Integer numberBreakfast;
    private Integer numberLunch;
    private Integer numberDinner;

    public ReportOfKitchenDto() {
        this.numberAdults = 0;
        this.numberChildren = 0;
        this.numberAdultMayor = 0;
        this.totalPerson = 0;
        this.numberBreakfast = 0;
        this.numberLunch = 0;
        this.numberDinner = 0;
    }

    
}
