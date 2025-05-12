package com.proriberaapp.ribera.services.point.user;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserPointDataResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.BoResponse;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserPointServiceImpl implements UserPointService {
    private final WebClient webClient;
    @Value("${backoffice.api.url}")
    private String urlBackOffice;

    @Value("${inclub.api.url.user}")
    private String urlBackofficeUser;

    public UserPointServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(urlBackOffice).build();
    }

    @Override
    public Mono<UserPointDataResponse> getUserPoints(String username, Integer idMembershipFamily, String tokenBackOffice) {
        return webClient.get()
                .uri(urlBackofficeUser + "/" + username)
                .retrieve()
                .bodyToMono(ResponseInclubLoginDto.class)
                .flatMap(user -> webClient.get()
                        .uri(urlBackOffice + "/user-points-released/{userId}/{idMembershipFamily}", user.getData().getId(), idMembershipFamily)
                        .header("Authorization", "Bearer " + tokenBackOffice)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<BoResponse<UserPointDataResponse>>() {})
                        .map(BoResponse::getData)
                )
                .switchIfEmpty(Mono.error(new RequestException("User points not found for user: " + username)));
    }


}
