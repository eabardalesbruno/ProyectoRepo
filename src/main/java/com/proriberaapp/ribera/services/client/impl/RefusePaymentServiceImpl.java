package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.CurrencyTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RefusePaymentRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceItemRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceTypeRepsitory;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RefusePaymentServiceImpl implements RefusePaymentService {
        /*
         * private final RefusePaymentRepository refusePaymentRepository;
         * private final PaymentBookRepository paymentBookRepository;
         * 
         * public RefusePaymentServiceImpl(RefusePaymentRepository
         * refusePaymentRepository, PaymentBookRepository paymentBookRepository) {
         * this.refusePaymentRepository = refusePaymentRepository;
         * this.paymentBookRepository = paymentBookRepository;
         * }
         * 
         * @Override
         * public Flux<RefusePaymentEntity> getAllRefusePayments() {
         * return refusePaymentRepository.findAll();
         * }
         * 
         * @Override
         * public Flux<RefuseEntity> getAllRefuseReason() {
         * return refusePaymentRepository.findAllWhereRefuseReasonIdNotEqualToOne();
         * }
         * 
         * @Override
         * public Mono<RefusePaymentEntity> getRefusePaymentById(Integer id) {
         * return refusePaymentRepository.findById(id);
         * }
         * 
         * @Override
         * public Mono<RefusePaymentEntity> saveRefusePayment(RefusePaymentEntity
         * refusePayment) {
         * return refusePaymentRepository.save(refusePayment)
         * .flatMap(savedRefusePayment -> {
         * return paymentBookRepository.findById(savedRefusePayment.getPaymentBookId())
         * .flatMap(paymentBook -> {
         * paymentBook.setRefuseReasonId(savedRefusePayment.getRefuseReasonId());
         * return paymentBookRepository.save(paymentBook);
         * })
         * .then(Mono.just(savedRefusePayment));
         * });
         * }
         * 
         * @Override
         * public Mono<Void> deleteRefusePayment(Integer id) {
         * return refusePaymentRepository.deleteById(id);
         * }
         */
        private final RefusePaymentRepository refusePaymentRepository;
        private final PaymentBookRepository paymentBookRepository;
        private final UserClientRepository userClientRepository;

        private final BookingRepository bookingRepository;
        private final EmailService emailService;
        private final InvoiceServiceI invoiceService;

        public RefusePaymentServiceImpl(RefusePaymentRepository refusePaymentRepository,
                        PaymentBookRepository paymentBookRepository,
                        UserClientRepository userClientRepository,
                        BookingRepository bookingRepository, EmailService emailService,
                        InvoiceTypeRepsitory invoiceTypeRepsitory,
                        InvoiceRepository invoiceRepsitory,
                        CurrencyTypeRepository currencyTypeRepository,
                        InvoiceItemRepository invoiceItemRepository,
                        InvoiceServiceI invoiceService) {
                this.refusePaymentRepository = refusePaymentRepository;
                this.paymentBookRepository = paymentBookRepository;
                this.userClientRepository = userClientRepository;
                this.bookingRepository = bookingRepository;
                this.emailService = emailService;
                this.invoiceService = invoiceService;
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
                                .flatMap(savedRefusePayment -> paymentBookRepository
                                                .findById(savedRefusePayment.getPaymentBookId())
                                                .flatMap(paymentBook -> {
                                                        paymentBook.setRefuseReasonId(
                                                                        savedRefusePayment.getRefuseReasonId());
                                                        return bookingRepository
                                                                        .findByBookingId(paymentBook.getBookingId())
                                                                        .flatMap(booking -> {
                                                                                booking.setBookingStateId(3);
                                                                                return bookingRepository.save(booking);
                                                                        })
                                                                        .then(paymentBookRepository.save(paymentBook));
                                                })
                                                .then(paymentBookRepository
                                                                .findUserClientIdByPaymentBookId(
                                                                                savedRefusePayment.getPaymentBookId())
                                                                .flatMap(userClientId -> userClientRepository
                                                                                .findById(userClientId)
                                                                                .flatMap(userClient -> {
                                                                                        String emailBody = generatePaymentRejectionEmailBody(
                                                                                                        userClient,
                                                                                                        savedRefusePayment);
                                                                                        return emailService
                                                                                                        .sendEmail(userClient
                                                                                                                        .getEmail(),
                                                                                                                        "Rechazo de Pago",
                                                                                                                        emailBody)
                                                                                                        .thenReturn(savedRefusePayment);
                                                                                }))));
        }

        @Override
        public Mono<Void> deleteRefusePayment(Integer id) {
                return refusePaymentRepository.deleteById(id);
        }

        private String generatePaymentRejectionEmailBody(UserClientEntity userClient,
                        RefusePaymentEntity refusePayment) {
                String body = "<html>\n" +
                                "<head>\n" +
                                "    <title>Pago Rechazado</title>\n" +
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
                                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n"
                                +
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
                                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n"
                                +
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
                                "            <h3 style='text-align: center;'>Lamentamos informarte que tu pago ha sido rechazado</h3>\n"
                                +
                                "            <p style='text-align: center;'>Razon del rechazo: "
                                + refusePayment.getDetail() + "</p>\n"
                                +
                                "        </div>\n" +
                                "    </div>\n" +
                                "\n" +
                                "    <!-- Sección de ayuda -->\n" +
                                "    <div class=\"help-section\">\n" +
                                "        <h3>¿Necesitas ayuda?</h3>\n" +
                                "        <p>Comunicate con nosotros a través de los siguientes medios:</p>\n" +
                                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                                "    </div>\n" +
                                "</body>\n" +
                                "</html>";

                return body;
        }

        @Override
        public Mono updatePendingPayAndSendConfirmation(Integer paymentBookId) {
                return this.paymentBookRepository
                                .loadUserDataAndBookingData(paymentBookId)
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("El ID de pago no existe")))
                                .flatMap(paymenbook -> {
                                        if (paymenbook.getPendingpay() == 0) {
                                                paymenbook.setPaymentstateid(2);
                                                paymenbook.setPendingpay(1);
                                                String identifierClient = paymenbook.getUseridentifierclient() != null
                                                                ? paymenbook.getUseridentifierclient()
                                                                : "99999999";
                                                InvoiceClientDomain clientDomain = new InvoiceClientDomain(
                                                                paymenbook.getUsername(),
                                                                identifierClient,
                                                                paymenbook.getUseraddress(), paymenbook.getUserphone(),
                                                                paymenbook.getUseremail(),
                                                                paymenbook.getUserclientid());
                                                InvoiceCurrency invoiceCurrency = InvoiceCurrency
                                                                .getInvoiceCurrencyByCurrency(
                                                                                paymenbook.getCurrencytypename());
                                                InvoiceDomain invoiceDomain = new InvoiceDomain(
                                                                clientDomain,
                                                                paymenbook.getPaymentbookid(), 18, invoiceCurrency,
                                                                InvoiceType.BOLETA, new BigDecimal(0.0));
                                                invoiceDomain.addItemWithIncludedIgv(new InvoiceItemDomain(
                                                                paymenbook.getRoomname(),
                                                                paymenbook.getRoomname(), 1,
                                                                paymenbook.getTotalCost()));
                                                invoiceDomain.calculatedTotals();
                                                UserClientEntity userClientEntity = UserClientEntity.builder()
                                                                .userClientId(paymenbook.getUserclientid())
                                                                .firstName(paymenbook.getUsername()).build();
                                                String emailBody = this.generatePaymentConfirmationEmailBody(
                                                                userClientEntity);
                                                return this.invoiceService.save(invoiceDomain)
                                                                .then(Mono.zip(paymentBookRepository
                                                                                .confirmPayment(paymentBookId),
                                                                                this.emailService.sendEmail(
                                                                                                paymenbook.getUseremail(),
                                                                                                "Confirmación de Pago Aceptado",
                                                                                                emailBody)));

                                        }
                                        return Mono.empty();
                                });
                /* return paymentBookRepository.findById(paymentBookId) */
                /*
                 * .switchIfEmpty(Mono.error(new
                 * IllegalArgumentException("El ID de pago no existe")))
                 */
                /* .flatMap(paymentBook -> { */
                /* if (paymentBook.getPendingpay() == 0) { */
                /* paymentBook.setPaymentStateId(2); */
                /* paymentBook.setPendingpay(1); */
                /* return paymentBookRepository.save(paymentBook) */
                /* .then(userClientRepository */
                /* .findById(paymentBook.getUserClientId()) */
                /* .flatMap(userClient -> { */
                /* String emailBody = generatePaymentConfirmationEmailBody( */
                /* userClient); */
                /* return emailService.sendEmail( */
                /* userClient.getEmail(), */
                /* "Confirmación de Pago Aceptado", */
                /* emailBody); */
                /* })); */
                /* } */
                /* return Mono.empty(); */
                /* }); */
        }

        private String generatePaymentConfirmationEmailBody(UserClientEntity userClient) {
                String body = "<html>\n" +
                                "<head>\n" +
                                "    <title>Pago Aceptado</title>\n" +
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
                                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n"
                                +
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
                                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n"
                                +
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
                                "            <h3 style='text-align: center;'>Su pago ha sido aceptado con éxito</h3>\n"
                                +
                                "        </div>\n" +
                                "    </div>\n" +
                                "\n" +
                                "    <!-- Sección de ayuda -->\n" +
                                "    <div class=\"help-section\">\n" +
                                "        <h3>¿Necesitas ayuda?</h3>\n" +
                                "        <p>Comunicate con nosotros a través de los siguientes medios:</p>\n" +
                                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                                "    </div>\n" +
                                "</body>\n" +
                                "</html>";

                return body;
        }
}