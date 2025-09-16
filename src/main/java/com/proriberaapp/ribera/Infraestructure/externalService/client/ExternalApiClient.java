package com.proriberaapp.ribera.Infraestructure.externalService.client;

import com.proriberaapp.ribera.Infraestructure.exception.ExternalApiException;
import com.proriberaapp.ribera.Infraestructure.externalService.dtos.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class ExternalApiClient {

    private final WebClient webClient;

    public ExternalApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> getDataContent(String uri, ParameterizedTypeReference<DataResponse<T>> responseType) {

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(), r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                                .flatMap(body -> {
                                    log.error("External API error. Status={}, Body={}", r.statusCode(), body);
                                    return Mono.error(new ExternalApiException("External API error: " + body));
                                })
                )
                .bodyToMono(responseType)
                .map(DataResponse::getData);
    }

    public <T, R> Mono<T> postDataContent(String uri, R body, ParameterizedTypeReference<DataResponse<T>> responseType) {

        return webClient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(), r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                                .flatMap(resp -> {
                                    log.error("External API error. Status={}, Body={}", r.statusCode(), resp);
                                    return Mono.error(new ExternalApiException("External API error: " + resp));
                                })
                )
                .bodyToMono(responseType)
                .map(DataResponse::getData);
    }


    public <T, R> Mono<T> postDataContent(String uri, ParameterizedTypeReference<DataResponse<T>> responseType,String token) {

        return webClient.post()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(), r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                                .flatMap(resp -> {
                                    log.error("External API error. Status={}, Body={}", r.statusCode(), resp);
                                    return Mono.error(new ExternalApiException("External API error: " + resp));
                                })
                )
                .bodyToMono(responseType)
                .map(DataResponse::getData);
    }


}
