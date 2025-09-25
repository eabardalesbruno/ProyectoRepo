package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private int id;
    private String email;
    private String name;
    private String lastName;
    private String username;
    private String telephone;
    private String nroDocument;
    private String address;
    private int idState;

    private int idResidenceCountry;
    private String gender;
    private int idDocument;
    private String documentName;
    private List<Integer> createDate;
}
