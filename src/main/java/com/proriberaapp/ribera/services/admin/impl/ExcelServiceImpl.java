package com.proriberaapp.ribera.services.admin.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.ExcelEntity;
import com.proriberaapp.ribera.services.admin.ReportManagerService;

import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl {

    private final ReportManagerService reportManagerService;

    public ExcelServiceImpl(ReportManagerService reportManagerService) {
        this.reportManagerService = reportManagerService;
    }

    public Mono<ByteArrayResource> generateExcelFromEntitiesByMonth(List<ExcelEntity> entities) {
        try (InputStream is = new ClassPathResource("template.xlsm").getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Map<String, List<ExcelEntity>> dataByMonth = entities.stream()
                    .collect(Collectors.groupingBy(entity -> {
                        int month = entity.getCreatedAt().getMonthValue();
                        return getMonthName(month);
                    }));

            for (Map.Entry<String, List<ExcelEntity>> entry : dataByMonth.entrySet()) {
                String sheetName = entry.getKey();
                List<ExcelEntity> monthlyData = entry.getValue();

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    System.out.println("Hoja " + sheetName + " no encontrada en la plantilla. Se omite.");
                    continue;
                }

                int startRow = 1;
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


                for (ExcelEntity entity : monthlyData) {
                    Row row = sheet.createRow(startRow++);

                    String currency = switch (entity.getIdCurrency()) {
                        case 1 -> "Soles";
                        case 2 -> "Dolares";
                        default -> "Moneda desconocida";
                    };

                    String formattedDate = entity.getCreatedAt().toLocalDate().format(dateFormatter);

                    Map<String, Object> columnMapping = Map.of(
                            "SerieDoc", entity.getSerie(),
                            "Ruc_Prov", entity.getIdentifierClient().toString(),
                            "Fecha", formattedDate,
                            "Moneda", currency,
                            "ValorTC", entity.getTc(),
                            "Igv", entity.getTotalIgv(),
                            "Total", entity.getTotalPayment());

                    Row headerRow = sheet.getRow(0);
                    if (headerRow == null) {
                        throw new RuntimeException("La fila de cabeceras está vacía o no existe.");
                    }

                    for (int cellIndex = 0; cellIndex < headerRow.getLastCellNum(); cellIndex++) {
                        Cell headerCell = headerRow.getCell(cellIndex);
                        if (headerCell == null)
                            continue;

                        String headerName = headerCell.getStringCellValue();
                        Object value = columnMapping.get(headerName);

                        Cell cell = row.createCell(cellIndex);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            System.out.println("Archivo Excel generado correctamente con datos organizados por mes.");
            return Mono.just(resource);

        } catch (IOException e) {
            return Mono.error(new RuntimeException("Error al generar el archivo Excel", e));
        }
    }

    private String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                throw new IllegalArgumentException("Mes inválido: " + month);
        }
    }

    public Mono<ByteArrayResource> generateExcelWithMonthlyData() {

        Map<String, List<Map<String, Object>>> monthlyData = new HashMap<>();

        Map<String, Object> eneroData1 = new HashMap<>();
        eneroData1.put("CodDoc", "FCC");
        eneroData1.put("SerieDoc", "F001");
        eneroData1.put("NroDocDel", "0007548");
        eneroData1.put("NroDocAl", "0007549");
        eneroData1.put("Cond_Pago", "CONTADO");
        eneroData1.put("Ruc_Prov", "20123456789");
        eneroData1.put("Fecha", "2024-01-01");
        eneroData1.put("Fec_Vcmto", "2024-01-30");
        eneroData1.put("Moneda", "M0001");
        eneroData1.put("ValorTC", "3.75");
        eneroData1.put("Tipo_Ope", "2");
        eneroData1.put("Neto", "2000.00");
        eneroData1.put("Isc", "0.00");
        eneroData1.put("Igv", "360");
        eneroData1.put("Percepcion", "0.00");
        eneroData1.put("Inafecto", "0.00");
        eneroData1.put("Exonerado", "0.00");
        eneroData1.put("Otros_Trib", "0.00");
        eneroData1.put("ICBP", "0.00");
        eneroData1.put("Total", "2360.00");
        eneroData1.put("CodCuenta", "601002");
        eneroData1.put("Glosa", "Compra de servicios");
        eneroData1.put("CodDep_Adu", "003");
        eneroData1.put("Fecha_Detra", "2024-02-15");
        eneroData1.put("Cons_Detra", "15.00");
        eneroData1.put("Total_Detra", "15.00");
        eneroData1.put("Cons_Reten", "7.50");
        eneroData1.put("Total_Reten", "7.50");
        eneroData1.put("CodDocRef", "BCC");
        eneroData1.put("SerieDocRef", "B002");
        eneroData1.put("NumDocRef", "0007500");
        eneroData1.put("D_Bie_Servic", "S");
        eneroData1.put("Contrato_Proy", "C002");
        eneroData1.put("TC_Susten_CF", "3.80");
        eneroData1.put("Serie_CF", "CF002");
        eneroData1.put("Numero_CF", "0002");
        eneroData1.put("Convenio", "SI");
        eneroData1.put("Exoneracion", "0.00");
        eneroData1.put("Tipo_Renta", "5ta");
        eneroData1.put("Modalidad", "Indirecta");
        eneroData1.put("CodCCosto", "002");
        eneroData1.put("CodCta_Cancel", "101002");
        eneroData1.put("Tipo_Pago", "Efectivo");
        eneroData1.put("Num_Tran_Ban", "123456789");
        eneroData1.put("Detraccion", "20.00");

        monthlyData.put("Enero", List.of(eneroData1));

        try (InputStream is = new ClassPathResource("template.xlsm").getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            for (Map.Entry<String, List<Map<String, Object>>> entry : monthlyData.entrySet()) {
                String sheetName = entry.getKey();
                List<Map<String, Object>> data = entry.getValue();

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    continue;
                }

                int startRow = 1;
                for (Map<String, Object> rowData : data) {
                    Row row = sheet.createRow(startRow++);
                    int cellIndex = 0;
                    for (Object value : rowData.values()) {
                        Cell cell = row.createCell(cellIndex++);
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            System.out.println("Archivo Excel con data estática generado correctamente.");
            return Mono.just(resource);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Error generating Excel file", e));
        }
    }
}