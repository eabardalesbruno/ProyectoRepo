package com.proriberaapp.ribera.services.client;

import java.util.List;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;

import reactor.core.publisher.Mono;

public interface LoginInclubService {
    Mono<ResponseValidateCredential> verifiedCredentialsInclub(String username, String password);

    Mono<String> login(String userName, String password);
}
