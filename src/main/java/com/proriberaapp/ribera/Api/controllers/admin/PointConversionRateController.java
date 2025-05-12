package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.request.PointConversionRateRequest;
import com.proriberaapp.ribera.Api.controllers.admin.dto.request.UpdatePointConversionRateRequest;
import com.proriberaapp.ribera.Domain.dto.FamilyPackageResponseDto;
import com.proriberaapp.ribera.Domain.dto.PointConversionRateDto;
import com.proriberaapp.ribera.Domain.entities.PointConversionRateEntity;
import com.proriberaapp.ribera.services.admin.PointConversionRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/maintenance/convertion-rate")
@RequiredArgsConstructor
@Slf4j
public class PointConversionRateController {

    private final PointConversionRateService pointConversionRateService;

    @GetMapping("/all")
    public Flux<PointConversionRateDto> getAllPointConversionRate() {
        return pointConversionRateService.getAllPointConversionRate();
    }

    @GetMapping("/{id}")
    public Mono<PointConversionRateDto> getPointConversionRateById(@PathVariable Integer id) {
        return pointConversionRateService.getPointConversionRateById(id);
    }

    @GetMapping("/family-packages")
    public Flux<FamilyPackageResponseDto> getDropDownFamilyPackages() {
        return pointConversionRateService.getDropDownFamilyPackages();
    }

    @PostMapping("/save")
    public Mono<PointConversionRateEntity> savePointConversionRate(
            @RequestBody PointConversionRateRequest pointConversionRateRequest) {
        return pointConversionRateService.savePointConversionRate(pointConversionRateRequest);
    }

    @PatchMapping("/update")
    public Mono<PointConversionRateEntity> updatePointConversionRate(
            @RequestBody UpdatePointConversionRateRequest updatePointConversionRateRequest) {
        return pointConversionRateService.updatePointConversionRate(updatePointConversionRateRequest);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deletePointConversionRate(@PathVariable Integer id) {
        return pointConversionRateService.deletePointConversionRate(id);
    }
}
