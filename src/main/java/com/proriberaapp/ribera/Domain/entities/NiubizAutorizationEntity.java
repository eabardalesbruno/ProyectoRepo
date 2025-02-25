package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;

@Data
public class NiubizAutorizationEntity {
    private String transactionToken;
    private String customerEmail;
}
