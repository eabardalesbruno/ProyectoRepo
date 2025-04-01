package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthResponse {
    private boolean result;
    private AuthDataResponse data;
    private Date timestamp;
    private int status;

}
