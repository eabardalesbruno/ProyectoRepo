package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.TicketEntryFullDayEntity;
import com.proriberaapp.ribera.Infraestructure.repository.TicketEntryFullDayRepository;
import com.proriberaapp.ribera.services.client.TicketEntryFullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                    existingEntry.setCategory(entry.getCategory());
                    existingEntry.setSubCategory(entry.getSubCategory());
                    existingEntry.setType(entry.getType());
                    existingEntry.setStartDate(entry.getStartDate());
                    existingEntry.setEndDate(entry.getEndDate());
                    existingEntry.setAdultPrice(entry.getAdultPrice());
                    existingEntry.setSeniorPrice(entry.getSeniorPrice());
                    existingEntry.setChildPrice(entry.getChildPrice());
                    existingEntry.setInfantPrice(entry.getInfantPrice());
                    return ticketEntryFullDayRepository.save(existingEntry);
                });
    }

    @Override
    public Mono<Void> deleteEntry(Integer id) {
        return ticketEntryFullDayRepository.deleteById(id);
    }
}
