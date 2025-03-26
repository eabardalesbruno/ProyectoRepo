package com.proriberaapp.ribera.services.point.user;

import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointDataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointsResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class UserPointServiceImpl implements UserPointService {
    private final WebClient webClient;
    private final UserClientService userClientService;
    private final JwtProvider jtp;
    private String authToken;
    private PasswordEncoder passwordEncoder;
    @Value("${inclub.api.url}")
    private String urlBackOffice;


    public UserPointServiceImpl(WebClient.Builder webClientBuilder, UserClientService userClientService, JwtProvider jtp) {
        this.jtp = jtp;
        this.userClientService = userClientService;
        this.webClient = webClientBuilder.baseUrl(urlBackOffice).build();
    }

    private Mono<String> authenticate(String username, String password) {
        return webClient.post()
                .uri(urlBackOffice + "/auth/login")
                .bodyValue(Map.of("username", username, "password", password))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .map(AuthResponse::getToken)
                .doOnNext(token -> this.authToken = token);
    }

    public Mono<UserPointDataResponse> getUserPoints(Integer idUser, Long idMembershipFamily) {
        return  userClientService.findById(idUser)
                .flatMap(user -> authenticate(user.getUsername(), user.getPassword())
                .flatMap(token -> webClient.get()
                        .uri("/user-points-released/{idUser}/{idMembershipFamily}", idUser, idMembershipFamily)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(UserPointsResponse.class)
                        .map(UserPointsResponse::getData)));
    }

}
