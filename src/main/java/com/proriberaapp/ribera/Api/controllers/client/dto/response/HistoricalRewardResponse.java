package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.proriberaapp.ribera.Api.controllers.client.dto.HistoricalRewardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class HistoricalRewardResponse {
    private boolean result;
    private Integer total;
    private List<HistoricalRewardDto> data;
    private Integer size;
    private Integer page;
}
