package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.TicketEntryFullDayEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketEntryFullDayRepository extends R2dbcRepository<TicketEntryFullDayEntity, Integer> {

}
