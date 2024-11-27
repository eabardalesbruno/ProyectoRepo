package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDto {
    private int id;
    private String naneSubscription;
    private String status;
    private int idFamilyPackage;
    private int statusId;
    private int numberQuotas;
    private int idPackage;
}
