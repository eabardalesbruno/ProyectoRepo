package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseLoginInclub {
    private UserDto user;
    private List<MembershipDto> membership;
}
