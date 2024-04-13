package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.dto.TokenRequest;
import com.proriberaapp.ribera.Api.controllers.dto.TokenResponse;

public interface TokenBoService {
    TokenResponse getToken(TokenRequest request);
}