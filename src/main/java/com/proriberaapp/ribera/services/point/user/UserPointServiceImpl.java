package com.proriberaapp.ribera.services.point.user;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.AuthResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointDataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointsResponse;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
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
    private String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJFUDUyMDMyMTAwMDAiLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9VU0VSIiwiaWF0IjoxNzQzMDI4OTMyLCJleHAiOjE3NDMwNDY5MzJ9.rG3QVZqCo5tP6fJp3hec1U_oo_ckG5_3nRZxzh9Z2vE";
    private PasswordEncoder passwordEncoder;
    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    @Value("${inclub.api.url.user}")
    private String urlBackofficeUser;

    public UserPointServiceImpl(WebClient.Builder webClientBuilder, UserClientService userClientService, JwtProvider jtp) {
        this.jtp = jtp;
        this.userClientService = userClientService;
        this.webClient = webClientBuilder.baseUrl(urlBackOffice).build();
    }

    private Mono<String> authenticate(String username, String password) {
        System.out.println(username + " " + password);
        return webClient.post()
                .uri(urlBackOffice + "/auth/login")
                .bodyValue(Map.of("username", username, "password", password))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .map(AuthResponse::getToken)
                .doOnNext(token -> this.authToken = token);
    }

    @Override
    public Mono<UserPointDataResponse> getUserPoints(String username, Integer idMembershipFamily) {
        return webClient.get()
                .uri(urlBackofficeUser + "/" + username)
                .retrieve()
                .bodyToMono(ResponseInclubLoginDto.class)
                .flatMap(user -> webClient.get()
                        .uri(urlBackOffice + "/user-points-released/12853/2", user.getData().getId(), idMembershipFamily)
                        .header("Authorization", "Bearer " + authToken)
                        .retrieve()
                        .bodyToMono(UserPointsResponse.class)
                        .map(UserPointsResponse::getData)
                )
                .switchIfEmpty(Mono.error(new RequestException("User points not found for user: " + username)));
    }


}
