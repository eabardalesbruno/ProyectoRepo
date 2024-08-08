package com.proriberaapp.ribera.Api.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;
import com.proriberaapp.ribera.services.client.ComplaintsBookService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/complaints")
public class ComplaintsBookController {

    private final ComplaintsBookService complaintsBookService;

    @Autowired
    public ComplaintsBookController(ComplaintsBookService complaintsBookService) {
        this.complaintsBookService = complaintsBookService;
    }

    @PostMapping
    public Mono<ResponseEntity<ComplaintsBookEntity>> createComplaint(@RequestBody ComplaintsBookEntity complaint) {
        if (complaint.getPersonType() == null || complaint.getPersonType().isEmpty()) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        }

        return complaintsBookService.createComplaint(complaint)
                .map(savedComplaint -> ResponseEntity.status(HttpStatus.CREATED).body(savedComplaint))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}