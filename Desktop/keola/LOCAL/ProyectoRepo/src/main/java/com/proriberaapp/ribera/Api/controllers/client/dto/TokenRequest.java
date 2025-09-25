package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String username;
    private String password;
}
