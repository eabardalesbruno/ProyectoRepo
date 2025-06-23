package com.proriberaapp.ribera.utils.emails;

import java.time.LocalDateTime;

public class TransferEmailTemplateBuilder {

    public static String buildTransferSuccessEmail(String amount, String recipientFullName, LocalDateTime dateTime) {
        String formattedDate = dateTime.toLocalDate().toString();
        String formattedTime = dateTime.toLocalTime().withNano(0).toString();

        return "<!DOCTYPE html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "<title>Transferencia Exitosa</title>" +
                "<link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap\" rel=\"stylesheet\">" +
                "<style>" +
                "body{margin:0;padding:0;background-color:#f1f5f9;font-family:'Inter',sans-serif;}" +
                ".email-container{display:flex;justify-content:center;padding:40px 16px;}" +
                ".email-card{width:100%;max-width:700px;background-color:#ffffff;border-radius:12px;box-shadow:0 1px 3px rgba(0, 0, 0, 0.1);overflow:hidden;}" +
                ".email-header{background-color:#1d4e32;padding:24px;display:flex;justify-content:center;}" +
                ".email-header img{height:40px;}" +
                ".email-body{padding:32px;text-align:center;}" +
                ".success-text{font-size:16px;color:#374151;margin:0 0 8px;}" +
                ".transfer-text{font-size:14px;color:#6b7280;margin:0 0 16px;}" +
                ".amount-text{font-size:28px;font-weight:700;color:#1f2937;margin:0 0 24px;}" +
                ".sent-to{font-size:14px;color:#374151;margin:0 0 16px;}" +
                ".date-time{font-size:14px;color:#6b7280;display:flex;justify-content:center;align-items:center;gap:12px;margin-bottom:24px;}" +
                ".info-note{font-size:14px;color:#4b5563;line-height:1.6;}" +
                ".email-footer{background-color:#f9fafb;padding:24px 32px;border-top:1px solid #e5e7eb;}" +
                ".footer-title{font-size:14px;font-weight:600;margin:0 0 8px;color:#111827;}" +
                ".footer-text{font-size:14px;color:#374151;margin:0;}" +
                ".footer-text a{color:#2563eb;text-decoration:none;}" +
                ".footer-text a:hover{text-decoration:underline;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +
                "<div class=\"email-card\">" +
                "<div class=\"email-header\">" +
                "<img src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/ribera/Carta/3d76fb2e-6121-4e70-8054-cb8b3052aa74-menu&image.png\" alt=\"Logo Ribera del Río\">" +
                "</div>" +
                "<div class=\"email-body\">" +
                "<p class=\"success-text\">¡Transferencia exitosa!</p>" +
                "<p class=\"transfer-text\">Se realizó una transferencia de:</p>" +
                "<p class=\"amount-text\">" + amount + " USD Rewards</p>" +
                "<p class=\"sent-to\">Enviado a: <strong>" + recipientFullName + "</strong></p>" +
                "<div class=\"date-time\">" +
                "<span class=\"date\">📅 " + formattedDate + "</span>" +
                "<span class=\"divider\">|</span>" +
                "<span class=\"time\">⏰ " + formattedTime + "</span>" +
                "</div>" +
                "<p class=\"info-note\">" +
                "Este correo es solo de carácter informativo, en caso de no haber realizado este movimiento por favor de comunicarse.<br>Muchas gracias." +
                "</p>" +
                "</div>" +
                "<div class=\"email-footer\">" +
                "<p class=\"footer-title\">¿Necesitas ayuda?</p>" +
                "<p class=\"footer-text\">" +
                "Envíe sus comentarios o información de errores a " +
                "<a href=\"mailto:informesyreservas@cieneguillariberadelrio.com\">informesyreservas@cieneguillariberadelrio.com</a>" +
                "</p>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}