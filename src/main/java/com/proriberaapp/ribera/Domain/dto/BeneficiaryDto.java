package com.proriberaapp.ribera.Domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeneficiaryDto {
    private Integer id; // idBeneficiary
    private Integer idSubscription;
    private Integer userId;
    private Integer documentTypeId;
    private Integer residenceCountryId;
    private String name;
    private String lastName;
    private String gender;
    private String email;
    private String documentNumber; // nroDocument
    private String birthDate; // ageDate
    private Integer status;
    private String isAdult;
    private String creationDate;
    private String expirationDate;
    private String membershipName;
}
