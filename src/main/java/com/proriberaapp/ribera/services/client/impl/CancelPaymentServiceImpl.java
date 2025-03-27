package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.CancelPaymentService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import com.proriberaapp.ribera.utils.emails.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CancelPaymentServiceImpl implements CancelPaymentService {

    private final CancelPaymentRepository cancelPaymentRepository;
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final FullDayRepository fullDayRepository;
    private final FullDayDetailRepository fullDayDetailRepository;
    private final CompanionsRepository companionsRepository;

    public CancelPaymentServiceImpl(CancelPaymentRepository cancelPaymentRepository,
                                    PaymentBookRepository paymentBookRepository,
                                    UserClientRepository userClientRepository,
                                    BookingRepository bookingRepository,
                                    EmailService emailService, FullDayRepository fullDayRepository, FullDayDetailRepository fullDayDetailRepository, CompanionsRepository companionsRepository) {
        this.cancelPaymentRepository = cancelPaymentRepository;
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
        this.fullDayRepository = fullDayRepository;
        this.fullDayDetailRepository = fullDayDetailRepository;
        this.companionsRepository = companionsRepository;
    }

    @Override
    public Flux<CancelPaymentEntity> getAllCancelPayments() {
        return cancelPaymentRepository.findAll();
    }

    @Override
    public Flux<CancelEntity> getAllCancelReason() {
        return cancelPaymentRepository.findAllWhereCancelReasonIdNotEqualToOne();
    }

    @Override
    public Mono<CancelPaymentEntity> getCancelPaymentById(Integer id) {
        return cancelPaymentRepository.findById(id);
    }

    /*
     * ANTES
     * 
     * @Override
     * public Mono<CancelPaymentEntity> saveCancelPayment(CancelPaymentEntity
     * cancelPayment) {
     * return cancelPaymentRepository.save(cancelPayment)
     * .flatMap(savedCancelPayment ->
     * paymentBookRepository.findById(savedCancelPayment.getPaymentBookId())
     * .flatMap(paymentBook -> {
     * paymentBook.setCancelReasonId(savedCancelPayment.getCancelReasonId());
     * return paymentBookRepository.save(paymentBook);
     * })
     * .then(paymentBookRepository.findUserClientIdByPaymentBookId(
     * savedCancelPayment.getPaymentBookId())
     * .flatMap(userClientId -> userClientRepository.findById(userClientId)
     * .flatMap(userClient -> {
     * String emailBody = generatePaymentRejectionEmailBody(userClient,
     * savedCancelPayment);
     * return emailService.sendEmail(userClient.getEmail(), "Rechazo de Pago",
     * emailBody)
     * .thenReturn(savedCancelPayment);
     * })
     * )
     * )
     * );
     * }
     */

    /*
     * @Override
     * public Mono<CancelPaymentEntity> saveCancelPayment(CancelPaymentEntity
     * cancelPayment) {
     * return cancelPaymentRepository.save(cancelPayment)
     * .flatMap(savedCancelPayment ->
     * bookingRepository.findByBookingId(savedCancelPayment.getPaymentBookId()) //
     * Se busca el bookingId en vez de paymentBookId
     * .flatMap(booking -> {
     * if (booking.getBookingStateId() == 3) {
     * booking.setBookingStateId(2);
     * return bookingRepository.save(booking)
     * .flatMap(updatedBooking -> {
     * return paymentBookRepository.findById(savedCancelPayment.getPaymentBookId())
     * .flatMap(paymentBook -> {
     * paymentBook.setCancelReasonId(savedCancelPayment.getCancelReasonId());
     * return paymentBookRepository.save(paymentBook);
     * });
     * });
     * } else {
     * return Mono.just(booking);
     * }
     * })
     * .then(paymentBookRepository.findUserClientIdByPaymentBookId(
     * savedCancelPayment.getPaymentBookId())
     * .flatMap(userClientId -> userClientRepository.findById(userClientId)
     * .flatMap(userClient -> {
     * String emailBody = generatePaymentRejectionEmailBody(userClient,
     * savedCancelPayment);
     * return emailService.sendEmail(userClient.getEmail(), "Cancelación de Pago",
     * emailBody)
     * .thenReturn(savedCancelPayment);
     * })
     * )
     * )
     * );
     * }
     */

    /*
     * NO ELIMINA
     * 
     * @Override
     * public Mono<CancelPaymentEntity> saveCancelPayment(CancelPaymentEntity
     * cancelPayment) {
     * return cancelPaymentRepository.save(cancelPayment)
     * .flatMap(savedCancelPayment ->
     * bookingRepository.findByBookingId(savedCancelPayment.getPaymentBookId())
     * .flatMap(booking -> {
     * if (booking.getBookingStateId() == 3) {
     * booking.setBookingStateId(4);
     * return bookingRepository.save(booking)
     * .flatMap(updatedBooking -> {
     * return paymentBookRepository.findById(savedCancelPayment.getPaymentBookId())
     * .flatMap(paymentBook -> {
     * paymentBook.setCancelReasonId(savedCancelPayment.getCancelReasonId());
     * return paymentBookRepository.save(paymentBook)
     * .then(bookingRepository.deleteById(updatedBooking.getBookingId()))
     * .then(Mono.just(updatedBooking));
     * });
     * });
     * } else {
     * return Mono.just(booking);
     * }
     * })
     * .then(paymentBookRepository.findUserClientIdByPaymentBookId(
     * savedCancelPayment.getPaymentBookId())
     * .flatMap(userClientId -> userClientRepository.findById(userClientId)
     * .flatMap(userClient -> {
     * String emailBody = generatePaymentRejectionEmailBody(userClient,
     * savedCancelPayment);
     * return emailService.sendEmail(userClient.getEmail(), "Cancelación de Pago",
     * emailBody)
     * .thenReturn(savedCancelPayment);
     * })
     * )
     * )
     * );
     * }
     */

    @Override
    public Mono<Void> saveCancelPayment(CancelPaymentEntity cancelPayment) {
        /*
         * return cancelPaymentRepository.save(cancelPayment)
         * .flatMap(savedCancelPayment ->
         * bookingRepository.findByBookingId(savedCancelPayment.getPaymentBookId())
         * .flatMap(booking -> {
         * if (booking.getBookingStateId() == 3 || booking.getBookingStateId() == 4) {
         * booking.setBookingStateId(4);
         * return bookingRepository.save(booking)
         * .then(paymentBookRepository.findById(savedCancelPayment.getPaymentBookId())
         * .flatMap(paymentBook -> {
         * paymentBook.setCancelReasonId(savedCancelPayment.getCancelReasonId());
         * return paymentBookRepository.save(paymentBook)
         * .thenReturn(savedCancelPayment);
         * })
         * );
         * } else {
         * return Mono.just(savedCancelPayment);
         * }
         * })
         * )
         * .flatMap(savedCancelPayment ->
         * paymentBookRepository.findUserClientIdByPaymentBookId(savedCancelPayment.
         * getPaymentBookId())
         * .flatMap(userClientId -> userClientRepository.findById(userClientId)
         * .flatMap(userClient -> {
         * String emailBody = generatePaymentRejectionEmailBody(userClient,
         * savedCancelPayment);
         * return emailService.sendEmail(userClient.getEmail(), "Cancelación de Pago",
         * emailBody)
         * .thenReturn(savedCancelPayment);
         * })
         * )
         * );
         */
        return cancelPaymentRepository.save(cancelPayment)
                .flatMap(savedCancelPayment -> Mono
                        .zip(this.bookingRepository.updateStateToAnulated(cancelPayment.getPaymentBookId()),
                                this.paymentBookRepository.setCancelForBookingId(cancelPayment.getPaymentBookId(),
                                        cancelPayment.getCancelReasonId()))
                        .then(this.bookingRepository.findBookingEmailDtoByBookingId(cancelPayment.getPaymentBookId())))
                .flatMap(bookingEmailDto -> {
                    String emailBody = generatePaymentRejectionEmailBody(bookingEmailDto);
                    return emailService.sendEmail(bookingEmailDto.getClientEmail(), "Anulación de reserva", emailBody);

                });
    }

    @Override
    public Mono<Void> deleteCancelPayment(Integer id) {
        return cancelPaymentRepository.deleteById(id);
    }

    private String generatePaymentRejectionEmailBody(bookingRejectUserEmailDto bookingRejectUserEmailDto) {
        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
        AnulatedUserTemplateEmail rejectedPaymentTemplateEmail = new AnulatedUserTemplateEmail(
                bookingRejectUserEmailDto.getClientName(), bookingRejectUserEmailDto.getRoomName());
        baseEmailReserve.addEmailHandler(rejectedPaymentTemplateEmail);

        return baseEmailReserve.execute();
    }

    @Override
    public Mono<Void> updatePendingPayAndSendConfirmation(Integer paymentBookId) {
        return paymentBookRepository.findById(paymentBookId)
                .flatMap(paymentBook -> {
                    if (paymentBook.getPendingpay() == 0) {
                        paymentBook.setPendingpay(1);
                        return paymentBookRepository.save(paymentBook)
                                .then(userClientRepository.findById(paymentBook.getUserClientId())
                                        .flatMap(userClient -> {
                                            String emailBody = generatePaymentConfirmationEmailBody(userClient);
                                            return emailService.sendEmail(userClient.getEmail(),
                                                    "Confirmación de Pago Aceptado", emailBody);
                                        }));
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> cancelFullDay(Integer fullDayId) {
        return fullDayRepository.findByFulldayid(fullDayId)
                .flatMap(fullDay -> {
                    fullDay.setBookingstateid(4);
                    return fullDayRepository.save(fullDay)
                            .then(userClientRepository.findById(fullDay.getUserClientId())
                                    .flatMap(userClient -> {
                                        String recipientName = userClient.getFirstName();
                                        String recipientEmail = userClient.getEmail();
                                        String type = fullDay.getType();
                                        String body = EmailTemplateFullday.getRejectionTemplate(recipientName, type);
                                        return emailService.sendEmail(recipientEmail, "Anulación de Reserva", body);
                                    })
                            );
                })
                .then(fullDayDetailRepository.deleteByFulldayid(fullDayId))
                .then(companionsRepository.deleteByFulldayid(fullDayId))
                .then(Mono.defer(() -> {
                    CancelPaymentEntity cancelPayment = new CancelPaymentEntity();
                    cancelPayment.setPaymentBookId(fullDayId);
                    cancelPayment.setCancelReasonId(1);
                    cancelPayment.setDetail("cancelado");
                    return cancelPaymentRepository.save(cancelPayment);
                }))
                .then();
    }

    private String generatePaymentConfirmationEmailBody(UserClientEntity userClient) {
        String body = "<html>\n" +
                "<head>\n" +
                "    <title>Bienvenido</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: black;\n" +
                "            background-color: white; /* Color de fondo */\n" +
                "        }\n" +
                "        .header {\n" +
                "            width: 100%;\n" +
                "            position: relative;\n" +
                "            background-color: white; /* Color de fondo del encabezado */\n" +
                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n" +
                "        }\n" +
                "        .logos-right {\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            right: 10px;\n" +
                "            display: flex;\n" +
                "            gap: 5px;\n" +
                "        }\n" +
                "        .logos-right img {\n" +
                "            width: 30px;\n" +
                "            height: 30px;\n" +
                "        }\n" +
                "        .logo-left {\n" +
                "            width: 50px;\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            left: 10px;\n" +
                "        }\n" +
                "        .banner {\n" +
                "            width: 540px;\n" +
                "            border-top-left-radius: 20px;\n" +
                "            border-top-right-radius: 20px;\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 500px;\n" +
                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            border-bottom-left-radius: 10px;\n" +
                "            border-bottom-right-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: center;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .content h1 {\n" +
                "            margin-top: 20px;\n" +
                "            font-weight: bold;\n" +
                "            font-style: italic;\n" +
                "        }\n" +
                "        .content h3, .content p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .help-section {\n" +
                "            width: 500px;\n" +
                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <!-- Encabezado con logos -->\n" +
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Hola, "
                + userClient.getFirstName() + "!</h1>\n" +
                "            <h3 style='text-align: center;'>Su pago ha sido aceptado con éxito</h3>\n" +
                "            <center><div style='width: 100%'></div></center>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Sección de ayuda -->\n" +
                "    <div class=\"help-section\">\n" +
                "        <h3>Necesitas ayuda?</h3>\n" +
                "        <p>Comunicate con nosotros a traves de los siguientes medios:</p>\n" +
                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return body;
    }
}
