package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenValid;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthDataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthResponse;
import com.proriberaapp.ribera.Api.controllers.exception.CredentialsInvalidException;
import com.proriberaapp.ribera.Api.controllers.exception.TokenInvalidException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.LoginInclubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginInclubServiceImpl implements LoginInclubService {
    @Value("${inclub.api.url.user}")
    private String URL_LOGIN_USER;

    @Value("${inclub.api.url.validate.credentials}")
    private String URL_VALIDATE_PASSWORD;

    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    private final UserClientRepository userClientRepository;

    private final JwtProvider jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final WalletServiceImpl walletServiceImpl;
    private final WebClient webClient;

    @Override
    public Mono<TokenValid> login(String userName, String password) throws TokenInvalidException {
        WebClient webClient = WebClient.create(this.URL_LOGIN_USER);
        return Mono.zip(this.verifiedCredentialsInclub(userName, password),
                        authenticateBackOffice(userName, password))
                .flatMap(data -> {
                    System.out.println(data.getT1());
                    System.out.println(data.getT2());
                    ResponseValidateCredential responseValidate = data.getT1();
                    String tokenBackOffice = data.getT2();
                    if (!responseValidate.isData()) {
                        return Mono.error(new CredentialsInvalidException());
                    }
                    return webClient.get()
                            .uri("/" + userName)
                            .retrieve()
                            .bodyToMono(ResponseInclubLoginDto.class)
                            .flatMap(responseInclubLoginDto -> this.userClientRepository.findByUsername(userName)
                                    .flatMap(user -> {
                                        // Verificar si el usuario tiene una wallet asociada
                                        if (user.getWalletId() == null) {
                                            // Si no tiene wallet, crear una nueva
                                            return walletServiceImpl.createWalletUsuario(user.getUserClientId(), 1)
                                                    .flatMap(wallet -> {
                                                        // Asociamos la wallet al usuario
                                                        user.setWalletId(wallet.getWalletId());
                                                        if (responseInclubLoginDto.getData().getIdState() == 1) {
                                                            user.setStatus(StatesUser.ACTIVE);
                                                        } else {
                                                            user.setStatus(StatesUser.INACTIVE);
                                                        }
                                                        return userClientRepository.save(user) // Guardamos el usuario con la wallet
                                                                .flatMap(userClientEntity -> {
                                                                    TokenValid tokenValid = null; // Generar token
                                                                    tokenValid = new TokenValid(jwtUtil.generateToken(user), tokenBackOffice);

                                                                    return Mono.just(tokenValid); // Devolvemos el token
                                                                });

                                                    });
                                        } else {
                                            if (responseInclubLoginDto.getData().getIdState() == 1) {
                                                user.setStatus(StatesUser.ACTIVE);
                                            } else {
                                                user.setStatus(StatesUser.INACTIVE);
                                            }
                                            return userClientRepository.save(user) // Guardamos el usuario con la wallet
                                                    .flatMap(userClientEntity -> {
                                                        TokenValid tokenValid = new TokenValid(jwtUtil.generateToken(user), tokenBackOffice); // Generar token
                                                        return Mono.just(tokenValid); // Devolvemos el token
                                                    });
                                        }
                                    }).switchIfEmpty(Mono.defer(() -> {
                                        long currentTimeMillis = System.currentTimeMillis();
                                        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
                                        UserClientEntity userClientEntity = UserClientEntity.builder()
                                                .address(responseInclubLoginDto.getData().getAddress() != null ? responseInclubLoginDto.getData().getAddress() : null)
                                                .email(responseInclubLoginDto.getData().getEmail())
                                                .googleEmail(null)
                                                .userLevelId(1)
                                                .countryId(76)
                                                .role(1)
                                                .googleId(null)
                                                .username(responseInclubLoginDto.getData().getUsername())
                                                .registerTypeId(13)
                                                .documentNumber(
                                                        responseInclubLoginDto.getData().getNroDocument())
                                                .documenttypeId(1)
                                                .createdat(currentTimestamp)
                                                .cellNumber(responseInclubLoginDto.getData().getTelephone())
                                                .firstName(responseInclubLoginDto.getData().getName())
                                                .lastName(responseInclubLoginDto.getData().getLastName())
                                                .password(passwordEncoder.encode(password))
                                                .isUserInclub(true)
                                                .status(responseInclubLoginDto.getData().getIdState() == 1 ? StatesUser.ACTIVE : StatesUser.INACTIVE)
                                                .build();
                                        return this.userClientRepository.save(userClientEntity)
                                                .flatMap(userClient -> {
                                                    TokenValid tokenValid;
                                                    tokenValid = new TokenValid(jwtUtil.generateToken(userClientEntity), tokenBackOffice);
                                                    return Mono.just(tokenValid);
                                                });
                                    })));
                });
    }

    @Override
    public Mono<ResponseValidateCredential> verifiedCredentialsInclub(String username, String password) {
        UserDto user = UserDto.builder().username(username).password(password).build();
        return webClient.post()
                .uri(URL_VALIDATE_PASSWORD)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(ResponseValidateCredential.class);
    }

    private Mono<String> authenticateBackOffice(String username, String password) {
        return webClient.post()
                .uri(urlBackOffice + "/auth/login")
                .bodyValue(Map.of("username", username, "password", password))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .map(AuthResponse::getData).map(AuthDataResponse::getAccess_token)
                .flatMap(Mono::just);
    }
}
