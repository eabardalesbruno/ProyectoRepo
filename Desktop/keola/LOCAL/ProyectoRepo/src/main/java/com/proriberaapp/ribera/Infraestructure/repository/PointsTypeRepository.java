package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PointsTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PointsTypeRepository extends ReactiveCrudRepository<PointsTypeEntity, Integer> {
    @Query("SELECT * FROM pointstype WHERE pointstypedesc LIKE CONCAT('%', :keyword, '%')")
    Flux<PointsTypeEntity> findByPointstypedescContaining(String keyword);

    @Query("SELECT * FROM pointstype WHERE pointstypedesc = :name ")
    Mono<PointsTypeEntity> findByName(String name);

}
