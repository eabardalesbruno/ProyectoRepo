package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.request.PointConversionRateRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.request.UpdatePointConversionRateRequest;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
import com.proriberaapp.ribera.Domain.dto.FamilyPackageResponseDto;
import com.proriberaapp.ribera.Domain.dto.PointConversionRateDto;
import com.proriberaapp.ribera.Domain.dto.RetrieveFamilyPackageResponseDto;
import com.proriberaapp.ribera.Domain.entities.PointConversionRateEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointConversionRateRepository;
import com.proriberaapp.ribera.services.admin.PointConversionRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointConversionRateServiceImpl implements PointConversionRateService {

    @Value("${inclub.api.url.admin}")
    private String URL_FAMILY_PACKAGE_BASE;

    private final WebClient webClient;

    private final PointConversionRateRepository pointConversionRateRepository;

    @Override
    public Flux<PointConversionRateDto> getAllPointConversionRate() {
        log.info("Start the method getAllPointConversionRate");
        return fetchAllPointConversionRates()
                .collectList()
                .flatMapMany(this::familyNames)
                .doOnError(e -> log.error(e.getMessage(), e))
                .doOnComplete(() -> log.info("End the method getAllPointConversionRate"));
    }

    private Flux<PointConversionRateDto> fetchAllPointConversionRates() {
        return pointConversionRateRepository.findAll()
                .map(this::mapEntityToDtoWithoutFamilyName);
    }

    private Mono<RetrieveFamilyPackageResponseDto> fetchFamilyPackages() {
        return webClient.get()
                .uri(URL_FAMILY_PACKAGE_BASE + "/familypackage/package/detail/version/state/true")
                .retrieve()
                .bodyToMono(RetrieveFamilyPackageResponseDto.class);
    }

    private Flux<PointConversionRateDto> familyNames(List<PointConversionRateDto> points) {
        return fetchFamilyPackages()
                .flatMapMany(response -> {
                    if (!response.isResult()) {
                        log.info("Family package API responded with result=false, returning points without family names.");
                        return Flux.fromIterable(points);
                    }

                    Map<Integer, String> familyNameMap = response.getData().stream()
                            .collect(Collectors.toMap(
                                    FamilyPackageResponseDto::getIdFamilyPackage,
                                    FamilyPackageResponseDto::getName
                            ));

                    List<PointConversionRateDto> updatedPoints = points.stream()
                            .map(p -> mapDtoWithFamilyName(p, familyNameMap))
                            .toList();

                    return Flux.fromIterable(updatedPoints);
                });
    }

    private PointConversionRateDto mapEntityToDtoWithoutFamilyName(PointConversionRateEntity entity) {
        return PointConversionRateDto.builder()
                .id(entity.getId())
                .idFamily(entity.getFamilyId())
                .familyName(null) // sin nombre todavía
                .conversionRate(entity.getConversionRate())
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .status(entity.getStatus())
                .build();
    }

    private PointConversionRateDto mapDtoWithFamilyName(PointConversionRateDto dto, Map<Integer, String> familyNameMap) {
        return PointConversionRateDto.builder()
                .id(dto.getId())
                .idFamily(dto.getIdFamily())
                .familyName(familyNameMap.get(dto.getIdFamily()))
                .conversionRate(dto.getConversionRate())
                .createdAt(dto.getCreatedAt())
                .status(dto.getStatus())
                .build();
    }

    @Override
    public Mono<PointConversionRateDto> getPointConversionRateById(Integer id) {
        return pointConversionRateRepository.findById(id)
                .map(this::mapEntityToDtoWithoutFamilyName)
                .flatMap(this::familyNameForSinglePoint);
    }

    private Mono<PointConversionRateDto> familyNameForSinglePoint(PointConversionRateDto point) {
        return fetchFamilyPackages()
                .map(response -> {
                    if (!response.isResult()) {
                        log.warn("Family package API responded with result=false. Returning point without family name.");
                        return point;
                    }

                    Map<Integer, String> familyNameMap = response.getData().stream()
                            .collect(Collectors.toMap(
                                    FamilyPackageResponseDto::getIdFamilyPackage,
                                    FamilyPackageResponseDto::getName
                            ));

                    return PointConversionRateDto.builder()
                            .id(point.getId())
                            .idFamily(point.getIdFamily())
                            .familyName(familyNameMap.get(point.getIdFamily()))
                            .conversionRate(point.getConversionRate())
                            .createdAt(point.getCreatedAt())
                            .status(point.getStatus())
                            .build();
                });
    }

    @Override
    public Flux<FamilyPackageResponseDto> getDropDownFamilyPackages() {
        return fetchFamilyPackages()
                .flatMapMany(response -> {
                    if (!response.isResult()) {
                        log.info("Family package API responded with result=false. Returning empty list.");
                        return Flux.empty();
                    }
                    return Flux.fromIterable(response.getData());
                });
    }

    @Override
    public Mono<PointConversionRateEntity> savePointConversionRate(
            PointConversionRateRequest pointConversionRateRequest) {
        return pointConversionRateRepository.existsByFamilyId(pointConversionRateRequest.getFamilyId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RequestException("Ya existe un registro para ese Family ID: "
                                + pointConversionRateRequest.getFamilyId()));
                    }
                    var pointConversionRateEntity = mapRequestToEntity(pointConversionRateRequest);
                    return pointConversionRateRepository.save(pointConversionRateEntity);
                });
    }

    private PointConversionRateEntity mapRequestToEntity(PointConversionRateRequest request) {
        return PointConversionRateEntity.builder()
                .familyId(request.getFamilyId())
                .conversionRate(request.getConversionRate())
                .createdAt(LocalDateTime.now())
                .status(request.getStatus())
                .build();
    }

    @Override
    public Mono<PointConversionRateEntity> updatePointConversionRate(
            UpdatePointConversionRateRequest updatePointConversionRateRequest) {
        log.info("Start the method updatePointConversionRate");
        return pointConversionRateRepository.findById(updatePointConversionRateRequest.getIdPointConversionRate())
                .switchIfEmpty(Mono.error(new RequestException("No se encontró el registro con ID: " +
                        updatePointConversionRateRequest.getIdPointConversionRate())))
                .flatMap(existingEntity -> {
                    Optional.ofNullable(updatePointConversionRateRequest.getConversionRate())
                            .ifPresent(existingEntity::setConversionRate);

                    Optional.ofNullable(updatePointConversionRateRequest.getStatus())
                            .ifPresent(existingEntity::setStatus);

                    existingEntity.setUpdatedAt(LocalDateTime.now());

                    return pointConversionRateRepository.save(existingEntity);
                })
                .doOnError(e -> log.error(e.getMessage(), e))
                .doOnSuccess(p -> log.info("End the method updatePointConversionRate"));
    }
}
