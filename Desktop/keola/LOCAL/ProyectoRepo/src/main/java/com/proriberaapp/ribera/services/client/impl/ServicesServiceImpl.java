package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewServiceReturn;
import com.proriberaapp.ribera.Domain.entities.ServicesEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ServicesRepository;
import com.proriberaapp.ribera.services.client.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ServicesServiceImpl implements ServicesService {
    private final ServicesRepository servicesRepository;

    @Autowired
    public ServicesServiceImpl(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Override
    public Mono<ServicesEntity> createService(ServicesEntity service) {
        return servicesRepository.save(service);
    }

    @Override
    public Mono<ServicesEntity> getServiceById(Integer id) {
        return servicesRepository.findById(id);
    }

    @Override
    public Flux<ServicesEntity> getAllServices() {
        return servicesRepository.findAll();
    }

    @Override
    public Mono<ServicesEntity> updateService(Integer id, ServicesEntity service) {
        return servicesRepository.findById(id)
                .flatMap(existingService -> {
                    existingService.setServicedesc(service.getServicedesc());
                    return servicesRepository.save(existingService);
                });
    }

    @Override
    public Mono<Void> deleteService(Integer id) {
        return servicesRepository.deleteById(id);
    }

    @Override
    public Flux<ViewServiceReturn> findAllViewServiceReturn() {
        BigDecimal porcentaje = new BigDecimal("1.10"); // 10% expresado como 1 más el porcentaje

        return servicesRepository.findAllViewServiceReturn()
                .concatMap(service -> servicesRepository.findAllViewComfortReturn(service.getRoomofferid())
                        .collectList()
                        .map(comfort -> {
                            service.setListAmenities(comfort);
                            return service;
                        })
                )
                .concatMap(service -> servicesRepository.findAllViewServiceComfortReturn(service.getRoomofferid())
                        .collectList()
                        .map(comfort -> {
                            service.setListService(comfort);
                            return service;
                        })
                );
    }

}
