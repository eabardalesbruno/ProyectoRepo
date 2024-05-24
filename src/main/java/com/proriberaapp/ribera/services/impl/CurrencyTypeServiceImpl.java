package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.CurrencyTypeRepository;
import com.proriberaapp.ribera.services.CurrencyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyTypeServiceImpl implements CurrencyTypeService {
    private final CurrencyTypeRepository currencyTypeRepository;

    @Autowired
    public CurrencyTypeServiceImpl(CurrencyTypeRepository currencyTypeRepository) {
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @Override
    public Mono<CurrencyTypeEntity> createCurrencyType(CurrencyTypeEntity currencyType) {
        return currencyTypeRepository.save(currencyType);
    }

    @Override
    public Mono<CurrencyTypeEntity> getCurrencyType(Integer currencyTypeId) {
        return currencyTypeRepository.findById(currencyTypeId);
    }

    @Override
    public Flux<CurrencyTypeEntity> getAllCurrencyTypes() {
        return currencyTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteCurrencyType(Integer currencyTypeId) {
        return currencyTypeRepository.deleteById(currencyTypeId);
    }
}
