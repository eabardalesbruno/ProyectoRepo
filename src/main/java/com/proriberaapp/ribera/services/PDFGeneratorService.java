package com.proriberaapp.ribera.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.proriberaapp.ribera.Api.controllers.admin.dto.ResponseFileDto;
import com.proriberaapp.ribera.Domain.dto.ReservationReportDto;
import com.proriberaapp.ribera.Domain.entities.*;

import org.apache.commons.io.FileUtils;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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

    public static File generatePdfFromHtml(String codigotransaccion, String username, String documentNumber,
            String currencyName, String fecha, String bookingid, String bookingiroomname, String precioVentaTotal,
            String pdfFileName) throws IOException {
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\" />"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
                + "    <title>Constancia de Pago Wallet</title>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f8f9fa; }"
                + "        .container { max-width: 800px; margin: auto; background: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 8px; }"
                + "        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }"
                + "        .logo img { height: 60px; }"
                + "        .header-text { text-align: right; }"
                + "        .header-text h1 { margin: 0; font-size: 18px; color: #006600; }"
                + "        .header-text h2 { margin: 5px 0; font-size: 16px; }"
                + "        .info { margin-bottom: 20px; }"
                + "        .table { width: 100%; border-collapse: collapse; margin-top: 20px; }"
                + "        .table th, .table td { border: 1px solid #006600; text-align: left; padding: 8px; }"
                + "        .table th { background-color: #006600; color: #ffffff; }"
                + "        .summary { margin-top: 40px; text-align: right; }"
                + "        .summary .total { font-weight: bold; font-size: 16px; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"container\">"
                + "        <div class=\"header\">"
                + "            <div class=\"logo\">"
                + "                <img src=\"https://i.postimg.cc/xT6NS6R9/Color-4x.png\" alt=\"Logo\" />"
                + "            </div>"
                + "            <div class=\"header-text\">"
                + "                <h1>CONSTANCIA DE PAGO WALLET</h1>"
                + "                <h2>Nro de Transacción: " + codigotransaccion + "</h2>"
                + "            </div>"
                + "        </div>"
                + "        <div class=\"info\">"
                + "            <div><strong>Señor(es):</strong> " + username + "</div>"
                + "            <div><strong>Núm. de Documento:</strong> " + documentNumber + "</div>"
                + "            <div><strong>Moneda:</strong> " + currencyName + "</div>"
                + "            <div><strong>Fecha de Emisión:</strong> " + fecha + "</div>"
                + "        </div>"
                + "        <table class=\"table\">"
                + "            <thead>"
                + "                <tr>"
                + "<th>CÓDIGO</th>"
                + "<th>DESCRIPCIÓN</th>"
                + "<th>CANT.</th>"
                + "<th>VALOR UNITARIO</th>"
                + "<th>PRECIO VENTA TOTAL</th>"
                + "                </tr>"
                + "            </thead>"
                + "            <tbody>"
                + "                <tr>"
                + "<td>" + bookingid + "</td>"
                + "<td>" + bookingiroomname + "</td>"
                + "<td>1</td>"
                + "<td>" + precioVentaTotal + "</td>"
                + "<td>" + precioVentaTotal + "</td>"
                + "                </tr>"
                + "            </tbody>"
                + "        </table>"
                + "        <div class=\"summary\">"
                + "            <div>Monto Neto: " + precioVentaTotal + "</div>"
                + "            <div class=\"total\">Total: " + precioVentaTotal + "</div>"
                + "        </div>"
                + "    </div>"
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
            throw new IOException("Error generating PDF.", e);
        }

        return pdfFile;
    }

    public static File generateReservationPdfFromHtml(ReservationReportDto entity) throws IOException {
        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\" />" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />" +
                "<title>Reserva</title>" +
                "<style>" +
                "     @page { size: A4; margin: 10mm; }\n" +
                "    body {\n" +
                "        font-family: Arial, sans-serif;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        background-color: #fff;\n" +
                "    }\n" +
                "    .container {\n" +
                "        max-width: 100%;\n" +
                "        border: 2px solid #006600;\n" +
                "        border-radius: 12px;\n" +
                "        padding: 20px;\n" +
                "    }\n" +
                "    .header {\n" +
                "        text-align: center;\n" +
                "        margin-bottom: 20px;\n" +
                "    }\n" +
                "    .header img {\n" +
                "        height: 80px;\n" +
                "        margin-bottom: 10px;\n" +
                "    }\n" +
                "    .header h1 {\n" +
                "        margin: 0;\n" +
                "        font-size: 22px;\n" +
                "        text-transform: uppercase;\n" +
                "        color: #006600;\n" +
                "    }\n" +
                "    .header p {\n" +
                "        margin: 0;\n" +
                "        font-size: 14px;\n" +
                "        color: #333;\n" +
                "    }\n" +
                "    .form-table {\n" +
                "        width: 100%;\n" +
                "        border-collapse: separate;\n" +
                "        border-spacing: 0;\n" +
                "        margin-bottom: 20px;\n" +
                "        border: 1px solid #006600;\n" +
                "        border-radius: 12px;\n" +
                "        overflow: hidden;\n" +
                "    }\n" +
                "    .form-table th, .form-table td {\n" +
                "        border: 1px solid #006600;\n" +
                "        padding: 8px;\n" +
                "        text-align: left;\n" +
                "        font-size: 14px;\n" +
                "    }\n" +
                "    .form-table th {\n" +
                "        background-color: #e7f5e7;\n" +
                "        font-weight: bold;\n" +
                "    }\n" +
                "    .info-title {\n" +
                "        font-weight: bold;\n" +
                "        color: #006600;\n" +
                "        margin-top: 10px;\n" +
                "    }\n" +
                "  .grid-container {\n" +
                "    display: flex; \n" +
                "    flex-wrap: wrap; flex-direction:row; \n" +
                "    gap: 10px; \n" +
                "    width: 100%;\n" +
                "    margin-top: 0;\n" +
                "}" +
                ".info-box {\n" +
                "    display:inline-block  ;width: 300px;\n" +
                "    padding: 8px 10px;\n" +
                "    margin-right: 5px; \n" +
                "    margin-bottom: 10px; \n" +
                "    font-size: 14px;\n" +
                "    border: 1px solid #006600;\n" +
                "    border-radius: 12px;\n" +
                "    box-sizing: border-box;\n" +
                "}" +
                "\n" +
                ".info-box strong {\n" +
                "    display: inline;\n" +
                "    margin-bottom: 2px;\n" +
                "}" +
                "   .signature-container {\n" +
                "    display: inline-block;;\n" +
                "    justify-content: space-between; margin-bottom:15px;\n" +
                "    margin-top: 20px;\n" +
                "}\n" +
                "\n" +
                ".signature {\ndisplay: inline-block;" +
                "    text-align: center;\n" +
                "    margin-top: 40px;\n" +
                "    width: 300px;\n" +
                "}\n" +
                "\n" +
                ".signature span {\n" +
                "    display: inline-block;\n" +
                "    border-top: 1px solid #006600;\n" +
                "    margin-top: 20px;\n" +
                "    font-size: 14px;\n" +
                "}" +
                "    .terms {\n" +
                "        font-size: 12px;\n" +
                "        margin-top: 20px;\n" +
                "    }\n" +
                "    .terms p {\n" +
                "        margin: 5px 0;\n" +
                "    }\n" +
                "    .checkbox {\n" +
                "        display: flex;\n" +
                "        align-items: center;\n" +
                "        gap: 5px;\n" +
                "    }\n" +
                "    .checkbox label {\n" +
                "        font-size: 12px;\n" +
                "    }\n" +
                "    .footer-container {\n" +
                "        max-width: 900px;\n" +
                "        margin: auto;\n" +
                "        padding: 20px;\n" +
                "        background-color: #f1f1f1;\n" +
                "        border-top: 2px solid #006600;\n" +
                "        border-radius: 12px;\n" +
                "        margin-top: 20px;\n" +
                "    }\n" +
                "    .footer .info-title {\n" +
                "        margin-bottom: 10px;\n" +
                "    }\n" +
                "    .footer .policies ol {\n" +
                "        margin: 0;\n" +
                "        padding-left: 20px;\n" +
                "    }\n" +
                "    .policies li {\n" +
                "        margin: 3px; font-size: 11px; " +
                "    }"+
                " .policies p {font-size: 12px}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<img src=\"https://i.postimg.cc/xT6NS6R9/Color-4x.png\" alt=\"Logo\" />" +
                "<h1>TARJETA DE REGISTRO / GUEST REGISTER CARD</h1>" +
                "<p>RIBERA DEL RIO CLUB RESORT S.A.<br/>RUC: 20608720911</p>" +
                "</div>" +

                "<div class=\"form-section\">" +
                "<table class=\"form-table\">" +
                "<tr>" +
                "<th colspan=\"2\">NOMBRE COMPLETO / Full Name</th>" +
                "<th>TIPO DE DOCUMENTO / ID Type</th>" +
                "<th>NACIONALIDAD / Nationality</th>" +
                "</tr>" +
                "<tr>" +
                "<td colspan=\"2\">" + entity.getFullname() + "</td>" +
                "<td>" + entity.getDocumentType() + " : " + entity.getDocumentNumber() + "</td>" +
                "<td>" + entity.getCountrydesc() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th>LLEGADA / Arrival</th>" +
                "<th>SALIDA / Departure</th>" +
                "<th>PERSONAS / Pax</th>" +
                "<th>DEPARTAMENTO / Apartment</th>" +
                "</tr>" +
                "<tr>" +
                "<td>" + entity.getDayBookingInit() + "</td>" +
                "<td>" + entity.getDayBookingEnd() + "</td>" +
                "<td>" + (entity.getNumberAdults() +" "+ entity.getNumberBabies()  +" "+ entity.getNumberChildren()) + "</td>" +
                "<td>" + entity.getRoomName() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th>DIRECCIÓN / Address</th>" +
                "<th>TELÉFONO / Phone</th>" +
                "<th>CIUDAD / City</th>" +
                "<th>PAÍS / Country</th>" +
                "</tr>" +
                "<tr>" +
                "<td>" + entity.getAddress()+ "</td>" +
                "<td>" + entity.getCellphone() + "</td>" +
                "<td>" + " " + "</td>" +
                "<td>" + entity.getCountrydesc() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th>CORREO ELECTRÓNICO / Email</th>" +
                "<th colspan=\"2\">FORMA DE PAGO / Payment Method</th>" +
                "<th>HAB N / Room Nr</th>" +
                "</tr>" +
                "<tr>" +
                "<td>" + entity.getEmail() + "</td>" +
                "<td colspan=\"2\">" + entity.getMethodPayment() + "</td>" +
                "<td>" + entity.getRoomNumber() + "</td>" +
                "</tr>" +
                "</table>" +
                "</div>"+
                "<!-- Terms and Check Section -->\n" +
                "        <div class=\"terms\">\n" +
                "            <p><strong>- El huésped deberá firmar su check in (ingreso) y su check out (salida).</strong></p>\n" +
                "            <p><strong>- El huésped pagará una penalidad de S/ 50.00 (cincuenta soles) por hora o fracción por demora en abandonar el apartamento asignado por Ribera del Río.</strong></p>\n" +
                "            <p><strong>- Marcar con Sí / No en los recuadros.</strong></p>\n" +
                "            <div class=\"checkbox\">\n" +
                "                <input type=\"checkbox\" id=\"data-processing\"></input>\n" +
                "                <label for=\"data-processing\">Autorizo el tratamiento de mis datos para fines de prospección, promoción comercial para conocer las membresías de socios de Ribera del Río y sus empresas vinculadas amparado en Ley N° 29733, Ley de Protección de Datos Personales.</label>\n" +
                "            </div>\n" +
                "            <div class=\"checkbox\">\n" +
                "                <input type=\"checkbox\" id=\"terms-conditions\"></input >" +
                "                <label for=\"terms-conditions\">Acepto los Términos y condiciones y Políticas de privacidad que están aquí.</label>\n" +
                "            </div>\n" +
                "        </div>"+
                "<!-- Signature Section -->\n" +
                "        <div class=\"signature-container\">\n" +
                "    <div class=\"signature\">\n" +
                "        <span>CHECK IN<br />Firma</span>\n" +
                "    </div>\n" +
                "    <div class=\"signature\">\n" +
                "        <span>CHECK OUT : HORA<br />Firma</span>\n" +
                "    </div>\n" +
                "</div>";

        // Companions Section
        if (entity.getLstCompanions() != null && !entity.getLstCompanions().isEmpty()) {
            int numCompanions = 0;
            htmlContent += "<div class=\"info-title\">Acompañantes:</div>";
            htmlContent += "<div class=\"grid-container\">";
            for (ReservationReportDto companion : entity.getLstCompanions()) {
                numCompanions++;
                htmlContent += "<div class=\"info-box\">" +
                        "<strong>Acompañante " + numCompanions + ":</strong><br></br>" +
                        "<strong>Señor(es):</strong> " + companion.getFullname() + "<br></br>" +
                        "<strong>Tipo de Documento:</strong> " + companion.getDocumentType() + "<br></br>" +
                        "<strong>Núm. de Documento:</strong> " + companion.getDocumentNumber() + "<br></br>" +
                        "<strong>Género:</strong> " + companion.getGender() + "<br></br>" +
                        "<strong>Edad:</strong> " + companion.getYears() + "<br></br>" +
                        "</div>";
                if (numCompanions % 2 == 0) {
                    htmlContent += "</div><div class=\"grid-container\">";
                }
            }
            htmlContent += "</div>"+
                    " <!-- Footer Section with its own container -->\n" +
                    "    <div class=\"footer-container\">\n" +
                    "        <div class=\"footer\">\n" +
                    "            <div class=\"info-section\">\n" +
                    "                <div class=\"info-title\">Políticas y Penalidades:</div>\n" +
                    "                <div class=\"policies\">\n" +
                    "                    <p><strong>I. Uso de las piscinas</strong></p>\n" +
                    "                    <ol>\n" +
                    "                        <li>El uso de las piscinas está permitido desde las 10:00 hrs hasta las 18:00 hrs.</li>\n" +
                    "                        <li>Es obligatorio el uso de ropa de baño y ducharse antes de entrar a las piscinas.</li>\n" +
                    "                        <li>No está permitido el consumo de alimentos o bebidas dentro de las piscinas.</li>\n" +
                    "                        <li>No está permitido consumir bebidas en envases de vidrio alrededor de las piscinas.</li>\n" +
                    "                        <li>*Las piscinas pueden estar cerradas de manera imprevista en caso el ente regulador de piscinas lo indique.</li>\n" +
                    "                    </ol>\n" +
                    "                    <p><strong>II. Check IN (Ingreso) y Check OUT (Salida)</strong></p>\n" +
                    "                    <ol>\n" +
                    "                        <li>La hora de ingreso al Apart Hotel (Check IN) es desde las 3:00pm.</li>\n" +
                    "                        <li>La hora de salida del Apart Hotel (Check OUT) es desde las 12:00pm.</li>\n" +
                    "                        <li>Por su seguridad el Check IN se realizará hasta las 22:00 hrs.</li>\n" +
                    "                        <li>Al momento de realizar el Check IN debe presentar sus documentos de identificación válidos (DNI, pasaporte, o carnet de extranjero) tanto la persona que realizó la reserva como sus acompañantes.</li>\n" +
                    "                        <li>Al firmar dicha ficha de registro de huésped implica la aceptación del reglamento.</li>\n" +
                    "                        <li>Al momento del Check IN, el huésped deberá cancelar la totalidad de la tarifa. No se realizarán devoluciones ni reembolsos.</li>\n" +
                    "                        <li>Antes de su salida (Check OUT) deberá acercarse a Recepción para asignarle una persona que realizará la verificación de la habitación. Las llaves del apartamento deben ser devueltas a Recepción.</li>\n" +
                    "                    </ol>\n" +
                    "                    <p><strong>III. Durante su estadía</strong></p>\n" +
                    "                    <ol>\n" +
                    "                        <li>Todo huésped debe registrarse presentando un documento de identidad – DNI o pasaporte el cual permanecerá en custodia de recepción hasta el check out de la reserva.</li>\n" +
                    "                        <li>El documento de identidad será devuelto después de verificar la buena conservación del apartamento por parte del titular y sus huéspedes. Caso contrario asumirán los costos de los daños.</li>\n" +
                    "                        <li>Al titular de la reserva se le hará entrega de llaves, tarjetas, controles de tv que serán devueltos durante el check out.</li>\n" +
                    "                        <li>No está permitido trasladar artículos (vasos, toallas, ropas de cama, muebles, menaje, etc.) fuera del apartamento, en caso de que algún artículo sufra deterioro o pérdida el titular de la reserva deberá asumir el precio.</li>\n" +
                    "                        <li>Alimentos y bebidas que no pertenezcan a la empresa solo podrán ser consumidas dentro de su apartamento.</li>\n" +
                    "                        <li>El mantenimiento del apartamento se realizará cada 24 hrs.</li>\n" +
                    "                        <li>Como somos un apart hotel que protege la ecología está prohibido fumar dentro de los apartamentos, al incumplir está norma, el huésped tendrá que pagar una penalidad de acuerdo al grado de gravedad del siniestro.</li>\n" +
                    "                        <li>No está permitido realizar reuniones en los apartamentos o habitaciones o áreas públicas que perturben el orden público por la tranquilidad de los demás huéspedes desde las 20:00 hrs. Además, el volumen del televisor debe mantenerse en un nivel apropiado.</li>\n" +
                    "                        <li>El establecimiento no se hace responsable de la pérdida de objetos no declarados.</li>\n" +
                    "                        <li>No se permite el ingreso de mascotas. En caso sea perros guía o lazarillos notificarlo con antelación en Recepción.</li>\n" +
                    "                        <li>Las rutas de evacuación en caso de desastres naturales están debidamente señaladas. Recomendamos mantener la calma y seguir las indicaciones de nuestro personal.</li>\n" +
                    "                        <li>Si desea artículos para juegos de mesa, canchas deportivas, entre otros, puede solicitarlo en recepción y se brindarán con una identificación.</li>\n" +
                    "                        <li>Las visitas sólo podrán ser realizadas en el área de lobby en Recepción. Después de las 22:00h por seguridad se cerrarán las puertas del establecimiento, si desea salir será bajo su responsabilidad y deberá identificarse a seguridad con su respectivo brazalete.</li>\n" +
                    "                    </ol>\n" +
                    "                    <p><strong>IV. Tránsito de instalaciones</strong></p>\n" +
                    "                    <ol>\n" +
                    "                        <li>La velocidad máxima permitida al interior de las instalaciones es de 15km/h.</li>\n" +
                    "                        <li>El tránsito de camionetas, autos, cuatrimotos y motos sólo está permitido por las pistas y parqueos.</li>\n" +
                    "                        <li>El no cumplimiento de los puntos 1 y 2 llevará a una sanción y penalidad.</li>\n" +
                    "                        <li>No nos hacemos responsables por la pérdida de objetos personales ni por daños.</li>\n" +
                    "                    </ol>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div>";
        }

        // Close HTML
        htmlContent += "</div>" +
                "</body>" +
                "</html>";

        // Generate PDF file
        File pdfFile = new File(entity.getPdfFileName());
        try (OutputStream os = new FileOutputStream(pdfFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, "");
            builder.toStream(os);
            builder.run();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error generating PDF.", e);
        }

        return pdfFile;
    }

    public static ResponseFileDto generatePdfFromContent(String content, String pdfFileName){
        ResponseFileDto result = new ResponseFileDto();

        File pdfFile = new File(pdfFileName);
        byte[] encoded = null;
        try (OutputStream os = new FileOutputStream(pdfFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(content, "");
            builder.toStream(os);
            builder.run();
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(pdfFile));
            result.setFile(new String(encoded, StandardCharsets.US_ASCII));
            result.setFilename(pdfFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
            result.setErrormessage(String.valueOf(new RuntimeException("Error generating PDF", e)));
        }
        return result;
    }

}