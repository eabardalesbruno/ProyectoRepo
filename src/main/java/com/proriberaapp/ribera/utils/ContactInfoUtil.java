package com.proriberaapp.ribera.utils;

import com.proriberaapp.ribera.Api.controllers.client.dto.ContactInfo;
import com.proriberaapp.ribera.Api.controllers.client.dto.EventContactInfo;

public class ContactInfoUtil {
    public static String getEventContactInfo(EventContactInfo eventContactInfo) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 20px;" +
                "        }" +
                "        .container {" +
                "            background-color: #ffffff;" +
                "            padding: 20px;" +
                "            margin: auto;" +
                "            max-width: 600px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        p {" +
                "            font-size: 14px;" +
                "            color: #666;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .data-group {" +
                "            margin-bottom: 15px;" +
                "        }" +
                "        .data-group label {" +
                "            font-weight: bold;" +
                "            margin-bottom: 5px;" +
                "            color: #555;" +
                "        }" +
                "        .data-group span {" +
                "            display: block;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "            background-color: #f9f9f9;" +
                "            border: 1px solid #ddd;" +
                "            border-radius: 4px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <img style='width:100%' src='http://imgfz.com/i/hGIOrlW.png'>" +
                "    <div class='container'>" +
                "        <h2>Notificacion:</h2>" +
                "        <h3>Se ha brindado los datos de contacto.</h3>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='usuario'>Nombres</label>" +
                "                <span id='usuario'>" + eventContactInfo.getName() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='apellido'>Apellidos</label>" +
                "                <span id='apellido'>" + eventContactInfo.getLastName() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='phone'>Nro. Celular</label>" +
                "                <span>+51 " + eventContactInfo.getCellphone() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='email'>Correo electronico</label>" +
                "                <span id='email'>" + eventContactInfo.getEmail() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='entrada'>Fecha de entrada</label>" +
                "                <span id='entrada'>" + eventContactInfo.getStartDate() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='salida'>Fecha de salida</label>" +
                "                <span id='salida'>" + eventContactInfo.getEndDate() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='adultos'>Numero de adultos</label>" +
                "                <span id='adultos'>" + eventContactInfo.getNumberAdults() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='niños'>Numero de infantes</label>" +
                "                <span id='niños'>" + eventContactInfo.getNumberChildren() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='mensaje'>Mensaje</label>" +
                "                <span>" + eventContactInfo.getMessage() + "</span>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    public static String getContactInfo(ContactInfo contactInfo) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 20px;" +
                "        }" +
                "        .container {" +
                "            background-color: #ffffff;" +
                "            padding: 20px;" +
                "            margin: auto;" +
                "            max-width: 600px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        p {" +
                "            font-size: 14px;" +
                "            color: #666;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .data-group {" +
                "            margin-bottom: 15px;" +
                "        }" +
                "        .data-group label {" +
                "            font-weight: bold;" +
                "            margin-bottom: 5px;" +
                "            color: #555;" +
                "        }" +
                "        .data-group span {" +
                "            display: block;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "            background-color: #f9f9f9;" +
                "            border: 1px solid #ddd;" +
                "            border-radius: 4px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <img style='width:100%' src='http://imgfz.com/i/hGIOrlW.png'>" +
                "    <div class='container'>" +
                "        <h2>Notificacion:</h2>" +
                "        <h3>Se ha brindado los datos de contacto.</h3>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='usuario'>Nombres</label>" +
                "                <span id='usuario'>" + contactInfo.getName() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='apellido'>Apellidos</label>" +
                "                <span id='apellido'>" + contactInfo.getLastName() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='phone'>Nro. Celular</label>" +
                "                <span>+51 " + contactInfo.getCellphone() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='email'>Correo electronico</label>" +
                "                <span id='email'>" + contactInfo.getEmail() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='entrada'>Numero de documento</label>" +
                "                <span id='entrada'>" + contactInfo.getDocumentNumber() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='mensaje'>Mensaje</label>" +
                "                <span>" + contactInfo.getMessage() + "</span>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

}
