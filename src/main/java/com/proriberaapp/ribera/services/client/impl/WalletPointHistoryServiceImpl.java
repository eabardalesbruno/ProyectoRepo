package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointsHistoryResponse;
import com.proriberaapp.ribera.Domain.mapper.WalletPointHistoryMapper;
import com.proriberaapp.ribera.Infraestructure.repository.WalletPointHistoryRepository;
import com.proriberaapp.ribera.services.client.WalletPointHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletPointHistoryServiceImpl implements WalletPointHistoryService {

    private final WalletPointHistoryRepository walletPointHistoryRepository;
    private final WalletPointHistoryMapper walletPointHistoryMapper;

    @Override
    public Mono<WalletPointsHistoryResponse> findPointsHistoryByUserId(
            Integer userId, String startDate, String endDate, Integer limit, Integer offset) {
        return Mono.zip(
                walletPointHistoryRepository
                        .findPointsHistoryByUserIdAndRangeDate(userId, startDate, endDate, limit, offset).collectList(),
                walletPointHistoryRepository.countListPointsHistoryByUserId(userId, startDate, endDate)
                )
                .map(tuple2 -> walletPointHistoryMapper.toWalletPointsHistoryResponse(
                        tuple2.getT1().stream(),limit, offset, tuple2.getT2()
                ))
                .doOnError(e -> log.error("Error retrieving wallet point history", e))
                .doOnSuccess(value -> log.info("End of the findPointsHistoryByUserId"));
    }
}
