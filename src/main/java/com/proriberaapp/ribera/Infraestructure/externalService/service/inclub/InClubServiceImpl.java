package com.proriberaapp.ribera.Infraestructure.externalService.service.inclub;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseValidateCredential;
import com.proriberaapp.ribera.Infraestructure.exception.ExternalApiException;
import com.proriberaapp.ribera.Infraestructure.externalService.client.ExternalApiClient;
import com.proriberaapp.ribera.Infraestructure.externalService.dtos.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class InClubServiceImpl implements IInClubService {


//    https://adminpanelapi.inclub.world/api
//    https://gateway.inclub.world/api/v1

    private final ExternalApiClient adminClient;
    private final ExternalApiClient gatewayClient;
    private final ExternalApiClient accountClient;

    @Autowired
    public InClubServiceImpl(
            @Qualifier("adminPanelClientV2") ExternalApiClient adminClient,
            @Qualifier("inClubGatewayClientV2") ExternalApiClient gatewayClient,
            @Qualifier("inClubAccountClientV2") ExternalApiClient accountClient
    ) {
        this.adminClient = adminClient;
        this.gatewayClient = gatewayClient;
        this.accountClient = accountClient;
    }

    public Mono<ResponseValidateCredential> verifiedCredentialsInclub(String username, String password){
        UserDto user = UserDto.builder().username(username).password(password).build();

        return accountClient.postDataContent("account/within/user",user ,new ParameterizedTypeReference<>() {})
    }
    // Ejemplo de uso
//    public Mono<UserDto> findUser(String userId) {
//        return adminClient.getDataContent(
//                "/users/" + userId,
//                new ParameterizedTypeReference<DataResponse<UserDto>>() {}
//        );
//    }
//
//    public Mono<ResultDto> createSomething(CreateReq req) {
//        return gatewayClient.postDataContent(
//                "/something",
//                req,
//                new ParameterizedTypeReference<DataResponse<ResultDto>>() {}
//        );
//    }



}
