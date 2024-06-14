package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.UserClientVersionEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientVersionRepository;
import com.proriberaapp.ribera.services.UserClientVersionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClientVersionServiceImpl implements UserClientVersionService {
    private final UserClientVersionRepository userClientVersionRepository;

    public UserClientVersionServiceImpl(UserClientVersionRepository userClientVersionRepository) {
        this.userClientVersionRepository = userClientVersionRepository;
    }

    @Override
    public Mono<UserClientVersionEntity> createUserClientVersion(UserClientVersionEntity userClientVersion) {
        return userClientVersionRepository.save(userClientVersion);
    }

    @Override
    public Flux<UserClientVersionEntity> getAllUserClientVersions() {
        return userClientVersionRepository.findAll();
    }

    @Override
    public Mono<UserClientVersionEntity> getUserClientVersionById(Integer id) {
        return userClientVersionRepository.findById(id);
    }
}
