package com.proriberaapp.ribera.services.promoters;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TokenDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.PromotorDataDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import reactor.core.publisher.Mono;

public interface UserPromoterService {
    Mono<TokenDto> login(LoginRequest loginRequest);
    Mono<UserResponse> register(RegisterRequest registerRequest);
    Mono<PromotorDataDTO> findPromotorDTOById(Integer id);
}
