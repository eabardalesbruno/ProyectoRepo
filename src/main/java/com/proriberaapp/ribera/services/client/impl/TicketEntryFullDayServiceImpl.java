package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.TicketEntryFullDayEntity;
import com.proriberaapp.ribera.Infraestructure.repository.TicketEntryFullDayRepository;
import com.proriberaapp.ribera.services.client.TicketEntryFullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TicketEntryFullDayServiceImpl implements TicketEntryFullDayService {

    private final TicketEntryFullDayRepository ticketEntryFullDayRepository;

    @Override
    public Flux<TicketEntryFullDayEntity> getAllEntries() {
        return ticketEntryFullDayRepository.findAll();
    }

    @Override
    public Mono<TicketEntryFullDayEntity> getEntryById(Integer id) {
        return ticketEntryFullDayRepository.findById(id);
    }

    @Override
    public Mono<TicketEntryFullDayEntity> createEntry(TicketEntryFullDayEntity entry) {
        return ticketEntryFullDayRepository.save(entry);
    }

    @Override
    public Mono<TicketEntryFullDayEntity> updateEntry(Integer id, TicketEntryFullDayEntity entry) {
        return ticketEntryFullDayRepository.findById(id)
                .flatMap(existingEntry -> {
                    existingEntry.setStartDate(entry.getStartDate());
                    existingEntry.setEndDate(entry.getEndDate());
                    existingEntry.setAdultPrice(entry.getAdultPrice());
                    existingEntry.setSeniorPrice(entry.getSeniorPrice());
                    existingEntry.setChildPrice(entry.getChildPrice());
                    existingEntry.setInfantPrice(entry.getInfantPrice());

                    Mono<TicketEntryFullDayEntity> saveEntry = ticketEntryFullDayRepository.save(existingEntry);
                    if (id == 1) {
                        Mono<TicketEntryFullDayEntity> updateEntry5 = ticketEntryFullDayRepository.findById(5)
                                .flatMap(entry5 -> {
                                    entry5.setStartDate(existingEntry.getStartDate());
                                    entry5.setEndDate(existingEntry.getEndDate());
                                    entry5.setAdultPrice(existingEntry.getAdultPrice().multiply(BigDecimal.valueOf(0.5)));
                                    entry5.setSeniorPrice(existingEntry.getSeniorPrice().multiply(BigDecimal.valueOf(0.5)));
                                    entry5.setChildPrice(existingEntry.getChildPrice().multiply(BigDecimal.valueOf(0.5)));
                                    entry5.setInfantPrice(existingEntry.getInfantPrice().multiply(BigDecimal.valueOf(0.5)));
                                    return ticketEntryFullDayRepository.save(entry5);
                                });

                        return saveEntry.then(updateEntry5).then(saveEntry);
                    }
                    return saveEntry;
                });
    }

    @Override
    public Mono<Void> deleteEntry(Integer id) {
        return ticketEntryFullDayRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updateEnabledDaysForall(String enabledDays) {
        return ticketEntryFullDayRepository.actualizarEnabledDaysParaTodos(enabledDays);
    }

    @Override
    public Flux<String> getEnabledDaysAll() {
        return ticketEntryFullDayRepository.obtenerEnabledDays();
    }
}
