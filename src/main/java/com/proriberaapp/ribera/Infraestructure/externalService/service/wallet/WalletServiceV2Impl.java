package com.proriberaapp.ribera.Infraestructure.externalService.service.wallet;

import com.proriberaapp.ribera.Infraestructure.externalService.client.ExternalApiClient;
import com.proriberaapp.ribera.Infraestructure.externalService.dtos.response.DataResponse;
import com.proriberaapp.ribera.Infraestructure.externalService.dtos.response.WalletCreationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WalletServiceV2Impl implements IWalletService {


    private final ExternalApiClient walletClient;

    @Autowired
    public WalletServiceV2Impl(

            @Qualifier("riberaWalletClientV2") ExternalApiClient walletClient) {

        this.walletClient = walletClient;
    }

//    private Mono<String> authenticateBackOffice(String username, String password){
//        return gatewayClient.postDataContent("/auth/login", Map.of("username", username, "password", password),
//                        new ParameterizedTypeReference<DataResponse<AuthDataResponse>>() {})
//                .map(AuthDataResponse::getAccess_token)
//                .flatMap(Mono::just);
//    }

    private Mono<WalletCreationResponse> createWallet(String token) {

        String uri = "wallet/create-complete";
        return walletClient.postDataContent(uri, new ParameterizedTypeReference<DataResponse<WalletCreationResponse>>() {
        }, token);
    }

}
