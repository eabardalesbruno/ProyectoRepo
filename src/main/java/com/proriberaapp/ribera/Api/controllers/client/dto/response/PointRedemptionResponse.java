package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointRedemptionHistoryDto;
import lombok.Data;

import java.util.List;

@Data
public class PointRedemptionResponse {
    private boolean result;
    private List<PointRedemptionHistoryDto> data;
    private String timestamp;
    private int status;

}
