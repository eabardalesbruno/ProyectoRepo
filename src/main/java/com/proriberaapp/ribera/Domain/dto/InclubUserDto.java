package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

@Data
public class InclubUserDto {
    private Integer idUser;
    private String username;
    private int[] creationDate;
    private String documentNumber;
    private String name;
    private String gender;
    private String lastName;
    private String email;
    private String cellPhone;
    private String documentName;
    private Integer state;
    private String sponsorName;
    private String sponsorLastName;
    private String sponsorEmail;
}
