package com.proriberaapp.ribera.Api.controllers.client;
import com.proriberaapp.ribera.Domain.entities.EntertainmentEntity;
import com.proriberaapp.ribera.services.client.EntertainmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/entertainment")
public class EntertainmentController {
    private final EntertainmentService entertainmentService;

    @Autowired
    public EntertainmentController(EntertainmentService entertainmentService) {
        this.entertainmentService = entertainmentService;
    }

    @PostMapping
    public Mono<ResponseEntity<EntertainmentEntity>> createEntertainment(@RequestBody EntertainmentEntity entertainment) {
        return entertainmentService.createEntertainment(entertainment)
                .map(createdEntertainment -> new ResponseEntity<>(createdEntertainment, HttpStatus.CREATED));
    }

    @GetMapping("/{entertainmentId}")
    public Mono<ResponseEntity<EntertainmentEntity>> getEntertainment(@PathVariable Integer entertainmentId) {
        return entertainmentService.getEntertainment(entertainmentId)
                .map(entertainment -> ResponseEntity.ok(entertainment))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<EntertainmentEntity> getAllEntertainments() {
        return entertainmentService.getAllEntertainments();
    }

    @DeleteMapping("/{entertainmentId}")
    public Mono<ResponseEntity<Void>> deleteEntertainment(@PathVariable Integer entertainmentId) {
        return entertainmentService.deleteEntertainment(entertainmentId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}