package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.dto.TokenRequest;
import com.proriberaapp.ribera.Api.controllers.dto.TokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenBoServiceImpl implements TokenBoService {

    private final String API_URL = "https://servicios.inclubtest.online:2053/api/token";

    @Override
    public TokenResponse getToken(TokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<TokenRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(API_URL, entity, TokenResponse.class);
    }
}