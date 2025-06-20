package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserRewardTransferHistoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRewardTransferHistoryRepository extends R2dbcRepository<UserRewardTransferHistoryEntity , Integer> {

    Flux<UserRewardTransferHistoryEntity> findAll();
}