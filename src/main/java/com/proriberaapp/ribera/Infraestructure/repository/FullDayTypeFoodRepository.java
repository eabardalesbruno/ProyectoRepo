package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FullDayTypeFoodRepository extends R2dbcRepository<FullDayTypeFoodEntity,Integer> {

    @Query("SELECT type FROM fulldaytypefood WHERE fulldaytypefoodid = :fulldayTypefoodid")
    Mono<FullDayTypeFoodEntity> findCategoryByFoodId(@Param("fulldayTypefoodid") Integer fulldayTypefoodid);

}
