package com.proriberaapp.ribera.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WalletPointHistoryDto {

    private Double points;
    private String datebuy;
}