package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ViewServiceReturn;
import com.proriberaapp.ribera.Domain.entities.ServicesEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ServicesRepository extends R2dbcRepository<ServicesEntity, Integer> {
    @Query("SELECT * FROM ViewServiceReturn")
    Flux<ViewServiceReturn> findAllViewServiceReturn();
    @Query("SELECT * FROM ViewComfortReturn WHERE roomofferid = :roomofferid")
    Flux<ViewServiceReturn.ListAmenities> findAllViewComfortReturn(@Param("roomofferid") Integer roomofferid);
    @Query("SELECT * FROM ViewServiceComfortReturn WHERE roomofferid = :roomofferid")
    Flux<ViewServiceReturn.ListAmenities> findAllViewServiceComfortReturn(@Param("roomofferid") Integer roomofferid);

}
