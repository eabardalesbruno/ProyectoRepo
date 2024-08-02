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
        String body = "<html><head><title>Pago Cancelado</title></head><body style='color:black'>";
        body += "<div style='width: 100%;'>";
        body += "<div style='display: flex; align-items: center; justify-content: space-between;'>";
        body += "<img style='width: 100px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453907774_2238863976459404_4409148998166454890_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=1p66qBQN6IYQ7kNvgEnxiv2&_nc_ht=scontent.flim1-2.fna&oh=00_AYACRHyTnMSMkClEmGFw8OmSBT2T_U4LGusY0F3KX0OBVQ&oe=66B1E966' alt='Logo Izquierda'>";
        body += "<div style='display: flex;'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453503393_2238863839792751_3678586622785113323_n.jpg?stp=cp0_dst-jpg&_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OMKWsE877hcQ7kNvgHnzNGq&_nc_ht=scontent.flim1-2.fna&oh=00_AYBSmgM6SVV33fWdVeqn9sUMleFSdtOGZPcc0m-USS93bg&oe=66B20925' alt='Logo 1'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453501437_2238863739792761_5553627034492335729_n.jpg?stp=cp0_dst-jpg&_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=fcEltLDDNeMQ7kNvgFNAsL6&_nc_ht=scontent.flim1-2.fna&oh=00_AYBD75zTjdsLuKmtk3vPYR7fBfCg5U2aVQ_tYm8679ZFCQ&oe=66B1FF76' alt='Logo 2'>";
        body += "<img style='width: 80px;' src='https://scontent.flim1-1.fna.fbcdn.net/v/t39.30808-6/453497633_2238863526459449_291281439279005519_n.jpg?stp=cp0_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=vMzblHxFzGUQ7kNvgHhI3YO&_nc_ht=scontent.flim1-1.fna&oh=00_AYAEn_ThdeZSWqvo7RurNrnoAulbgxM7V5YzJc_CGsYACg&oe=66B1E905' alt='Logo 3'>";
        body += "</div>";
        body += "</div>";
        body += "<img style='width: 100%; margin-top: 20px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453514514_2238864093126059_4377276491425541120_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=r0fzgelec-UQ7kNvgFL0EDI&_nc_ht=scontent.flim1-2.fna&oh=00_AYAJLos7io5zNmz08RwyK1pc5ZGwN5Cn8jt8Eg17N73CQQ&oe=66B1E807' alt='Pago Cancelado'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Hola, " + userClient.getFirstName() + "!</h1>";
        body += "<h3 style='text-align: center;'>Lamentamos informarte que tu pago ha sido cancelado</h3>";
        body += "<p style='text-align: center;'>Razon de la cancelacion: " + cancelPayment.getDetail() + "</p>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Por favor, contacta con nuestro soporte para más información.</center>";
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
