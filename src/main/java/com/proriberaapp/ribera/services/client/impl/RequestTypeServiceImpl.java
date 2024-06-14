package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RequestTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RequestTypeRepository;
import com.proriberaapp.ribera.services.client.RequestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestTypeServiceImpl implements RequestTypeService {
    private final RequestTypeRepository requestTypeRepository;

    @Autowired
    public RequestTypeServiceImpl(RequestTypeRepository requestTypeRepository) {
        this.requestTypeRepository = requestTypeRepository;
    }

    @Override
    public Mono<RequestTypeEntity> createRequestType(RequestTypeEntity requestType) {
        return requestTypeRepository.save(requestType);
    }

    @Override
    public Mono<RequestTypeEntity> getRequestTypeById(Integer id) {
        return requestTypeRepository.findById(id);
    }

    @Override
    public Flux<RequestTypeEntity> getAllRequestTypes() {
        return requestTypeRepository.findAll();
    }

    @Override
    public Mono<RequestTypeEntity> updateRequestType(Integer id, RequestTypeEntity requestType) {
        return requestTypeRepository.findById(id)
                .flatMap(existingRequestType -> {
                    existingRequestType.setRequesttypedesc(requestType.getRequesttypedesc());
                    return requestTypeRepository.save(existingRequestType);
                });
    }

    @Override
    public Mono<Void> deleteRequestType(Integer id) {
        return requestTypeRepository.deleteById(id);
    }
}