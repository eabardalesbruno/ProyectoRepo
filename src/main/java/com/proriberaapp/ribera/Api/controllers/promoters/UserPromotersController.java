package com.proriberaapp.ribera.Api.controllers.promoters;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TokenDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.promoters.UserPromoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.promoter}")
@RequiredArgsConstructor
public class UserPromotersController {

    private final JwtProvider jwtProvider;
    private final UserPromoterService userPromoterService;

    @PostMapping("/register")
    public Mono<UserResponse> register(
            @RequestBody RegisterRequest registerRequest,
            @RequestHeader("Authorization") String token) {
        Integer idUser = jwtProvider.getIdFromToken(token.substring(7));
        return userPromoterService.register(idUser, registerRequest);
    }


    @PostMapping("/login")
    public Mono<TokenDto> login(@RequestBody LoginRequest loginRequest) {
        return userPromoterService.login(loginRequest);
    }
}
