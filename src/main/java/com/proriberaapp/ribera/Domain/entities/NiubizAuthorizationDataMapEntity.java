package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;

@Data
public class NiubizAuthorizationDataMapEntity {

    private String urlAddress;
    private String serviceLocationCityName;
    private String serviceLocationCountrySubdivisionCode;
    private String serviceLocationCountryCode;
    private String serviceLocationPostalCode;

}
