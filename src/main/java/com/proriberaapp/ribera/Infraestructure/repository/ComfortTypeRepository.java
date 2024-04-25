package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ComfortTypeRepository extends R2dbcRepository<ComfortTypeEntity, Integer> {
    Mono<ComfortTypeEntity> findByComfortTypeName(ComfortTypeEntity entity);

    Flux<ComfortTypeEntity> findAllByComfortTypeNameIn(List<ComfortTypeEntity> entity);

    @Query("SELECT * FROM viewcomfortdata WHERE bookingId = :bookingId")
    Flux<ViewBookingReturn.ComfortData> findAllByViewComfortType(@Param("bookingId") Integer bookingId);
}
