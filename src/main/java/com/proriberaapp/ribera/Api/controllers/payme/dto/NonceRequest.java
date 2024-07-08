package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonceRequest {
    private String action;
    //private String username;
    private String audience;
    @JsonProperty("client_id")
    private String clientId;
    private String scope;
}
