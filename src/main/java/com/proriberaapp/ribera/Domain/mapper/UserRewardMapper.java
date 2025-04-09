package com.proriberaapp.ribera.Domain.mapper;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletPointResponse;
import com.proriberaapp.ribera.Domain.entities.UserRewardEntity;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRewardMapper {
    @Mapping(target = "userId", source = "userId")
    UserRewardEntity toEntity(UserRewardRequest request);
    UserRewardResponse toDto(UserRewardEntity entity);
}