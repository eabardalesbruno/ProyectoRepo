package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPromoterPageDto {

    private List<UserPromoterDto> userPromoter;

    private Long totalPromoters;


}
