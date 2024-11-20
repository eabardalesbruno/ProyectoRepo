package com.proriberaapp.ribera.Domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyDomain {
    private String name;
    private String ruc;
    private String address;
    private String phone;
    private String email;
    private String webPage;
    private String logo;

}
