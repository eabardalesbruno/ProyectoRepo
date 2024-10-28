package com.proriberaapp.ribera.services.promoters.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.LoginRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.TokenDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserResponse;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserPromoterRepository;
import com.proriberaapp.ribera.services.promoters.UserPromoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserPromoterServiceImpl implements UserPromoterService {

    private final UserPromoterRepository userPromoterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public Mono<TokenDto> login(LoginRequest loginRequest) {
        return userPromoterRepository.findByUsernameOrEmail(loginRequest.username(), loginRequest.email())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")))
                .map(user -> new TokenDto(jwtProvider.generateTokenPromoter(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "error generating token")));
    }

    @Override
    public Mono<UserResponse> register(RegisterRequest registerRequest) {
        String password = passwordEncoder.encode(registerRequest.password());
        UserPromoterEntity userCreate = RegisterRequest.fromPromoter(registerRequest, password);
        userCreate.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        //userCreate.setCreatedId(idUserPromoter);

        String username = registerRequest.firstName().toUpperCase() + " " + registerRequest.lastName().toUpperCase();
        return userPromoterRepository.findByUsernameOrEmailOrDocumentNumber(username , registerRequest.email(), registerRequest.document()).hasElement()
                .flatMap(exists -> exists ?
                        Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username or email or document already in use"))
                        : userPromoterRepository.save(userCreate))
                .map(UserResponse::toResponsePromoter);
    }
}
