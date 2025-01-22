package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class quotationDayDto {
    private Integer quotationId;
    private String name;
    private Integer id;
    private String shortname;
    private boolean selected;
}
