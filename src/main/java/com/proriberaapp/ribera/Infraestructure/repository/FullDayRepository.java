package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FullDayRepository extends R2dbcRepository<FullDayEntity,Integer> {

    @Query("SELECT * FROM fullday WHERE receptionistid = :id")
    Flux<FullDayEntity> findByReceptionistId(@Param("id") Integer associatedId);

    @Query("SELECT * FROM fullday WHERE userpromoterid = :id")
    Flux<FullDayEntity> findByUserPromoterId(@Param("id") Integer associatedId);

    @Query("SELECT * FROM fullday WHERE userclientid = :id")
    Flux<FullDayEntity> findByUserClientId(@Param("id") Integer associatedId);
}
