package com.proriberaapp.ribera.services.admin.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointQuotationDayDto;
import com.proriberaapp.ribera.Domain.dto.PointDaysDto;
import com.proriberaapp.ribera.Domain.dto.PointSaveQuotationDto;
import com.proriberaapp.ribera.Domain.dto.PointTypeDto;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorDayEntity;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorEntity;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorDayEntity.PointTypeConversionFactorDayEntityBuilder;
import com.proriberaapp.ribera.Infraestructure.exception.PointQuotationIsAlreadyRegisteredException;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTypeConversionFactorDayRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTypeConversionFactorRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PointsTypeRepository;
import com.proriberaapp.ribera.services.admin.PointQuotationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PointQuotationServiceImpl implements PointQuotationService {
        @Autowired
        private PointsTypeRepository pointsTypeRepository;

        @Autowired
        private PointsTypeConversionFactorDayRepository pointsTypeConversionFactorDayRepository;

        @Autowired
        private PointsTypeConversionFactorRepository pointsTypeConversionFactorRepository;

        @Override
        public Mono<Void> save(PointSaveQuotationDto pointSaveQuotationDto) {
                List<PointDaysDto> daysSelected = pointSaveQuotationDto.getDays().stream()
                                .filter(day -> day.isSelected())
                                .collect(Collectors.toList());
                Mono<Void> savedPoint = Mono.defer(() -> {
                        PointTypeConversionFactorEntity entity = PointTypeConversionFactorEntity.builder()
                                        .costPerNight(pointSaveQuotationDto.getCostPerNight())
                                        .idpointtype(pointSaveQuotationDto.getPointType().getPointstypeid())
                                        .build();
                        return this.pointsTypeConversionFactorRepository.save(entity)
                                        .flatMap(entitySaved -> {
                                                List<PointTypeConversionFactorDayEntity> days = daysSelected.stream()
                                                                .map(day -> PointTypeConversionFactorDayEntity.builder()
                                                                                .idpointtype(pointSaveQuotationDto
                                                                                                .getPointType()
                                                                                                .getPointstypeid())
                                                                                .idConversionFactor(entitySaved.getId())
                                                                                .idDay(day.getId()).build())
                                                                .collect(Collectors.toList());
                                                return this.pointsTypeConversionFactorDayRepository.saveAll(days)
                                                                .then();
                                        });
                });

                return this.pointsTypeConversionFactorDayRepository
                                .getDaysIgnoreIdFactorConversion(pointSaveQuotationDto.getPointType()
                                                .getPointstypeid())
                                .collectList()
                                .flatMap(list -> {
                                        if (list.size() > 0) {
                                                return Mono.error(new PointQuotationIsAlreadyRegisteredException(
                                                                "La cotización con el tipo seleccionado ya esta registrada"));
                                        }
                                        return savedPoint;
                                });
        }

        @Override
        public Mono<Void> update(PointSaveQuotationDto pointSaveQuotationDto) {
                List<PointDaysDto> daysSelected = pointSaveQuotationDto.getDays().stream()
                                .filter(day -> day.isSelected())
                                .collect(Collectors.toList());

                Mono<Void> savedPoint = Mono.defer(() -> {
                        PointTypeConversionFactorEntity entity = PointTypeConversionFactorEntity.builder()
                                        .costPerNight(pointSaveQuotationDto.getCostPerNight())
                                        .idpointtype(pointSaveQuotationDto.getPointType()
                                                        .getPointstypeid())
                                        .id(pointSaveQuotationDto.getId())
                                        .state(1)
                                        .build();
                        return this.pointsTypeConversionFactorRepository.save(entity)
                                        .flatMap(entitySaved -> {
                                                List<PointTypeConversionFactorDayEntity> days = daysSelected.stream()
                                                                .map(day -> PointTypeConversionFactorDayEntity.builder()
                                                                                .idpointtype(pointSaveQuotationDto
                                                                                                .getPointType()
                                                                                                .getPointstypeid())
                                                                                .idConversionFactor(entitySaved.getId())
                                                                                .idDay(day.getId()).build())
                                                                .collect(Collectors.toList());
                                                return this.pointsTypeConversionFactorDayRepository
                                                                .deleteFindConversionFactoId(
                                                                                entitySaved.getId())
                                                                .then(this.pointsTypeConversionFactorDayRepository
                                                                                .saveAll(days).then());
                                        });
                });
                return this.pointsTypeConversionFactorDayRepository
                                .getDaysIgnoreIdFactorConversion(pointSaveQuotationDto.getPointType()
                                                .getPointstypeid(),
                                                pointSaveQuotationDto.getId(),
                                                daysSelected.stream().map(day -> day.getId())
                                                                .collect(Collectors.toList()))
                                .collectList()
                                .flatMap(list -> {
                                        if (list.size() > 0) {
                                                return Mono.error(new PointQuotationIsAlreadyRegisteredException(
                                                                "La cotización con el tipo seleccionado ya esta registrada"));
                                        }
                                        return savedPoint;
                                });
        }

        @Override
        public Flux<PointQuotationDayDto> getQuotationDaySelected(Integer idConversionFactor) {
                return this.pointsTypeConversionFactorDayRepository.getQuotationDaySelected(idConversionFactor);
        }

        @Override
        public Flux<PointSaveQuotationDto> findAll() {
                return this.pointsTypeConversionFactorRepository.getAllWithPointType()
                                .map(point -> {
                                        PointSaveQuotationDto dto = new PointSaveQuotationDto();
                                        PointTypeDto pointType = PointTypeDto
                                                        .builder()
                                                        .pointstypedesc(point.getPointstypedesc())
                                                        .pointstypeid(point.getPointstypeid())
                                                        .build();
                                        dto.setPointType(pointType);
                                        dto.setCostPerNight(point.getCostPerNight());
                                        dto.setId(point.getId());
                                        return dto;
                                });
        }

}
