package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.TransferRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PasswordValidationResponse;
import reactor.core.publisher.Mono;

public interface UserRewardTrasferHistoryService {

    Mono<Void> transferRewards(TransferRequest request);
    Mono<PasswordValidationResponse> validatePassword(String email, String rawPassword);

}