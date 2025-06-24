package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserRewardEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.nio.channels.FileChannel;

@Repository
public interface UserRewardRepository extends ReactiveCrudRepository<UserRewardEntity, Long> {

    Flux<UserRewardEntity> findByUserId(Long userId);

    Flux<UserRewardEntity> findByUserIdOrderByDateDesc(Long userId);

    Flux<UserRewardEntity> findByUserIdAndStatus(Long userId, String status);

    Flux<UserRewardEntity> findByUserIdAndType(Long userId, String type);

    Flux<UserRewardEntity> findByType(String name);

    Flux<UserRewardEntity> findByStatus(int status);
}
