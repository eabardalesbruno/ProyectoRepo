package com.proriberaapp.ribera.services.client.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginRequestDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseDataMembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;
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

    @Override
    public Mono<String> login(String userName, String password) {
        WebClient webClient = WebClient.create(this.URL_LOGIN_USER);
        Mono<String> response = this.userClientRepository.findByUsername(userName)
                .switchIfEmpty(this.verifiedCredentialsInclub(userName, password)
                        .flatMap(responseValidate -> {
                            if (!responseValidate.isData()) {
                                return Mono.error(new RuntimeException("Error credenciales incorrectas") {
                                });
                            }
                            return webClient.post()
                                    .bodyValue(new LoginRequestDTO(userName, password))
                                    .retrieve()
                                    .bodyToMono(ResponseInclubLoginDto.class)
                                    .flatMap(responseInclubLoginDto -> {
                                        UserClientEntity userClientEntity = UserClientEntity.builder()
                                                .address(null)
                                                .email(responseInclubLoginDto.getData().getEmail())
                                                .googleEmail(null)
                                                .googleId(null)
                                                .password(password)
                                                .username(responseInclubLoginDto.getData().getUsername())
                                                .registerTypeId(13)
                                                .documentNumber(
                                                        null)
                                                .cellNumber(responseInclubLoginDto.getData().getTelephone())
                                                .firstName(responseInclubLoginDto.getData().getName())
                                                .lastName(responseInclubLoginDto.getData().getLastName())
                                                .password(passwordEncoder.encode(password))
                                                .build();

                                        return this.userClientRepository.save(userClientEntity);
                                    });
                        }))
                .flatMap(user -> {
                    return Mono.just(jwtUtil.generateToken(user));
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
                .bodyToMono(ResponseValidateCredential.class).doOnNext(System.out::println).map(d -> {
                    System.out.println(d);
                    return d;
                });

    }

    private String encodeValue(String value) {

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
