package com.proriberaapp.ribera.Domain.mapper;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletPointMapper {
    @Mapping(target = "userId", source = "userId")
    WalletPointEntity toEntity(WalletPointRequest request);
    WalletPointResponse toDto(WalletPointEntity entity);
}
