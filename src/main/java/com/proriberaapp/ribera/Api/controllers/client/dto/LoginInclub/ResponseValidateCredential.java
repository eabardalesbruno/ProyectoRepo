package com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseValidateCredential {
    private boolean result;
    private boolean data;

    @Override
    public String toString() {
        return "ResponseValidateCredential(result=" + this.isResult() + ", data=" + this.isData() + ")";
    }
}
