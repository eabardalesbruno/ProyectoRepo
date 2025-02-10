package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Infraestructure.repository.ExcelRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl {

    @Autowired
    private final ExcelRepository excelRepository;

    public Mono<ByteArrayResource> generateExcelFromEntitiesByMonth(List<BookingWithPaymentDTO> entities) {
        try (InputStream is = new ClassPathResource("template.xlsm").getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Map<String, List<BookingWithPaymentDTO>> dataByMonth = entities.stream()
                    .collect(Collectors.groupingBy(entity -> {
                        int month = entity.getCreateDatInvoice().getMonthValue();
                        return getMonthName(month);
                    }));

            for (Map.Entry<String, List<BookingWithPaymentDTO>> entry : dataByMonth.entrySet()) {
                String sheetName = entry.getKey();
                List<BookingWithPaymentDTO> monthlyData = entry.getValue();

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    System.out.println("Hoja " + sheetName + " no encontrada en la plantilla. Se omite.");
                    continue;
                }

                int startRow = 1;
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                for (BookingWithPaymentDTO entity : monthlyData) {
                    Row row = sheet.createRow(startRow++);

                    String codDoc = switch (entity.getIdType()) {
                        // en el excel hay más casos, en la tb solo hay 1 y 2
                        case 1 -> "01"; // Factura
                        case 2 -> "03"; // Boleta
                        default -> "00"; // Otros
                    };
                    String correlative = String.format("%08d", entity.getCorrelative());
                    String currency = switch (entity.getIdCurrency()) {
                        case 1 -> "Soles";
                        case 2 -> "Dolares";
                        default -> "Moneda desconocida";
                    };

                    String condPago = "CONTADO";
                    int tipoOpe = 1;
                    int isc = 0;
                    String formattedTc = String.format("%.2f", entity.getTc());
                    String formattedDate = entity.getCreateDatInvoice().toLocalDate().format(dateFormatter);
                    String bienServ = "4";
                    String modalidad = "1";
                    String detraccion = "0";

                    Map<String, Object> columnMapping = new HashMap<>();
                    columnMapping.put("CodDoc", codDoc);
                    columnMapping.put("SerieDoc", entity.getSerie());
                    columnMapping.put("NroDocDel", correlative);
                    columnMapping.put("Cond_Pago", condPago);
                    columnMapping.put("Ruc_Prov", entity.getIdentifierClient().toString());
                    columnMapping.put("Fecha", formattedDate);
                    columnMapping.put("Moneda", currency);
                    columnMapping.put("ValorTC", formattedTc);
                    columnMapping.put("Tipo_Ope", tipoOpe);
                    columnMapping.put("Neto", entity.getSubtotal());
                    columnMapping.put("Igv", entity.getTotalIgv());
                    columnMapping.put("Isc", isc);
                    columnMapping.put("Total", entity.getTotalPayment());
                    columnMapping.put("D_Bie_Servic", bienServ);

                    columnMapping.put("Modalidad", modalidad);
                    columnMapping.put("Detraccion", detraccion);
                    columnMapping.put("Descuento", entity.getTotaldiscount());
                    columnMapping.put("Porcentaje_descuento", entity.getPercentagediscount()+"%");

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

    //Excel de Comisiones
    public Mono<byte[]> generateExcel() {
        return excelRepository.findExcelReportCommission()
                .map(dto -> new String[]{
                        dto.getCodigoReserva(),
                        dto.getPromotor(),
                        dto.getRucNumber(),
                        dto.getFacturaLink(),
                        dto.getEstado(),
                        dto.getNombreHabitacion(),
                        ESTADO_HABITACION_MAP.getOrDefault(dto.getEstadoHabitacion(), "Desconocido"),
                        dto.getNombreTitular(),
                        (dto.getCurrencytypeid() == 1 ? "S/." : dto.getCurrencytypeid() == 2 ? "$" : "Desconocido"),
                        dto.getCostoFinal().toString(),
                        (dto.getCostoAlimentos() != null ? dto.getCostoAlimentos() : BigDecimal.ZERO).toString(),
                        dto.getCostoSinAlimentos().toString(),
                        dto.getComision().toString()
                })
                .collectList()
                .flatMap(this::createExcelFileFromTemplate);
    }

    private Mono<byte[]> createExcelFileFromTemplate(List<String[]> data) {
        return Mono.fromSupplier(() -> {
            try (InputStream templateStream = new ClassPathResource("templateComision.xlsx").getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                Workbook workbook = new XSSFWorkbook(templateStream);
                Sheet sheet = workbook.getSheetAt(0);

                int rowNum = sheet.getLastRowNum() + 1;

                for (String[] rowData : data) {
                    Row row = sheet.createRow(rowNum++);
                    for (int colNum = 0; colNum < rowData.length; colNum++) {
                        row.createCell(colNum).setCellValue(rowData[colNum]);
                    }
                }

                workbook.write(outputStream);
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("Error al generar el archivo Excel desde la plantilla", e);
            }
        });
    }


    private static final Map<String, String> ESTADO_HABITACION_MAP = Map.of(
            "1", "Rechazado",
            "2", "Aceptado",
            "3", "Pendiente",
            "4", "Anulado",
            "5", "Finalizado",
            "6", "Ocupado",
            "7", "Limpieza",
            "8", "Libre"
    );
}