package com.proriberaapp.ribera.Domain.mapper;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.MetadataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointsHistoryResponse;
import com.proriberaapp.ribera.Domain.dto.WalletPointHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface WalletPointHistoryMapper {

    @Mapping(target = "walletPointHistoryDtos", source = "walletPointHistoryDtos")
    @Mapping(target = "metadata", expression = "java(toMetadataResponse(limit, offset, totalElements))")
    WalletPointsHistoryResponse toWalletPointsHistoryResponse(
            Stream<WalletPointHistoryDto> walletPointHistoryDtos, Integer limit, Integer offset, long totalElements);

    MetadataResponse toMetadataResponse(Integer limit, Integer offset, long totalElements);
}
