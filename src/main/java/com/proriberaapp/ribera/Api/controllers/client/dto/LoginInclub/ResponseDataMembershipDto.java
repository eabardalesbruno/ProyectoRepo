package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDataMembershipDto {
    private boolean result;
    private List<MembershipDto> data;

    @Override
    public String toString() {
        return "ResponseDataMembershipDto(result=" + this.isResult() + ", data=" + this.getData() + ")";
    }
}
