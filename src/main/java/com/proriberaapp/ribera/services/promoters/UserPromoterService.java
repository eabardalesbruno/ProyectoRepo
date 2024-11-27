package com.proriberaapp.ribera.services.promoters;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TokenDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.PromotorDataDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenResult;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import reactor.core.publisher.Mono;

public interface UserPromoterService {
    Mono<TokenDto> login(LoginRequest loginRequest);
    Mono<UserPromoterEntity> register(UserPromoterEntity registerRequest, String randomPassword);
    Mono<PromotorDataDTO> findPromotorDTOById(Integer id);
    Mono<String> loginWithGoogle(String googleId);
    Mono<TokenResult> checkAndGenerateToken(String email);
    Mono<UserPromoterEntity> findByEmail(String email);
}
