package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RefusePaymentRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RefusePaymentServiceImpl implements RefusePaymentService {
/*
    private final RefusePaymentRepository refusePaymentRepository;
    private final PaymentBookRepository paymentBookRepository;

    public RefusePaymentServiceImpl(RefusePaymentRepository refusePaymentRepository, PaymentBookRepository paymentBookRepository) {
        this.refusePaymentRepository = refusePaymentRepository;
        this.paymentBookRepository = paymentBookRepository;
    }

    @Override
    public Flux<RefusePaymentEntity> getAllRefusePayments() {
        return refusePaymentRepository.findAll();
    }

    @Override
    public Flux<RefuseEntity> getAllRefuseReason() {
        return refusePaymentRepository.findAllWhereRefuseReasonIdNotEqualToOne();
    }

    @Override
    public Mono<RefusePaymentEntity> getRefusePaymentById(Integer id) {
        return refusePaymentRepository.findById(id);
    }

    @Override
    public Mono<RefusePaymentEntity> saveRefusePayment(RefusePaymentEntity refusePayment) {
        return refusePaymentRepository.save(refusePayment)
                .flatMap(savedRefusePayment -> {
                    return paymentBookRepository.findById(savedRefusePayment.getPaymentBookId())
                            .flatMap(paymentBook -> {
                                paymentBook.setRefuseReasonId(savedRefusePayment.getRefuseReasonId());
                                return paymentBookRepository.save(paymentBook);
                            })
                            .then(Mono.just(savedRefusePayment));
                });
    }

    @Override
    public Mono<Void> deleteRefusePayment(Integer id) {
        return refusePaymentRepository.deleteById(id);
    }
 */
private final RefusePaymentRepository refusePaymentRepository;
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final EmailService emailService;

    public RefusePaymentServiceImpl(RefusePaymentRepository refusePaymentRepository,
                                    PaymentBookRepository paymentBookRepository,
                                    UserClientRepository userClientRepository,
                                    EmailService emailService) {
        this.refusePaymentRepository = refusePaymentRepository;
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.emailService = emailService;
    }

    @Override
    public Flux<RefusePaymentEntity> getAllRefusePayments() {
        return refusePaymentRepository.findAll();
    }

    @Override
    public Flux<RefuseEntity> getAllRefuseReason() {
        return refusePaymentRepository.findAllWhereRefuseReasonIdNotEqualToOne();
    }

    @Override
    public Mono<RefusePaymentEntity> getRefusePaymentById(Integer id) {
        return refusePaymentRepository.findById(id);
    }

    @Override
    public Mono<RefusePaymentEntity> saveRefusePayment(RefusePaymentEntity refusePayment) {
        return refusePaymentRepository.save(refusePayment)
                .flatMap(savedRefusePayment -> paymentBookRepository.findById(savedRefusePayment.getPaymentBookId())
                        .flatMap(paymentBook -> {
                            paymentBook.setRefuseReasonId(savedRefusePayment.getRefuseReasonId());
                            return paymentBookRepository.save(paymentBook);
                        })
                        .then(paymentBookRepository.findUserClientIdByPaymentBookId(savedRefusePayment.getPaymentBookId())
                                .flatMap(userClientId -> userClientRepository.findById(userClientId)
                                        .flatMap(userClient -> {
                                            String emailBody = generatePaymentRejectionEmailBody(userClient, savedRefusePayment);
                                            return emailService.sendEmail(userClient.getEmail(), "Rechazo de Pago", emailBody)
                                                    .thenReturn(savedRefusePayment);
                                        })
                                )
                        )
                );
    }

    @Override
    public Mono<Void> deleteRefusePayment(Integer id) {
        return refusePaymentRepository.deleteById(id);
    }

    private String generatePaymentRejectionEmailBody(UserClientEntity userClient, RefusePaymentEntity refusePayment) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Hola, " + userClient.getFirstName() + "!</h1>";
        body += "<h3 style='text-align: center;'>Lamentamos informarte que tu pago ha sido rechazado</h3>";
        body += "<p style='text-align: center;'>Razón del rechazo: " + refusePayment.getRefuseReasonId() + "</p>";
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
}