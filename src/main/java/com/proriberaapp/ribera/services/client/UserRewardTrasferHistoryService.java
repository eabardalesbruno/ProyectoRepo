package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.TransferRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.PasswordValidationResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.UserRewardTransferHistoryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface UserRewardTrasferHistoryService {

    Mono<Void> transferRewards(TransferRequest request);
    Mono<PasswordValidationResponse> validatePassword(String email, String rawPassword);
    Flux<UserRewardTransferHistoryResponse> getFilteredTransfers(String subcategory, String status, LocalDate dateFrom, LocalDate dateTo);
}