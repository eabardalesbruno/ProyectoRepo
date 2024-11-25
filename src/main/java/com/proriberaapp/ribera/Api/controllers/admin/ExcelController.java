package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.services.admin.impl.ExcelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelServiceImpl excelServiceImpl;

    @GetMapping("/download")
    public Mono<ResponseEntity<ByteArrayResource>> downloadExcel() {
        return excelServiceImpl.generateExcelWithMonthlyData()
                .map(resource -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=template.xlsm")
                        .header("Content-Type", "application/vnd.ms-excel.sheet.macroEnabled.12")
                        .body(resource));
    }
}
