package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponse {
    private boolean result;
    private UserDto data;
    private String timestamp;
    private int status;
}