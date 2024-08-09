package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeRequest {
    private String action;
    @JsonProperty("grant_type")
    private String grantType;
    //private String username;
    //private String password;
    private String audience;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String scope;
}
