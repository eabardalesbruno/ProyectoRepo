package com.proriberaapp.ribera.Infraestructure.externalService.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class UserInClubResponse {
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
