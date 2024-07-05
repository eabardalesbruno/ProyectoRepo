package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonceResponse {
    private String action;
    private Boolean success;
    private String nonce;
    private String scope;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}