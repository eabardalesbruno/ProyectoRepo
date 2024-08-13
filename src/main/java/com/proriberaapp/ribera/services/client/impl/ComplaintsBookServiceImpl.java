package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.services.client.ComplaintsBookService;
import com.proriberaapp.ribera.services.client.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proriberaapp.ribera.Infraestructure.repository.ComplaintsBookRepository;
import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ComplaintsBookServiceImpl implements ComplaintsBookService {
    private final ComplaintsBookRepository complaintsBookRepository;
    private final EmailService emailService;

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
                    String primaryRecipient = "reclamosriberadelrio@inresorts.club";
                    String userEmail = savedComplaint.getEmail();

                    // Enviar el correo al destinatario principal y una copia al email del usuario
                    return emailService.sendEmail(primaryRecipient, subject, body)
                            .then(emailService.sendEmail(userEmail, subject, body))
                            .thenReturn(savedComplaint);
                });
    }

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

}
