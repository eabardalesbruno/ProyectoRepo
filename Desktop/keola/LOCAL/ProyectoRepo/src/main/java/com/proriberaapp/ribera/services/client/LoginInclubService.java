package com.proriberaapp.ribera.services.client;

import java.util.List;

import com.proriberaapp.ribera.Api.controllers.client.dto.TokenValid;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;

import com.proriberaapp.ribera.Api.controllers.exception.TokenInvalidException;
import reactor.core.publisher.Mono;

public interface LoginInclubService {
    Mono<ResponseValidateCredential> verifiedCredentialsInclub(String username, String password);

    Mono<TokenValid> login(String userName, String password) throws TokenInvalidException;
}
