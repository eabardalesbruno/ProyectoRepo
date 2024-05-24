package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.PaymentBookService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-books/export")
public class PaymentBookExportController {

    private final PaymentBookService paymentBookService;

    @Autowired
    public PaymentBookExportController(PaymentBookService paymentBookService) {
        this.paymentBookService = paymentBookService;
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportPaymentBooksToExcel() {
        Flux<PaymentBookEntity> paymentBooksFlux = paymentBookService.getAllPaymentBooks();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payment Books");

            List<PaymentBookEntity> paymentBooksList = paymentBooksFlux.collectList().block();

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Payment Book Id", "Payment Method Id", "Currency Type Id", "Payment State Id", "Refuse Reason Id", "User Client Id", "Client Type Id", "Amount", "Description", "Payment Date", "Operation Code", "Note", "Total Cost", "Image Voucher", "Total Points"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (PaymentBookEntity paymentBook : paymentBooksList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(paymentBook.getPaymentBookId());
                row.createCell(1).setCellValue(paymentBook.getPaymentMethodId());
                row.createCell(2).setCellValue(paymentBook.getCurrencyTypeId());
                row.createCell(3).setCellValue(paymentBook.getPaymentStateId());
                row.createCell(7).setCellValue(paymentBook.getAmount().doubleValue());
                row.createCell(8).setCellValue(paymentBook.getDescription());
                row.createCell(9).setCellValue(paymentBook.getPaymentDate().toString());
                row.createCell(10).setCellValue(paymentBook.getOperationCode());
                row.createCell(11).setCellValue(paymentBook.getNote());
                row.createCell(12).setCellValue(paymentBook.getTotalCost().doubleValue());
                row.createCell(13).setCellValue(paymentBook.getImageVoucher());
                row.createCell(14).setCellValue(paymentBook.getTotalPoints());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDispositionFormData("filename", "payment_books.xlsx");

            return new ResponseEntity<>(excelBytes, responseHeaders, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}