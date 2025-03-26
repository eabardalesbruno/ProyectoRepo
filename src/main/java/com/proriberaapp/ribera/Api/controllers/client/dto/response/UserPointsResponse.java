package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import lombok.Data;

@Data
public class UserPointsResponse {
    private boolean result;
    private UserPointDataResponse data;
    private String timestamp;
    private int status;
}
