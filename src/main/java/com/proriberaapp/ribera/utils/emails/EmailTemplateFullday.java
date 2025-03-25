package com.proriberaapp.ribera.utils.emails;

public class EmailTemplateFullday {

    public static String getAcceptanceTemplate(String recipientName,String type, int reservationCode, String checkInDate, int adults, int children) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Confirmación de Reserva</title>" +
                "    <style>" +
                "         body { font-family: Arial, sans-serif;background-color: #f4f4f4; margin: 0; padding: 0;}" +
                "        .container {max-width: 700px;margin: 20px auto;background: #FFFFFF;border-radius: 10px;overflow: hidden;box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}" +
                "        .header img {width: 100%;display: block;}" +
                "        .content {padding: 20px;}" +
                "        .title {font-size: 18px; font-weight: bold;}" +
                "        .highlight {font-style: italic;font-weight: bold;}" +
                "        .info-box {display: flex;background: #ffffff;border-radius: 10px;overflow: hidden;margin-top: 15px;border: 1px solid #ddd;}" +
                "        .info-box img {width: 40%;object-fit: cover;}" +
                "        .details {padding: 15px;width: 60%;}" +
                "        .details h2 {margin: 0 0 10px;font-size: 18px;font-weight: bold;}" +
                "        .details p {margin: 5px 0;font-size: 14px;}" +
                "        .separator {height: 2px;background-color: #025928;margin: 10px 0;}" +
                "        .footer {padding: 15px;background: #F6F7FB;text-align: center;font-size: 14px;}" +
                "        .footer a {color: #007bff;text-decoration: none;}" +
                "        .check-in-out {display: flex;justify-content: space-between;}" +
                "        .note {font-size: 13px;color: #666;}" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <img src='https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.png' alt='Fullday del hotel'>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p class='title'>Estimado(a), " + recipientName + "</p>" +
                "            <p>El presente es para informarle que se completó exitosamente el registro de su pago para la reserva de: <span class='highlight'>"+ type +".</span></p>" +
                "            <div class='info-box'>" +
                "                <img src='https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/Carta/7551089d-8607-4471-a45d-3335e69e11c0-menu&image.png' alt='Habitación con vista a la piscina'>" +
                "                <div class='details'>" +
                "                    <h2>Fullday Todo completo</h2>" +
                "                    <p>Titular de la reserva: <strong>" + recipientName + "</strong> </p>" +
                "                    <p>Código de reserva: <strong>" + reservationCode + "</strong></p>" +
                "                    <div class='separator'></div>" +
                "                    <div class='check-in-out'>" +
                "                        <p>Check-in: <strong>" + checkInDate + "</strong></p>" +
                "                        <p>Check-out: <strong>" + checkInDate + "</strong></p>" +
                "                    </div>" +
                "                    <p>Cantidad de personas: <strong>" + adults + " adultos, " + children + " niño(s)</strong></p>" +
                "                    <div class='separator'></div>" +
                "                    <p>Ubicación:</p>" +
                "                    <p><strong>Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593</strong></p>" +
                "                </div>" +
                "            </div>" +
                "            <p class='note'>Este correo es solo de carácter informativo, no es un comprobante de pago. En caso de no poder usar la reservación, por favor llamar con 2 días de anticipación.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>¿Necesitas ayuda?</p>" +
                "            <p>Envía tus comentarios a <a href='mailto:informesyreservas@cieneguillariberadelrio.com'>informesyreservas@cieneguillariberadelrio.com</a></p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    public static String getRejectionTemplate(String recipientName, String reservationCode, String reason) {
        return "<html>" +
                "<body>" +
                "<h2>Estimado(a) " + recipientName + ",</h2>" +
                "<p>Lamentamos informarle que su reserva ha sido <strong>RECHAZADA</strong>.</p>" +
                "<p><strong>Código de reserva:</strong> " + reservationCode + "</p>" +
                "<p><strong>Motivo:</strong> " + reason + "</p>" +
                "<p>Si tiene alguna consulta, no dude en contactarnos.</p>" +
                "<p>Atentamente,</p>" +
                "<p><strong>Equipo de Atención</strong></p>" +
                "</body>" +
                "</html>";
    }

}