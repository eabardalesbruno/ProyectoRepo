package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.UserClientVersionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserClientVersionService {
    Mono<UserClientVersionEntity> createUserClientVersion(UserClientVersionEntity userClientVersion);
    Flux<UserClientVersionEntity> getAllUserClientVersions();
    Mono<UserClientVersionEntity> getUserClientVersionById(Integer id);
    // Otros métodos CRUD según sea necesario
}