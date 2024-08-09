package com.proriberaapp.ribera.services.promoters;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TokenDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserResponse;
import reactor.core.publisher.Mono;

public interface UserPromoterService {
    Mono<TokenDto> login(LoginRequest loginRequest);
    Mono<UserResponse> register(Integer idUserPromoter, RegisterRequest registerRequest);
}
