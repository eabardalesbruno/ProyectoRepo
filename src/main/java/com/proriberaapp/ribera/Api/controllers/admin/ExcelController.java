package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.dto.ReportCommissionDto;
import com.proriberaapp.ribera.services.admin.ReportManagerService;
import com.proriberaapp.ribera.services.admin.impl.ExcelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelServiceImpl excelServiceImpl;
    private final ReportManagerService reportManagerService;

    @GetMapping("/download")
    public Mono<ResponseEntity<ByteArrayResource>> downloadExcel(
        @RequestParam Integer stateId, 
        @RequestParam Integer month,
        @RequestParam Integer year
    ) { 
        return reportManagerService.generateBookingReport(stateId, month, year)

                .collectList()
                .flatMap(excelServiceImpl::generateExcelFromEntitiesByMonth)
                .map(resource -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=template.xlsm")
                        .header("Content-Type", "application/vnd.ms-excel.sheet.macroEnabled.12")
                        .body(resource))
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.internalServerError()
                            .header("Content-Type", "application/json")
                            .body(new ByteArrayResource(
                                    ("Error al generar el archivo Excel: " + e.getMessage()).getBytes())));
                });
    }

    @GetMapping("/downloadComisionAll")
    public Mono<ResponseEntity<byte[]>> generateExcel() {
        return excelServiceImpl.generateExcel()
                .map(data -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bookings.xlsx")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data));
    }

}
