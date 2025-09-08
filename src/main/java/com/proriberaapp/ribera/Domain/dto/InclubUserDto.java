package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

@Data
public class InclubUserDto {
    private Integer idBeneficiary;
    private Integer idSubscription;
    private Integer userId;
    private Integer documentTypeId;
    private Integer residenceCountryId;
    private String name;
    private String lastName;
    private String gender; // M / F
    private String email;
    private Long nroDocument; // numérico largo
    private String ageDate; // fecha en String (yyyy-MM-dd)
    private Integer status; // estado
    private String isAdult; // Y / N
    private String creationDate; // "yyyy-MM-dd HH:mm:ss"
    private String expirationDate; // "yyyy-MM-dd HH:mm:ss"
}
