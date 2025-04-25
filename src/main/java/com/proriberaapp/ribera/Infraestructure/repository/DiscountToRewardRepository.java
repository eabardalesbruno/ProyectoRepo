package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.DiscountToRewardEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface DiscountToRewardRepository extends R2dbcRepository<DiscountToRewardEntity, Integer> {

    @Query(value = """
            SELECT *
            FROM discount_to_reward dtr
            WHERE dtr."name" ILIKE :name;
            """)
    Mono<DiscountToRewardEntity> findByName(String name);
}
