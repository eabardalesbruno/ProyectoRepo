package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.TicketEntryFullDayEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TicketEntryFullDayService {

    Flux<TicketEntryFullDayEntity> getAllEntries();

    Mono<TicketEntryFullDayEntity> getEntryById(Integer id);

    Mono<TicketEntryFullDayEntity> createEntry(TicketEntryFullDayEntity entry);

    Mono<TicketEntryFullDayEntity> updateEntry(Integer id, TicketEntryFullDayEntity entry);

    Mono<Void> deleteEntry(Integer id);

    Mono<Void> updateEnabledDaysForall(String enabledDays);

    Flux<String> getEnabledDaysAll();

    Mono<List<Integer>> getEnabledDays();

}
