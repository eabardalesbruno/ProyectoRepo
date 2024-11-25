package com.proriberaapp.ribera.services.admin.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl {

    public Mono<ByteArrayResource> generateExcelWithMonthlyData() {
        //Cambiar por las listas de informacion en funcion de sus fechas
        //Verificar que los campos insertados son texto o numericos
        //Verificar que no me vole los macros del archivo

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
            System.out.println("Archivo Excel generado correctamente.");
            return Mono.just(resource);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Error generating Excel file", e));
        }
    }
}