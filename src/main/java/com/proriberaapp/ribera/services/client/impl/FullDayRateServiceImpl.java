package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRateDto;
import com.proriberaapp.ribera.Domain.entities.FullDayRateEntity;
import com.proriberaapp.ribera.Domain.mapper.FullDayRateMapper;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.FullDayRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FullDayRateServiceImpl implements FullDayRateService {

    private final FullDayRateRepository fullDayRateRepository;

    @Override
    public Mono<Long> countAll() {
        return fullDayRateRepository.countAll();
    }

    @Override
    public Flux<FullDayRateDto> getRatesAll() {
        return fullDayRateRepository.findAllByOrderByCreatedAtDesc()
                .map(FullDayRateMapper::toDto);
    }

    @Override
    public Flux<FullDayRateDto> searchRates(String title, Boolean rateStatus, String rateType) {
        return fullDayRateRepository.searchRates(
                        (title == null || title.isBlank()) ? null : title,
                        rateStatus,
                        (rateType == null || rateType.isBlank()) ? null : rateType
                )
                .map(FullDayRateMapper::toDto);
    }

    @Override
    public Mono<FullDayRateDto> save(FullDayRateDto fullDayRateDto) {
        return fullDayRateRepository
                .save(FullDayRateMapper.toEntity(fullDayRateDto))
                .map(FullDayRateMapper::toDto);
    }

    @Override
    public Mono<FullDayRateDto> update(FullDayRateDto fullDayRateDto, Integer id) {
        return fullDayRateRepository.findById(id)
            .switchIfEmpty(Mono.error(
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarifa no encontrada")
            ))
            .flatMap(existing -> {
                existing.setTitle(fullDayRateDto.getTitle());
                existing.setPrice(fullDayRateDto.getPrice());
                existing.setDescription(fullDayRateDto.getDescription());
                existing.setUserCategory(fullDayRateDto.getUserCategory());
                existing.setRateType(fullDayRateDto.getRateType());
                existing.setRateStatus(fullDayRateDto.getRateStatus());
                return fullDayRateRepository.save(existing);
            })
            .map(FullDayRateMapper::toDto);
    }

    @Override
    public Mono<Boolean> deleteById(Integer id) {
        return fullDayRateRepository.findById(id)
                .flatMap(rate -> fullDayRateRepository.delete(rate).thenReturn(true))
                .defaultIfEmpty(false);
    }
}