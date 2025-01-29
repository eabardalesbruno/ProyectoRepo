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
                "    @page { size: 210mm 297mm; margin: 10mm;}" +
                "      body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #fff;}" +
                "      .container { max-width: 100%; border: 2px solid #006600;  border-radius: 12px; padding: 20px; box-sizing: border-box; position: relative; }"+
                "      .header { text-align: center; margin-bottom: 20px; position: relative; }" +
                "      .header img { height: 80px; margin-bottom: 10px;}" +
                "      .watermark { position: fixed;top: 50%; left: 50%; width: 60%; height:auto ;transform: translate(-50%, -50%);z-index: -1; opacity: 0.0; pointer-events: none; mix-blend-mode: multiply;}" +
                "      .no-break { page-break-inside: avoid;}"+
                "      .header h1 { margin: 0; font-size: 22px; text-transform: uppercase; color: #006600;}" +
                "      .header p { margin: 0; font-size: 14px; color: #333;}" +
                "      .form-table { width: 100%; border-collapse: separate; border-spacing: 0; margin-bottom: 20px; border: 1px solid #006600; overflow: hidden;}" +
                "      .form-table th, .form-table td { border: 1px solid #006600; text-align: center; font-size: 12px; }" +
                "      .form-table td { padding:6px 1px;}"+
                "      .Ti {height: 50px;}"+
                "      .form-table th { background-color: #e7f5e7; font-weight: bold;padding: 7px 0.3px;}" +
                "      .info-title { font-weight: bold; color: #006600; margin-top: 10px;}" +
                "      .grid-container {display: flex;flex-wrap: wrap;flex-direction: row;gap: 10px;width: 100%;margin-top: 0;}" +
                "      .info-box { display: inline-block; width: 350px; padding: 8px 10px; margin-right: 5px; margin-bottom: 10px; font-size: 14px; border: 1px solid #006600; border-radius: 12px; box-sizing: border-box;}"+
                "      .info-box strong { display: inline; margin-bottom: 2px;}" +
                "      .signature-container { display: inline-block; justify-content: space-between; margin-bottom: 0px; margin-top: 0px;}" +
                "      .signature { display: inline-block;text-align: center; margin-top: 40px; width: 300px;}" +
                "      .signature span { display: inline-block; border-top: 1px solid #006600; margin-top: 20px; font-size: 14px;}" +
                "      .terms { font-size: 12px; margin-top: 20px;}" +
                "      .terms p { margin: 5px 0;} "+
                "      .checkbox { display: flex; align-items: center; gap: 5px;}" +
                "      .checkbox label { font-size: 12px;}" +
                "      .footer-container { max-width: 900px; margin: auto; padding: 20px; border-top: 2px solid #006600; border-radius: 12px; margin-top: 20px;}" +
                "      .footer .info-title { margin-bottom: 10px;}" +
                "      .footer .policies ol { margin: 0; padding-left: 20px;}" +
                "      .policies li { margin: 3px; font-size: 11px;}" +
                "      .policies p { font-size: 12px;}"+
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<img class=\"watermark\" src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/landing/Logo-Ribera-Trasparente.png\" alt=\"Marca de agua\" />"+
                "<div class=\"header\">" +
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
                "<td colspan=\"2\">" + (entity.getFullname() != null ? entity.getFullname() : "") + "</td>" +
                "<td>" + entity.getDocumentType()  + " : " + entity.getDocumentNumber() + "</td>" +
                "<td>" + (entity.getCountrydesc() != null ? entity.getCountrydesc() : "") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th>GENERO / Genre</th>" +
                "<th>FECHA DE NACIMIENTO / Date of Birth</th>" +
                "<th colspan=\"2\">CORREO ELECTRÓNICO / Email</th>"+
                "</tr>" +
                "<tr>" +
                "<td >"+  " " +" </td>" +
                "<td>" +  " " + " </td>" +
                " <td colspan=\"2\">" + (entity.getEmail() != null ? entity.getEmail() : "" )+ "</td>" +
                "</tr>"+
                "<tr>" +
                "<th>LLEGADA / Arrival</th>" +
                "<th>SALIDA / Departure</th>" +
                "<th>PERSONAS / Pax</th>" +
                "<th>DEPARTAMENTO / Apartment</th>" +
                "</tr>" +
                "<tr>" +
                "<td>" + (entity.getDayBookingInit() != null ? entity.getDayBookingInit() : "") + "</td>" +
                "<td>" + (entity.getDayBookingEnd() != null ? entity.getDayBookingEnd() : "") + "</td>" +
                "<td>" + (entity.getNumberAdults() +" "+ entity.getNumberBabies()  +" "+ entity.getNumberChildren()) + "</td>" +
                "<td>" + (entity.getRoomName()!= null ? entity.getRoomName() : "") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th>TELÉFONO / Phone</th>" +
                "<th>DIRECCIÓN / Address</th>" +
                "<th>CIUDAD / City</th>" +
                "<th>PAÍS / Country</th>" +
                "</tr>" +
                "<tr>" +
                "<td>" + (entity.getCellphone() != null ? entity.getCellphone() : "") + "</td>" +
                "<td>" + (entity.getAddress() != null ? entity.getAddress() : "")+ "</td>" +
                "<td>" + " " + "</td>" +
                "<td>" + (entity.getCountrydesc() != null ? entity.getCountrydesc() : "") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th colspan=\"3\">FORMA DE PAGO / Payment Method</th>" +
                "<th>HAB N / Room Nr</th>" +
                "</tr>" +
                "<tr>" +
                "<td colspan=\"3\">" + (entity.getMethodPayment() != null ? entity.getMethodPayment() : "")+ "</td>" +
                "<td>" + (entity.getRoomNumber() != null ? entity.getRoomNumber() : "") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th colspan=\"2\">TARIFA / Tariff</th> " +
                "<th colspan=\"2\">MOTIVO DE VAIJE / Nationality</th>" +
                "</tr>" +
                "<tr>" +
                "<td colspan=\"2\" class=\"Ti\">"+ " " +  "</td>" +
                "<td colspan=\"2\" class=\"Ti\"> </td>" +
                "</tr>"+
                "</table>" +
                "</div>"+
                "        <div class=\"terms\">" +
                "            <p><strong>- El huésped deberá firmar su check in (ingreso) y su check out (salida).</strong></p>" +
                "            <p><strong>- El huésped pagará una penalidad de S/ 50.00 (cincuenta soles) por hora o fracción por demora en abandonar el apartamento asignado por Ribera del Río.</strong></p>" +
                "            <p><strong>- Marcar con Sí / No en los recuadros.</strong></p>" +
                "            <div class=\"checkbox\">" +
                "                <input type=\"checkbox\" id=\"data-processing\"></input>" +
                "                <label for=\"data-processing\">Autorizo el tratamiento de mis datos para fines de prospección, promoción comercial para conocer las membresías de socios de Ribera del Río y sus empresas vinculadas amparado en Ley N° 29733, Ley de Protección de Datos Personales.</label>" +
                "            </div>\n" +
                "            <div class=\"checkbox\">" +
                "                <input type=\"checkbox\" id=\"terms-conditions\"></input >" +
                "                <label for=\"terms-conditions\">Acepto los Términos y condiciones y Políticas de privacidad que están aquí.</label>" +
                "            </div>" +
                "        </div>"+
                "        <div class=\"signature-container\">" +
                "    <div class=\"signature\">" +
                "        <span>CHECK IN<br />Firma</span>" +
                "    </div>" +
                "    <div class=\"signature\">" +
                "        <span>CHECK OUT : HORA<br />Firma</span>" +
                "    </div>" +
                "</div>";

        // Companions Section
        if (entity.getLstCompanions() != null && !entity.getLstCompanions().isEmpty()) {
            int numCompanions = 0;
            htmlContent += "<div class=\"info-title\">Acompañantes:</div>";
            htmlContent += "<div class=\"grid-container\">";
            for (ReservationReportDto companion : entity.getLstCompanions()) {
                numCompanions++;
                htmlContent += "<table class=\"form-table no-break\">\n" +
                        "          <strong>Acompañante " + numCompanions + ":</strong><br />\n" +
                        "          <tr>\n" +
                        "            <th colspan=\"2\">NOMBRE COMPLETO / Full Name</th>\n" +
                        "            <th>TIPO DE DOCUMENTO / ID Type</th>\n" +
                        "            <th>NACIONALIDAD / Nationality</th>\n" +
                        "          </tr>\n" +
                        "          <tr>\n" +
                        "            <td colspan=\"2\">"+ companion.getFullname() +"</td>\n" +
                        "            <td> " + companion.getDocumentType() + " : " + companion.getDocumentNumber() + "</td>" +
                        "            <td> " + (companion.getCountrydesc() != null ? companion.getCountrydesc() : "") + "</td>\n" +
                        "          </tr>\n" +
                        "          <tr>\n" +
                        "            <th>TELÉFONO / Phone</th>\n" +
                        "            <th>CIUDAD / City</th>\n" +
                        "            <th>DIRECCIÓN / Address</th>\n" +
                        "            <th>PAÍS / Country</th>" +
                        "          </tr>\n" +
                        "          <tr>\n" +
                        "            <td>" + (companion.getCellphone() != null ? companion.getCellphone() : "")+ "</td>\n" +
                        "            <td>" +""+ "</td>\n" +
                        "            <td>" + " " + "</td>\n" +
                        "            <td>" + (companion.getCountrydesc() != null ? companion.getCountrydesc() : "")+ "</td>" +
                        "          </tr>" +
                        "          <tr>\n" +
                        "             <th >CORREO ELECTRÓNICO / Email</th>\n" +
                        "            <th>GENERO / Genre</th>\n" +
                        "            <th>FECHA DE NACIMIENTO / Date of Birth</th>\n" +
                        "            <th>EDAD / Age</th>" +
                        "          </tr>\n" +
                        "          <tr>\n" +
                        "            <td >" + (companion.getEmail() != null ? companion.getEmail() : "") + "</td>\n" +
                        "            <td>" + (companion.getGender() != null ? companion.getGender() : "")+ "</td>\n" +
                        "            <td>" + "" + "</td>\n" +
                        "            <td>" + (companion.getYears() != null ? companion.getYears() : "") + "</td>" +
                        "          </tr>\n" +

                        "        </table>";
                if (numCompanions % 2 == 0) {
                    htmlContent += "</div><div class=\"grid-container\">";
                }
            }
            htmlContent += "</div>"+
                    " <!-- Footer Section with its own container -->\n" +
                    "    <div class=\"footer-container no-break\">\n" +
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