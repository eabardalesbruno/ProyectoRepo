package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenValid;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthDataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.WalletCreationResponse;
import com.proriberaapp.ribera.Api.controllers.exception.CredentialsInvalidException;
import com.proriberaapp.ribera.Api.controllers.exception.TokenInvalidException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.LoginInclubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginInclubServiceImpl implements LoginInclubService {
    @Value("${inclub.api.url.user}")
    private String URL_LOGIN_USER;

    @Value("${inclub.api.url.validate.credentials}")
    private String URL_VALIDATE_PASSWORD;

    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    @Value("${wallet.microservice.url}")
    private String walletMsUrl;

    private final UserClientRepository userClientRepository;
    private final JwtProvider jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final WalletServiceImpl walletServiceImpl;
    private final WebClient webClient;
    private WebClient webClientWallet;

    @PostConstruct
    public void init() {
        this.webClientWallet = WebClient.builder().baseUrl(walletMsUrl).build();
    }

    @Override
    public Mono<TokenValid> login(String userName, String password) throws TokenInvalidException {
        WebClient webClient = WebClient.create(this.URL_LOGIN_USER);
        return Mono.zip(this.verifiedCredentialsInclub(userName, password),
                        authenticateBackOffice(userName, password))
                .flatMap(data -> {
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
                                            // Generar token temporal para el usuario
                                            String tempToken = jwtUtil.generateToken(user);
                                            
                                            // Intentar crear wallet en el microservicio con fallback graceful
                                            return webClientWallet.post()
                                                    .uri("/api/v1/wallet/create/{idUser}", user.getUserClientId())
                                                    .header("Authorization", "Bearer " + tempToken)
                                                    .retrieve()
                                                    .bodyToMono(WalletCreationResponse.class)
                                                    .flatMap(walletResponse -> {
                                                        // Éxito: Asociar wallet al usuario
                                                        user.setWalletId(walletResponse.getData().getWalletId());
                                                        if (responseInclubLoginDto.getData().getIdState() == 1) {
                                                            user.setStatus(StatesUser.ACTIVE);
                                                        } else {
                                                            user.setStatus(StatesUser.INACTIVE);
                                                        }
                                                        return userClientRepository.save(user) // Guardamos el usuario con la wallet
                                                                .flatMap(userClientEntity -> {
                                                                    TokenValid tokenValid = new TokenValid(jwtUtil.generateToken(user), tokenBackOffice);
                                                                    return Mono.just(tokenValid); // Devolvemos el token
                                                                });
                                                    })
                                                    .doOnSuccess(token -> log.info("Wallet creada en MS para usuario InClub {}", user.getUserClientId()))
                                                    .onErrorResume(error -> {
                                                        // Fallback graceful: Continuar sin wallet
                                                        log.warn("Error creando wallet en MS para usuario InClub {}, continuando sin wallet: {}", 
                                                                user.getUserClientId(), error.getMessage());
                                                        if (responseInclubLoginDto.getData().getIdState() == 1) {
                                                            user.setStatus(StatesUser.ACTIVE);
                                                        } else {
                                                            user.setStatus(StatesUser.INACTIVE);
                                                        }
                                                        return userClientRepository.save(user)
                                                                .flatMap(userClientEntity -> {
                                                                    TokenValid tokenValid = new TokenValid(jwtUtil.generateToken(user), tokenBackOffice);
                                                                    return Mono.just(tokenValid);
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
