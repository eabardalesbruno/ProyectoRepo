package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.request.PointConversionRateRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.request.UpdatePointConversionRateRequest;
import com.proriberaapp.ribera.Domain.dto.FamilyPackageResponseDto;
import com.proriberaapp.ribera.Domain.dto.PointConversionRateDto;
import com.proriberaapp.ribera.Domain.entities.PointConversionRateEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PointConversionRateService {

    Flux<PointConversionRateDto> getAllPointConversionRate();

    Mono<PointConversionRateDto> getPointConversionRateById(Integer id);

    Flux<FamilyPackageResponseDto> getDropDownFamilyPackages();

    Mono<PointConversionRateEntity> savePointConversionRate(PointConversionRateRequest pointConversionRateRequest);

    Mono<PointConversionRateEntity> updatePointConversionRate(
            UpdatePointConversionRateRequest updatePointConversionRateRequest);
}