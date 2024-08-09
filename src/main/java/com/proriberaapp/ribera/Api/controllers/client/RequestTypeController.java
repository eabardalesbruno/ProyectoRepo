package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RequestTypeEntity;
import com.proriberaapp.ribera.services.client.RequestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/request-types")
public class RequestTypeController {
    private final RequestTypeService requestTypeService;

    @Autowired
    public RequestTypeController(RequestTypeService requestTypeService) {
        this.requestTypeService = requestTypeService;
    }

    @PostMapping
    public Mono<ResponseEntity<RequestTypeEntity>> createRequestType(@RequestBody RequestTypeEntity requestType) {
        return requestTypeService.createRequestType(requestType)
                .map(savedRequestType -> ResponseEntity.status(HttpStatus.CREATED).body(savedRequestType));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RequestTypeEntity>> getRequestTypeById(@PathVariable Integer id) {
        return requestTypeService.getRequestTypeById(id)
                .map(requestType -> ResponseEntity.ok(requestType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<RequestTypeEntity> getAllRequestTypes() {
        return requestTypeService.getAllRequestTypes();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<RequestTypeEntity>> updateRequestType(@PathVariable Integer id, @RequestBody RequestTypeEntity requestType) {
        return requestTypeService.updateRequestType(id, requestType)
                .map(updatedRequestType -> ResponseEntity.ok(updatedRequestType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRequestType(@PathVariable Integer id) {
        return requestTypeService.deleteRequestType(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}