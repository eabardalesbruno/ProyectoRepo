package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.request.RequestReleaseLogDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response.ApiResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response.RewardReleaseLogResponseDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.UserDto;
import com.proriberaapp.ribera.services.admin.BoRewardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoRewardsServiceImpl implements BoRewardsService {

    @Value("${inclub.api.url.user}")
    private String URL_DATA_USER;

    @Value("${inclub.api.url.bo.rewards}")
    private String URL_MS_BO_REWARDS;

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<RewardReleaseLogResponseDto> createReleaseLog(RequestReleaseLogDto request) {
        String username = request.getUsername();
        log.info("Iniciando creación de release log para username: {}", username);
        return loadUserDataFromInclub(username)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No se encontraron datos para el username: {}", username);
                    return Mono.error(new IllegalArgumentException("El usuario con username '" + username + "' no existe o no se pudieron cargar sus datos."));
                }))
                .flatMap(responseInclubLogin -> {
                    if (!responseInclubLogin.isResult() || responseInclubLogin.getData() == null) {
                        log.error("La respuesta de carga de usuario indica fallo para username {}: Result={}, Data={}",
                                username, responseInclubLogin.isResult(), responseInclubLogin.getData());
                        return Mono.error(new RuntimeException("Fallo al obtener datos del usuario '" + username + "': " +
                                (responseInclubLogin.getData() == null ? "Datos nulos" : "result:false")));
                    }

                    UserDto userData = responseInclubLogin.getData();
                    Integer userId = userData.getId();
                    log.info("Username '{}' mapeado a userId: {}", username, userId);

                    request.setUserId(userId);

                    return callRewardsService(request);
                })
                .doOnError(e -> log.error("Error en BoRewardsService al procesar la solicitud de recompensas para username {}: {}", username, e.getMessage(), e));
    }

    /**
     * Llama al servicio externo de datos de usuario (Inclub Login) para obtener
     * la información del usuario por su username.
     *
     * @param username El nombre de usuario.
     * @return Mono<ResponseInclubLoginDto> con los datos del usuario, o Mono.empty() si falla/no se encuentra.
     */
    private Mono<ResponseInclubLoginDto> loadUserDataFromInclub(String username) {
        WebClient webClient = webClientBuilder.baseUrl(URL_DATA_USER).build();
        String uri = "/" + username;

        log.info("Llamando al servicio de datos de usuario en URL: {}{}", URL_DATA_USER, uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ResponseInclubLoginDto.class)
                .onErrorResume(e -> {
                    log.error("Error al cargar datos del usuario '{}' desde {}: {}", username, URL_DATA_USER + uri, e.getMessage(), e);
                    return Mono.empty();
                });
    }

    /**
     * Llama al servicio externo de recompensas para crear un log de liberación.
     *
     * @param request El RequestReleaseLogDto con el userId ya resuelto.
     * @return Mono<RewardReleaseLogResponseDto> si la operación es exitosa.
     */
    private Mono<RewardReleaseLogResponseDto> callRewardsService(RequestReleaseLogDto request) {
        String fullRewardsUrl = URL_MS_BO_REWARDS + "rewards-release-log";
        log.info("Llamando al servicio de recompensas a la URL: {} para userId: {}", fullRewardsUrl, request.getUserId());

        return webClientBuilder.build().post()
                .uri(fullRewardsUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()

                // --- ¡EL CAMBIO CLAVE ESTÁ AQUÍ! ---
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<RewardReleaseLogResponseDto>>() {})
                .flatMap(apiResponse -> {
                    if (!apiResponse.isResult()) {
                        log.error("Servicio de recompensas respondió con result:false para userId {}. Datos: {}", request.getUserId(), apiResponse.getData());
                        return Mono.error(new RuntimeException("Operación de recompensa fallida: " + apiResponse.getData()));
                    }
                    log.info("Operación de recompensa exitosa para userId {}. Datos: {}", request.getUserId(), apiResponse.getData());
                    return Mono.just(apiResponse.getData());
                })
                .doOnError(error -> log.error("Error general al llamar a servicio de recompensas: {}", error.getMessage(), error));
    }
}
