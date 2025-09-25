package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.services.client.ComplaintsBookService;
import com.proriberaapp.ribera.services.client.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.proriberaapp.ribera.Infraestructure.repository.ComplaintsBookRepository;
import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ComplaintsBookServiceImpl implements ComplaintsBookService {
    private final ComplaintsBookRepository complaintsBookRepository;
    private final EmailService emailService;

    @Value("${mail_first_claim_book}")
    private String mailFisrtClaimBook;

    @Value("${mail_second_claim_book}")
    private String mailSecondClaimBook;

    @Autowired
    public ComplaintsBookServiceImpl(ComplaintsBookRepository complaintsBookRepository, EmailService emailService) {
        this.complaintsBookRepository = complaintsBookRepository;
        this.emailService = emailService;
    }

    @Override
    public Mono<ComplaintsBookEntity> createComplaint(ComplaintsBookEntity complaint) {
        // Establecer la fecha actual antes de guardar
        complaint.setDateSaved(LocalDateTime.now());

        return complaintsBookRepository.save(complaint)
                .flatMap(savedComplaint -> {
                    String subject = "Nuevo Reclamo Recibido";
                    String body = generateEmailBody(savedComplaint);
                    String userEmail = savedComplaint.getEmail();

                    // Enviar el correo al destinatario principal y una copia al email del usuario
                    return emailService.sendEmailCC(userEmail, mailFisrtClaimBook, mailSecondClaimBook, subject, body)
                            .onErrorResume(e -> {
                                // Log error and continue
                                System.err.println("Error sending email to primary recipient: " + e.getMessage());
                                return Mono.empty(); // Continue without stopping the flow
                            })
                            .thenReturn(savedComplaint);
                });
    }

    /*
    @Override
    public Mono<ComplaintsBookEntity> createComplaint(ComplaintsBookEntity complaint) {
        // Establecer la fecha actual antes de guardar
        complaint.setDateSaved(LocalDateTime.now());

        return complaintsBookRepository.save(complaint)
                .flatMap(savedComplaint -> {
                    String subject = "Nuevo Reclamo Recibido";
                    String body = generateEmailBody(savedComplaint);
                    String primaryRecipient = "notificacionesribera@inresorts.club";
                    String userEmail = savedComplaint.getEmail();

                    // Enviar el correo al destinatario principal y una copia al email del usuario
                    return emailService.sendEmail(primaryRecipient, subject, body)
                            .then(emailService.sendEmail(userEmail, subject, body))
                            .thenReturn(savedComplaint);
                });
    }
     */

    /*
    private String generateEmailBody(ComplaintsBookEntity complaint) {
        return "<html>\n" +
                "<head>\n" +
                "    <title>Reclamo Recibido</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: black;\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content h1 {\n" +
                "            margin: 20px 0;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div>\n" +
                "        <div>\n" +
                "            <h1>Nuevo Reclamo Recibido</h1>\n" +
                "            <p><strong>Tipo de Persona:</strong> " + complaint.getPersonType() + "</p>\n" +
                "            <p><strong>Nombre de Negocio:</strong> " + complaint.getBusinessName() + "</p>\n" +
                "            <p><strong>RUC:</strong> " + complaint.getRuc() + "</p>\n" +
                "            <p><strong>Nombre:</strong> " + complaint.getFirstName() + "</p>\n" +
                "            <p><strong>Apellido:</strong> " + complaint.getLastName() + "</p>\n" +
                "            <p><strong>Telefono:</strong> " + complaint.getPhone() + "</p>\n" +
                "            <p><strong>Email:</strong> " + complaint.getEmail() + "</p>\n" +
                "            <p><strong>Adulto:</strong> " + (complaint.getIsAdult() ? "Si" : "No") + "</p>\n" +
                "            <p><strong>Direccion:</strong> " + complaint.getAddress() + "</p>\n" +
                "            <p><strong>Acepto terminos:</strong> " + (complaint.getAcceptedTerms() ? "Si" : "No") + "</p>\n" +
                "            <p><strong>Reclamo:</strong> " + complaint.getComplaintsDescription() + "</p>\n" +
                "            <p><strong>Solicitud del reclamo:</strong> " + complaint.getAskingDescription() + "</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
     */
/*
    private String generateEmailBody(ComplaintsBookEntity complaint) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Nuevo Reclamo</title>\n" +
                "    <style>\n" +
                "      html, body { background: #f8f8fc; }\n" +
                "      .container { }\n" +
                "      .header-social { margin: 0 196px; padding: 40px 48px; display: flex; justify-content: space-between; }\n" +
                "      .red-social { display: flex; justify-content: center; align-items: center; gap: 16px; }\n" +
                "      .container-body { margin: 21px 186px 0 186px; }\n" +
                "      .bg-custom { display: flex; justify-content: center; flex-direction: column; }\n" +
                "      .custom-img { border-radius: 8px 8px 0 0; }\n" +
                "      .custom-body { background: #ffffff; margin-bottom: 16px; }\n" +
                "      .title { margin: 40px 40px 25px 40px; }\n" +
                "      .title-complains { font-family: 'Product Sans', sans-serif; font-size: 20px; font-weight: 700; line-height: 30px; text-align: left; color: #121a26; }\n" +
                "      .description { display: flex; flex-direction: column; color: #384860; font-family: 'Product Sans', sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; text-align: left; margin: 0 40px; }\n" +
                "      .description-complains .custom-apart { font-family: 'Product Sans', sans-serif; font-size: 16px; font-style: italic; font-weight: 700; line-height: 24px; text-align: left; }\n" +
                "      .custom-detail { margin: 24px 40px; display: flex; gap: 24px; }\n" +
                "      .data-complains { margin-bottom: 8px; font-family: 'Poppins', sans-serif; font-size: 16px; font-weight: 600; line-height: 24px; text-align: left; color: #1e1e1e; }\n" +
                "      .custom-detail-data { background: #f9f9f9; padding: 24px; font-family: 'Poppins', sans-serif; line-height: 21px; text-align: left; font-size: 14px; flex: 0 0 calc(33.33% - 24px); }\n" +
                "      .custom-detail-complains { background: #f9f9f9; padding: 24px; font-family: 'Poppins', sans-serif; line-height: 21px; text-align: left; font-size: 14px; flex: 0 0 calc(60.33% - 24px); }\n" +
                "      .detail { display: flex; flex-direction: column; margin-bottom: 8px; }\n" +
                "      .data { font-family: 'Poppins', sans-serif; font-size: 14px; font-weight: 400; line-height: 21px; text-align: left; color: #1e1e1e; }\n" +
                "      .data-detail { font-family: 'Poppins', sans-serif; font-size: 14px; font-weight: 600; line-height: 21px; text-align: left; color: #1e1e1e; }\n" +
                "      .custom-footer-help { background: #ffffff; margin-bottom: 16px; padding: 24px 40px; }\n" +
                "      .custom-help { font-family: 'Product Sans', sans-serif; font-size: 16px; font-weight: 700; line-height: 24px; text-align: left; }\n" +
                "      .custom-footer-info { margin: 0 196px; padding: 40px 48px; display: flex; font-family: 'Product Sans', sans-serif; font-size: 14px; font-weight: 400; line-height: 22.4px; color: #9D9D9D; }\n" +
                "      .custom-info { display: flex; flex-direction: column; }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <div class=\"header-social\">\n" +
                "        <img src=\"https://bit.ly/3WORXbG\" alt=\"logo\" />\n" +
                "        <div class=\"red-social\">\n" +
                "          <a href=\"https://www.facebook.com/RiberaDelRioClubResort/?locale=es_LA\" target=\"_blank\">\n" +
                "            <img src=\"https://bit.ly/3yHM4Fk\" alt=\"Facebook\" />\n" +
                "          </a>\n" +
                "          <a href=\"https://www.instagram.com/riberadelrioclubresort6/\" target=\"_blank\">\n" +
                "            <img src=\"https://bit.ly/3SPkLQ5\" alt=\"Instagram\" />\n" +
                "          </a>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div class=\"container-body\">\n" +
                "        <div class=\"bg-custom\">\n" +
                "          <img src=\"https://bit.ly/3WMBi8T\" alt=\"Ribera\" class=\"custom-img\" />\n" +
                "          <div class=\"custom-body\">\n" +
                "            <div class=\"title\">\n" +
                "              <p class=\"title-complains\">Nuevo Reclamo recibido</p>\n" +
                "            </div>\n" +
                "            <div class=\"description\">\n" +
                "              <span class=\"description-name\">Estimado(a), " + complaint.getFirstName() + ".</span>\n" +
                "              <p class=\"description-complains\">\n" +
                "                El presente es para informar que se registro exitosamente el reclamo de su reserva\n" +
                "                Nos contactamos por este medio o por nuestro canal de Whatsapp para comunicarle dicha solución.\n" +
                "              </p>\n" +
                "            </div>\n" +
                "            <div class=\"custom-detail\">\n" +
                "              <div class=\"custom-detail-data\">\n" +
                "                <div class=\"data-complains\">\n" +
                "                  <span>Datos del consumidor reclamante</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Nombre:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getFirstName() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Apellido:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getLastName() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Dirección:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getAddress() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Celular o Teléfono:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getPhone() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">N° de documento de identidad:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getRuc() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Tipo de cliente:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getPersonType() + "</span>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "              <div class=\"custom-detail-complains\">\n" +
                "                <div class=\"data-complains\">\n" +
                "                  <span>Datos del reclamo</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Tipo de Reclamo:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getPersonType() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Fecha del incidente:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getDateSaved() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Descripción del Reclamo:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getComplaintsDescription() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Pedido del Cliente:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getAskingDescription() + "</span>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "            <div class=\"custom-footer-help\">\n" +
                "              <span class=\"custom-help\">\n" +
                "                Nuestro equipo se contactará a la brevedad posible para ofrecer una solución.\n" +
                "              </span>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div class=\"custom-footer-info\">\n" +
                "        <span class=\"custom-info\">\n" +
                "          Este correo ha sido enviado a " + complaint.getEmail() + ".<br />\n" +
                "          Si no solicitó este mensaje, por favor ignore este correo.\n" +
                "        </span>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
    }*/
    private String generateEmailBody(ComplaintsBookEntity complaint) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Nuevo Reclamo</title>\n" +
                "    <style>\n" +
                "      body { background: #f8f8fc; margin: 0; padding: 0; }\n" +
                "      .container { width: 100%; max-width: 600px; margin: 0 auto; }\n" +
                "      .header-social { padding: 40px 48px; display: flex; justify-content: space-between; align-items: center; }\n" +
                "      .red-social { display: flex; align-items: center; gap: 16px; }\n" +
                "      .container-body { margin: 21px auto; padding: 0 16px; }\n" +
                "      .bg-custom { background: #ffffff; border-radius: 8px; overflow: hidden; }\n" +
                "      .custom-img { width: 100%; display: block; }\n" +
                "      .custom-body { padding: 40px; }\n" +
                "      .title-complains { font-family: 'Product Sans', sans-serif; font-size: 20px; font-weight: 700; line-height: 30px; color: #121a26; margin-bottom: 25px; }\n" +
                "      .description { color: #384860; font-family: 'Product Sans', sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; text-align: left; margin-bottom: 24px; }\n" +
                "      .description-complains .custom-apart { font-style: italic; font-weight: 700; }\n" +
                "      .custom-detail { margin-bottom: 24px; display: flex; flex-wrap: wrap; }\n" +
                "      .data-complains { margin-bottom: 8px; font-family: 'Poppins', sans-serif; font-size: 16px; font-weight: 600; line-height: 24px; color: #1e1e1e; }\n" +
                "      .custom-detail-data, .custom-detail-complains { background: #f9f9f9; padding: 24px; font-family: 'Poppins', sans-serif; line-height: 21px; font-size: 14px; flex: 1; }\n" +
                "      .detail { margin-bottom: 8px; }\n" +
                "      .data { font-family: 'Poppins', sans-serif; font-size: 14px; font-weight: 400; line-height: 21px; color: #1e1e1e; }\n" +
                "      .data-detail { font-weight: 600; }\n" +
                "      .custom-footer-help { background: #ffffff; padding: 24px 40px; text-align: left; }\n" +
                "      .custom-help { font-family: 'Product Sans', sans-serif; font-size: 16px; font-weight: 700; line-height: 24px; }\n" +
                "      .custom-footer-info { padding: 40px 48px; font-family: 'Product Sans', sans-serif; font-size: 14px; line-height: 22.4px; color: #9D9D9D; text-align: center; }\n" +
                "      .custom-footer-info a { color: #9D9D9D; text-decoration: none; }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <div class=\"header-social\">\n" +
                "        <img src=\"https://bit.ly/3WORXbG\" alt=\"logo\" />\n" +
                "        <div class=\"red-social\">\n" +
                "          <a href=\"https://www.facebook.com/RiberaDelRioClubResort/?locale=es_LA\" target=\"_blank\">\n" +
                "            <img src=\"https://bit.ly/3yHM4Fk\" alt=\"Facebook\" />\n" +
                "          </a>\n" +
                "          <a href=\"https://www.instagram.com/riberadelrioclubresort6/\" target=\"_blank\">\n" +
                "            <img src=\"https://bit.ly/3SPkLQ5\" alt=\"Instagram\" />\n" +
                "          </a>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div class=\"container-body\">\n" +
                "        <div class=\"bg-custom\">\n" +
                "          <img src=\"https://bit.ly/3WMBi8T\" alt=\"Ribera\" class=\"custom-img\" />\n" +
                "          <div class=\"custom-body\">\n" +
                "            <div class=\"title\">\n" +
                "              <p class=\"title-complains\">Nuevo Reclamo recibido</p>\n" +
                "            </div>\n" +
                "            <div class=\"description\">\n" +
                "              <span class=\"description-name\">Estimado(a), " + complaint.getFirstName() + ".</span>\n" +
                "              <p class=\"description-complains\">\n" +
                "                El presente es para informar que se registro exitosamente el reclamo de su reserva \n" +
                "                Nos contactamos por este medio o por nuestro canal de Whatsapp para comunicarle dicha solucion.\n" +
                "              </p>\n" +
                "            </div>\n" +
                "            <div class=\"custom-detail\">\n" +
                "              <div class=\"custom-detail-data\">\n" +
                "                <div class=\"data-complains\">\n" +
                "                  <span>Datos del consumidor reclamante</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Nombre:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getFirstName() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Apellido:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getLastName() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Dirección:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getAddress() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Celular o Telefono:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getPhone() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Correo Electrónico:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getEmail() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Numero de documento de identidad:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getRuc() + "</span>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "              <div class=\"custom-detail-complains\">\n" +
                "                <div class=\"data-complains\">\n" +
                "                  <span>Detalle del reclamo o queja</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Fecha de incidente:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getDateSaved().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Descripcion del Reclamo:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getComplaintsDescription() + "</span>\n" +
                "                </div>\n" +
                "                <div class=\"detail\">\n" +
                "                  <span class=\"data\">Pedido del Cliente:</span>\n" +
                "                  <span class=\"data-detail\">" + complaint.getAskingDescription() + "</span>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"custom-footer-help\">\n" +
                "          <p class=\"custom-help\">Ayuda y Soporte</p>\n" +
                "          <p>Para cualquier consulta, por favor contactarse con nuestro equipo de atencion al cliente a traves de contacto@riberadelrio.com</p>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div class=\"custom-footer-info\">\n" +
                "        <p>\n" +
                "          Recibio este mensaje porque usted o alguien con acceso a esta direccion de correo electronico se inscribio en la lista de contactos de Ribera del Rio.\n" +
                "          <br />\n" +
                "          <a href=\"#\">Cancelar suscripcion</a>\n" +
                "        </p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
    }
}
