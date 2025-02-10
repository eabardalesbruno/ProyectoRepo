package com.proriberaapp.ribera.Api.controllers.admin.dto;


import lombok.Data;

@Data
public class FeedingItemDto {

    private Integer idfeeding;
    private Integer idfamilygroup;
    private float value;
    private Integer idfeedingtype;
    private String feedingname;
    private String feedingtypename;
    private String familygroupname;

}
