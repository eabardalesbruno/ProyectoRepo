package com.proriberaapp.ribera.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}