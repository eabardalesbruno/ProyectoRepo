package com.proriberaapp.ribera.Domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceClientDomain {
    private String name;
    private String identifier;
    private String address;
    private String phone;
    private String email;

    InvoiceClientDomain() {

    }

}
