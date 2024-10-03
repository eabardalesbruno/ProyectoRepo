package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface BedsTypeRepository extends R2dbcRepository<BedsTypeEntity, Integer> {
    @Query("SELECT * FROM viewbedstype WHERE bookingId = :bookingId")
    Flux<ViewBookingReturn.BedsType> findAllByViewBedsType(@Param("bookingId") Integer bookingId);

    Flux<BedsTypeEntity> findAllByOrderByBedTypeIdAsc();
}
