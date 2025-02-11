package com.proriberaapp.ribera.Api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointQuotationDayDto;
import com.proriberaapp.ribera.Domain.dto.PointSaveQuotationDto;
import com.proriberaapp.ribera.services.admin.PointQuotationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/point-quotation")
public class PointQuotationController {
    @Autowired
    private PointQuotationService pointQuotationService;

    @GetMapping("/days/{conversionId}")
    public Flux<PointQuotationDayDto> getMethodName(@PathVariable("conversionId") Integer conversionId) {
        return pointQuotationService.getQuotationDaySelected(conversionId);
    }

    @PostMapping
    public Mono<Void> postMethodName(@RequestBody PointSaveQuotationDto entity) {

        return this.pointQuotationService.save(entity);
    }

    @PostMapping("/update")
    public Mono<Void> update(@RequestBody PointSaveQuotationDto entity) {
        return this.pointQuotationService.update(entity);
    }

}
