package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.TicketEntryFullDayEntity;
import com.proriberaapp.ribera.services.client.TicketEntryFullDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ticketentryfullday")
@RequiredArgsConstructor
public class TicketEntryFullDayController {

    private final TicketEntryFullDayService ticketEntryFullDayService;

    @GetMapping("/all")
    public Flux<TicketEntryFullDayEntity> getAllEntries() {
        return ticketEntryFullDayService.getAllEntries();
    }


    @GetMapping("/Entryid/{id}")
    public Mono<TicketEntryFullDayEntity> getEntryById(@PathVariable Integer id) {
        return ticketEntryFullDayService.getEntryById(id);
    }


    @PostMapping("/createEntry")
    public Mono<TicketEntryFullDayEntity> createEntry(@RequestBody TicketEntryFullDayEntity entry) {
        return ticketEntryFullDayService.createEntry(entry);
    }

    @PutMapping("/updateEntry/{id}")
    public Mono<TicketEntryFullDayEntity> updateEntry(@PathVariable Integer id, @RequestBody TicketEntryFullDayEntity entry) {
        return ticketEntryFullDayService.updateEntry(id, entry);
    }

    @DeleteMapping("/deleteEntry/{id}")
    public Mono<Void> deleteEntry(@PathVariable Integer id) {
        return ticketEntryFullDayService.deleteEntry(id);
    }

    @PutMapping("/update-enabled-days")
    public Mono<Void> updateEnabledDays(@RequestBody String enabledDays) {
        return ticketEntryFullDayService.updateEnabledDaysForall(enabledDays);
    }

    @GetMapping("/enabled-days")
    public Flux<String> getEnabledDays() {
        return ticketEntryFullDayService.getEnabledDaysAll();
    }
}