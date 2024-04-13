package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.RequestTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RequestTypeService {
    Mono<RequestTypeEntity> createRequestType(RequestTypeEntity requestType);
    Mono<RequestTypeEntity> getRequestTypeById(Integer id);
    Flux<RequestTypeEntity> getAllRequestTypes();
    Mono<RequestTypeEntity> updateRequestType(Integer id, RequestTypeEntity requestType);
    Mono<Void> deleteRequestType(Integer id);
}