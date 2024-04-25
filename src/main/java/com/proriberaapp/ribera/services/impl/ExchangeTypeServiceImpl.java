package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.ExchangeTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ExchangeTypeRepository;
import com.proriberaapp.ribera.services.ExchangeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExchangeTypeServiceImpl implements ExchangeTypeService {
    private final ExchangeTypeRepository exchangeTypeRepository;

    @Autowired
    public ExchangeTypeServiceImpl(ExchangeTypeRepository exchangeTypeRepository) {
        this.exchangeTypeRepository = exchangeTypeRepository;
    }

    @Override
    public Mono<ExchangeTypeEntity> createExchangeType(ExchangeTypeEntity exchangeType) {
        return exchangeTypeRepository.save(exchangeType);
    }

    @Override
    public Mono<ExchangeTypeEntity> getExchangeTypeById(Integer id) {
        return exchangeTypeRepository.findById(id);
    }

    @Override
    public Flux<ExchangeTypeEntity> getAllExchangeTypes() {
        return exchangeTypeRepository.findAll();
    }

    @Override
    public Mono<ExchangeTypeEntity> updateExchangeType(Integer id, ExchangeTypeEntity exchangeType) {
        return exchangeTypeRepository.findById(id)
                .flatMap(existingExchangeType -> {
                    existingExchangeType.setExchangetypedesc(exchangeType.getExchangetypedesc());
                    return exchangeTypeRepository.save(existingExchangeType);
                });
    }

    @Override
    public Mono<Void> deleteExchangeType(Integer id) {
        return exchangeTypeRepository.deleteById(id);
    }
}
