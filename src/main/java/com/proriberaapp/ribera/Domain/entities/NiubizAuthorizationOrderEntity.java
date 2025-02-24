package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;

@Data
public class NiubizAuthorizationOrderEntity {

    private String tokenId;
    private String purchaseNumber;
    private Double amount;
    private String currency;

}
