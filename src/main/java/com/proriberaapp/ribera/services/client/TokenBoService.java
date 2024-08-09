package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.TokenRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenResponse;

public interface TokenBoService {
    TokenResponse getToken(TokenRequest request);
}