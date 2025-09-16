package com.proriberaapp.ribera.Infraestructure.externalService.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class AuthDataResponse {
    private String access_token;
    private List<String> roles;
    private String username;
}
