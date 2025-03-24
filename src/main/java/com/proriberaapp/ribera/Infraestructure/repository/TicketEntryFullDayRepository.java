package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.TicketEntryFullDayEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TicketEntryFullDayRepository extends R2dbcRepository<TicketEntryFullDayEntity, Integer> {

    @Query("UPDATE ticketentryfullday SET enableddays = :enabledDays")
    Mono<Void> actualizarEnabledDaysParaTodos(String enabledDays);

    @Query("SELECT enableddays FROM ticketentryfullday LIMIT 1")
    Flux<String> obtenerEnabledDays();

    Mono<TicketEntryFullDayEntity> findByTicketEntryFullDayId(Integer ticketEntryFullDayId);

    Mono<TicketEntryFullDayEntity>  findTopBy();
}