package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FullDayTypeFoodRepository extends R2dbcRepository<FullDayTypeFoodEntity,Integer> {

    @Query("SELECT * FROM fulldaytypefood WHERE (:type IS NULL OR type = :type) " +
            "AND (:name IS NULL OR LOWER(foodname) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "LIMIT :limit OFFSET :offset")
    Flux<FullDayTypeFoodEntity> findByTypePaged(String type,String name, int limit, int offset);

    @Query("SELECT COUNT(*) FROM fulldaytypefood WHERE (:type IS NULL OR type = :type) " +
            "AND (:name IS NULL OR LOWER(foodname) LIKE LOWER(CONCAT('%', :name, '%')))")
    Mono<Integer> countByType(String type, String name);



    Mono<FullDayTypeFoodEntity> findByFullDayTypeFoodId(Integer fullDayTypeFoodId);

}
