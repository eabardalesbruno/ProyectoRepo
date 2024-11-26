package com.proriberaapp.ribera.services.client.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${inclub.api.url.subscriptions}")
    private String URL_MEMBERSHIPS;
    @Value("${inclub.api.url.validate-password}")
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
        String url = UriComponentsBuilder.fromHttpUrl(URL_VALIDATE_PASSWORD)
                .queryParam("username", username)
                .queryParam("password", password)
                .toUriString();
        return webClient.post()
                .uri(url)
                .bodyValue(new LoginRequestDTO(username, password))
                .retrieve()
                .bodyToMono(ResponseValidateCredential.class);

    }

    @Override
    public Mono<List<MembershipDto>> loadMemberships(int userId) {
        WebClient webClient = WebClient.create(URL_MEMBERSHIPS);

        return webClient.post()
                .uri("/".concat(
                        String.valueOf(userId)))
                .retrieve()
                .bodyToMono(ResponseDataMembershipDto.class)
                .flatMap(response -> {
                    return Mono.just(response.getData().stream().filter(p -> p.getIdFamilyPackage() == 1).toList());
                });

    }

}
