package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanionsDto {
    private String firstName;
    private String lastName;
    private String documentNumber;
    private Integer documentTypeId;
    private Integer countryId;


}
