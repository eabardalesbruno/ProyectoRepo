package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.proriberaapp.ribera.Domain.dto.WalletPointHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WalletPointsHistoryResponse {
    private WalletPointHistoryDto[] walletPointHistoryDtos;
    private MetadataResponse metadataResponse;
}
