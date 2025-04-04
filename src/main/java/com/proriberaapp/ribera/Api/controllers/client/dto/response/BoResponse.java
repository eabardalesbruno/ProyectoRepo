package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

@Data
public class BoResponse<T> {
    private boolean result;
    private T data;
    private String timestamp;
    private int status;
}
