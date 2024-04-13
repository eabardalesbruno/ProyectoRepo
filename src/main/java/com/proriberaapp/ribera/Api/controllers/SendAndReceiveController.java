package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.SendAndReceiveEntity;
import com.proriberaapp.ribera.services.SendAndReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/send-and-receive")
public class SendAndReceiveController {
    private final SendAndReceiveService sendAndReceiveService;

    @Autowired
    public SendAndReceiveController(SendAndReceiveService sendAndReceiveService) {
        this.sendAndReceiveService = sendAndReceiveService;
    }

    @PostMapping
    public Mono<ResponseEntity<SendAndReceiveEntity>> createSendAndReceive(@RequestBody SendAndReceiveEntity sendAndReceive) {
        return sendAndReceiveService.createSendAndReceive(sendAndReceive)
                .map(savedSendAndReceive -> ResponseEntity.status(HttpStatus.CREATED).body(savedSendAndReceive));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<SendAndReceiveEntity>> getSendAndReceiveById(@PathVariable Integer id) {
        return sendAndReceiveService.getSendAndReceiveById(id)
                .map(sendAndReceive -> ResponseEntity.ok(sendAndReceive))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<SendAndReceiveEntity> getAllSendAndReceives() {
        return sendAndReceiveService.getAllSendAndReceives();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<SendAndReceiveEntity>> updateSendAndReceive(@PathVariable Integer id, @RequestBody SendAndReceiveEntity sendAndReceive) {
        return sendAndReceiveService.updateSendAndReceive(id, sendAndReceive)
                .map(updatedSendAndReceive -> ResponseEntity.ok(updatedSendAndReceive))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSendAndReceive(@PathVariable Integer id) {
        return sendAndReceiveService.deleteSendAndReceive(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}