package com.proriberaapp.ribera.services.client.impl;

import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.dto.PointTransactionExchangeDto;
import com.proriberaapp.ribera.Domain.dto.PointTransferRowDto;
import com.proriberaapp.ribera.Domain.dto.PointTypeDto;
import com.proriberaapp.ribera.Infraestructure.repository.PointTransactionRepository;
import com.proriberaapp.ribera.services.point.PointTransactionTypeDto;

import io.r2dbc.postgresql.codec.Point;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PointTransactionQueryService {

    private final PointTransactionRepository pointTransactionRepository;

    public Flux<PointTransactionExchangeDto> getPointExchangeByUserId(Integer userId) {
        return this.pointTransactionRepository.getPointTransactionExchangeRow(userId)
                .map(pointRow -> {
                    PointTransactionExchangeDto pointExchange = new PointTransactionExchangeDto();
                    PointTypeDto pointTypeDto = PointTypeDto.builder()
                            .pointstypeid(pointRow.getPointtypeid())
                            .pointstypedesc(pointRow.getPointtypedesc())
                            .factor(pointRow.getPointtypefactor())
                            .build();
                    PointTransactionTypeDto pointTransactionTypeDto = PointTransactionTypeDto.builder()
                            .id(pointRow.getTransactiontypeid())
                            .name(pointRow.getTransactiontypename())
                            .color(pointRow.getTransactiontypecolor())
                            .build();
                    pointExchange.setTransactionType(pointTransactionTypeDto);
                    pointExchange.setId(pointRow.getId());
                    pointExchange.setMembershipName(pointRow.getMembershipname());
                    pointExchange.setPointDebited(pointRow.getPointdebited());
                    pointExchange.setPointACredited(pointRow.getPointacredited());
                    pointExchange.setCreatedAt(pointRow.getCreated_at());
                    pointExchange.setPointType(pointTypeDto);
                    return pointExchange;
                });
    }

    public Flux<PointTransferRowDto> getPointTransfer(Integer userId) {
        return this.pointTransactionRepository.getPointTransfer(userId);
    }

}
