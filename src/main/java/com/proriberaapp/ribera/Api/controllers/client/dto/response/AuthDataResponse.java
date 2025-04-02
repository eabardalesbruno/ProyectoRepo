package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AuthDataResponse {
    private String access_token;
    private List<String> roles;
    private String username;
}
