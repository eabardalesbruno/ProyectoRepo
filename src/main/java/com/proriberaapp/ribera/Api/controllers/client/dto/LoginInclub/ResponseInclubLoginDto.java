package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseInclubLoginDto {
    private boolean result;
    private UserDto data;

}