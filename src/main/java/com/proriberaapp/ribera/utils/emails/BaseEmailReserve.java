package com.proriberaapp.ribera.utils.emails;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BaseEmailReserve {
    private List<EmailHandler> emailHandlers;

    public BaseEmailReserve() {
        this.emailHandlers = new ArrayList<>();
    }

    public void addEmailHandler(EmailHandler emailHandler) {
        emailHandlers.add(emailHandler);
    }

    public String execute() {
        String result = "";
        String loadStyles = "";
        for (EmailHandler emailHandler : emailHandlers) {
            result = result.concat(emailHandler.execute());
            loadStyles = loadStyles.concat(emailHandler.getStyles());
        }
        String templateBuilder = this.template().replace("%BODY", result).replace("%STYLES", loadStyles)
                .replace("%FOOTER", this.footer());

        // Convertir estilos a estilos en línea
        templateBuilder = EmailTemplateProcessor.inlineStyles(templateBuilder);
        System.out.println(templateBuilder);
        return templateBuilder;
    }

    private String template() {
        return """
                                            <!DOCTYPE html>
                                <html lang="es">
                                <head>
                                    <meta charset="UTF-8">
                                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                    <title>Document</title>
                                    <style>
                                body {
                                    width: 100%;
                                    background: rgb(246, 247, 251);
                                  padding-bottom: 40px;
                                padding-top: 40px;
                                    font-family: Arial, sans-serif;
                                }
                .card{
                    background-color: rgb(246, 247, 251);
                    padding: 24px;}
                                .container {
                                     width: 95%;
                                    margin: 0 auto;
                                    background-color: white;
                                    border-radius: 8px;
                                    box-sizing: border-box;
                                    font-family:'Product Sans', sans-serif;

                                }

                                .header img {
                                    width: 100%;
                                    border-top-left-radius: 8px;
                                    border-top-right-radius: 8px;
                                }
                                .body {
                                    padding: 40px;
                                    box-sizing: border-box;
                                     font-family:'Product Sans', sans-serif;
                                }
                                .footer-message {
                                   margin-left: auto;
                                margin-right: auto;
                                margin-top: 16px;
                                width: 95%;
                                    background-color: white;
                                    padding: 24px 40px;
                                    border-radius: 8px;
                                    box-sizing: border-box;
                                }
                %STYLES
                                    </style>
                                </head>
                                <body>
                                <table class="container" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td class="header">
                                            <img class="banner" src="https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.jpg" alt="Bienvenido"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="body">
                                            %BODY
                                        </td>
                                    </tr>
                                    <tr>

                                    </tr>
                                </table>
                                    %FOOTER
                                </body>
                                </html>

                                                """;
    }

    private String footer() {
        return """
                <div class="footer-message">
                    <h3>¿Necesitas ayuda?</h3>
                    <p>Envia tus comentarios e información de errores a <a href="mailto:informesyreservas@cieneguilladelrio.com">informesyreservas@cieneguilladelrio.com</a></p>
                  </div>
                </div>
                       """;
    }

}
