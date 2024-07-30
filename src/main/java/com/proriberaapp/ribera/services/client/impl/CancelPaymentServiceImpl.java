package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.CancelPaymentService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
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

    public CancelPaymentServiceImpl(CancelPaymentRepository cancelPaymentRepository,
                                    PaymentBookRepository paymentBookRepository,
                                    UserClientRepository userClientRepository,
                                    BookingRepository bookingRepository,
                                    EmailService emailService) {
        this.cancelPaymentRepository = cancelPaymentRepository;
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
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

    /* ANTES
    @Override
    public Mono<CancelPaymentEntity> saveCancelPayment(CancelPaymentEntity cancelPayment) {
        return cancelPaymentRepository.save(cancelPayment)
                .flatMap(savedCancelPayment -> paymentBookRepository.findById(savedCancelPayment.getPaymentBookId())
                        .flatMap(paymentBook -> {
                            paymentBook.setCancelReasonId(savedCancelPayment.getCancelReasonId());
                            return paymentBookRepository.save(paymentBook);
                        })
                        .then(paymentBookRepository.findUserClientIdByPaymentBookId(savedCancelPayment.getPaymentBookId())
                                .flatMap(userClientId -> userClientRepository.findById(userClientId)
                                        .flatMap(userClient -> {
                                            String emailBody = generatePaymentRejectionEmailBody(userClient, savedCancelPayment);
                                            return emailService.sendEmail(userClient.getEmail(), "Rechazo de Pago", emailBody)
                                                    .thenReturn(savedCancelPayment);
                                        })
                                )
                        )
                );
    }
     */

    @Override
    public Mono<CancelPaymentEntity> saveCancelPayment(CancelPaymentEntity cancelPayment) {
        return cancelPaymentRepository.save(cancelPayment)
                .flatMap(savedCancelPayment -> paymentBookRepository.findById(savedCancelPayment.getPaymentBookId())
                        .flatMap(paymentBook -> {
                            paymentBook.setCancelReasonId(savedCancelPayment.getCancelReasonId());
                            return paymentBookRepository.save(paymentBook)
                                    .flatMap(savedPaymentBook -> {
                                        Integer bookingId = savedPaymentBook.getBookingId();
                                        return bookingRepository.findByBookingId(bookingId)
                                                .flatMap(booking -> {
                                                    if (booking.getBookingStateId() == 3) {
                                                        booking.setBookingStateId(2);
                                                        return bookingRepository.save(booking);
                                                    }
                                                    return Mono.just(booking);
                                                });
                                    });
                        })
                        .then(paymentBookRepository.findUserClientIdByPaymentBookId(savedCancelPayment.getPaymentBookId())
                                .flatMap(userClientId -> userClientRepository.findById(userClientId)
                                        .flatMap(userClient -> {
                                            String emailBody = generatePaymentRejectionEmailBody(userClient, savedCancelPayment);
                                            return emailService.sendEmail(userClient.getEmail(), "Cancelacion de Pago", emailBody)
                                                    .thenReturn(savedCancelPayment);
                                        })
                                )
                        )
                );
    }

    @Override
    public Mono<Void> deleteCancelPayment(Integer id) {
        return cancelPaymentRepository.deleteById(id);
    }

    private String generatePaymentRejectionEmailBody(UserClientEntity userClient, CancelPaymentEntity cancelPayment) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Hola, " + userClient.getFirstName() + "!</h1>";
        body += "<h3 style='text-align: center;'>Lamentamos informarte que tu pago ha sido cancelado</h3>";
        body += "<p style='text-align: center;'>Razon de la cancelacion: " + cancelPayment.getDetail() + "</p>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Por favor, contacta con nuestro soporte para mas informacion.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div></center>";
        body += "</body></html>";

        return body;
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
                                            return emailService.sendEmail(userClient.getEmail(), "Confirmación de Pago Aceptado", emailBody);
                                        })
                                );
                    }
                    return Mono.empty();
                });
    }

    private String generatePaymentConfirmationEmailBody(UserClientEntity userClient) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Hola, " + userClient.getFirstName() + "!</h1>";
        body += "<h3 style='text-align: center;'>Su pago ha sido aceptado con exito</h3>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Gracias por tu pago. Si tienes alguna duda, por favor contacta con nuestro soporte.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div></center>";
        body += "</body></html>";

        return body;
    }
}
