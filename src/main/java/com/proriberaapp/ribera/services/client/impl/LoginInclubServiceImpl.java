package com.proriberaapp.ribera.services.client.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Api.controllers.client.dto.TokenValid;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;
import com.proriberaapp.ribera.Api.controllers.exception.CredentialsInvalidException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.LoginInclubService;

import reactor.core.publisher.Mono;

@Service
public class LoginInclubServiceImpl implements LoginInclubService {
    @Value("${inclub.api.url.user}")
    private String URL_LOGIN_USER;

    @Value("${inclub.api.url.validate.credentials}")
    private String URL_VALIDATE_PASSWORD;

    @Autowired
    private UserClientRepository userClientRepository;
    @Autowired
    private JwtProvider jwtUtil;
    /* @Autowired */
    /* private UserClientService userClientService; */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WalletServiceImpl walletServiceImpl;

    @Override
    public Mono<TokenValid> login(String userName, String password) {
        WebClient webClient = WebClient.create(this.URL_LOGIN_USER);
        Mono<TokenValid> response = this.userClientRepository.findByUsername(userName)
                .switchIfEmpty(
                        this.verifiedCredentialsInclub(userName, password)
                                .flatMap(responseValidate -> {
                                    if (!responseValidate.isData()) {
                                        return Mono.error(new CredentialsInvalidException());
                                    }
                                    return webClient.get()
                                            .uri("/" + userName)
                                            .retrieve()
                                            .bodyToMono(ResponseInclubLoginDto.class)
                                            .flatMap(responseInclubLoginDto -> {
                                                long currentTimeMillis = System.currentTimeMillis();
                                                Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
                                                UserClientEntity userClientEntity = UserClientEntity.builder()
                                                        .address(null)
                                                        .email(responseInclubLoginDto.getData().getEmail())
                                                        .googleEmail(null)

                                                        .userLevelId(1)
                                                        .countryId(76)
                                                        .role(1)
                                                        .googleId(null)
                                                        .password(password)
                                                        .username(responseInclubLoginDto.getData().getUsername())
                                                        .registerTypeId(13)
                                                        .documentNumber(
                                                                responseInclubLoginDto.getData().getNroDocument())
                                                        .documenttypeId(1)
                                                        .createdat(
                                                                currentTimestamp)
                                                        .cellNumber(responseInclubLoginDto.getData().getTelephone())
                                                        .firstName(responseInclubLoginDto.getData().getName())
                                                        .lastName(responseInclubLoginDto.getData().getLastName())
                                                        .password(passwordEncoder.encode(password))
                                                        .build();

                                                return this.userClientRepository.save(userClientEntity);
                                            });
                                }))
                .flatMap(user -> {
                    if (!this.passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new CredentialsInvalidException());
                    }
                    // Verificar si el usuario tiene una wallet asociada
                    if (user.getWalletId() == null) {
                        // Si no tiene wallet, crear una nueva
                        return walletServiceImpl.createWalletUsuario(user.getUserClientId(), 1)
                                .flatMap(wallet -> {
                                    // Asociamos la wallet al usuario
                                    user.setWalletId(wallet.getWalletId());
                                    return userClientRepository.save(user)  // Guardamos el usuario con la wallet
                                            .thenReturn(user); // Continuamos con el flujo
                                });
                    } else {
                        return Mono.just(user); // Si ya tiene wallet, no hacemos nada
                    }
                })
                .flatMap(user -> {
                    TokenValid tokenValid = new TokenValid(jwtUtil.generateToken(user)); // Generar token
                    return Mono.just(tokenValid); // Devolvemos el token
                });

        return response;
    }

    @Override
    public Mono<ResponseValidateCredential> verifiedCredentialsInclub(String username, String password) {
        WebClient webClient = WebClient.create();
        UserDto user = UserDto.builder().username(username).password(password).build();
        return webClient.post()
                .uri(URL_VALIDATE_PASSWORD)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(ResponseValidateCredential.class).doOnNext(System.out::println);
    }

    private String encodeValue(String value) {

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
