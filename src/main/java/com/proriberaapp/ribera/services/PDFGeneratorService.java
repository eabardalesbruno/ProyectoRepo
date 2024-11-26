package com.proriberaapp.ribera.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.proriberaapp.ribera.Domain.entities.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PDFGeneratorService {

    public File generatePDFFile(String htmlContent, String fileName) throws IOException {
        File outputFile = new File(fileName);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(htmlContent, "");
        builder.toStream(outputStream);
        builder.run();
        outputStream.close();
        return outputFile;
    }

    public static void generatePdfFromHtml(String codigotransaccion, String username, String documentNumber, String currencyName, String fecha, String bookingid, String bookingiroomname, String precioVentaTotal, String pdfFileName) throws IOException {
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Constancia de Pago Wallet</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f8f9fa; }"
                + ".container { max-width: 800px; margin: auto; background: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 8px; }"
                + ".header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }"
                + ".logo img { height: 60px; }"
                + ".header-text { text-align: right; }"
                + ".info, .summary { margin-bottom: 20px; }"
                + ".table { width: 100%; border-collapse: collapse; margin-top: 20px; }"
                + ".table th, .table td { border: 1px solid #006600; text-align: left; padding: 8px; }"
                + ".table th { background-color: #006600; color: #ffffff; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\">"
                + "<div class=\"logo\">"
                + "<img src=\"https://via.placeholder.com/60\" alt=\"Logo\">"
                + "<h2>RIBERA DEL RÍO CLUB RESORT</h2>"
                + "</div>"
                + "<div class=\"header-text\">"
                + "<h1>CONSTANCIA DE PAGO</h1>"
                + "<h2>Nro de Transacción: " + codigotransaccion + "</h2>"
                + "</div>"
                + "</div>"
                + "<div class=\"info\">"
                + "<strong>Señor(es):</strong> " + username + "<br>"
                + "<strong>Núm. de Documento:</strong> " + documentNumber + "<br>"
                + "<strong>Moneda:</strong> " + currencyName + "<br>"
                + "<strong>Fecha de Emisión:</strong> " + fecha + "<br>"
                + "</div>"
                + "<table class=\"table\">"
                + "<thead>"
                + "<tr>"
                + "<th>CÓDIGO</th>"
                + "<th>DESCRIPCIÓN</th>"
                + "<th>CANT.</th>"
                + "<th>UM</th>"
                + "<th>VALOR UNITARIO</th>"
                + "<th>PRECIO VENTA TOTAL</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>"
                + "<tr>"
                + "<td>" + bookingid + "</td>"
                + "<td>" + bookingiroomname + "</td>"
                + "<td>1</td>"
                + "<td>-</td>"
                + "<td>" + precioVentaTotal + "</td>"
                + "<td>" + precioVentaTotal + "</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>"
                + "<div class=\"summary\">"
                + "<strong>Monto Neto:</strong> " + precioVentaTotal + "<br>"
                + "<strong>Total:</strong> " + precioVentaTotal
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        File pdfFile = new File(pdfFileName);
        try (OutputStream os = new FileOutputStream(pdfFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, "");
            builder.toStream(os);
            builder.run();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (pdfFile.exists()) {
            boolean deleted = pdfFile.delete();
            if (deleted) {
                System.out.println("El archivo PDF se eliminó exitosamente.");
            } else {
                System.out.println("No se pudo eliminar el archivo PDF.");
            }
        }
    }




}