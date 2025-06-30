package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.proriberaapp.ribera.Domain.entities.ExchangeHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HistoricalExchangeResponse {
    private boolean result;
    private Integer total;
    //private List<HistoricalExchangeDto> data;
    private List<ExchangeHistoryEntity> data;
    private Integer size;
    private Integer page;
}
