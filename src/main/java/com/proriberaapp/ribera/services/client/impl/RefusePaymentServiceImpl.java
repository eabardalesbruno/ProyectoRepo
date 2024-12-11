package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceItemRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceTypeRepsitory;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;
import com.proriberaapp.ribera.utils.TransformDate;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.BookingEmailDto;
import com.proriberaapp.ribera.utils.emails.ConfirmPaymentByBankTransferAndCardTemplateEmail;
import com.proriberaapp.ribera.utils.emails.RejectedPaymentTemplateEmail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RefusePaymentServiceImpl implements RefusePaymentService {
        private final RefusePaymentRepository refusePaymentRepository;
        private final PaymentBookRepository paymentBookRepository;
        private final UserClientRepository userClientRepository;
        private final RoomOfferRepository roomOfferRepository;
        private final RoomRepository roomRepository;
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
                        InvoiceServiceI invoiceService,
                        RoomOfferRepository roomOfferRepository,
                        RoomRepository roomRepository) {
                this.refusePaymentRepository = refusePaymentRepository;
                this.paymentBookRepository = paymentBookRepository;
                this.userClientRepository = userClientRepository;
                this.bookingRepository = bookingRepository;
                this.emailService = emailService;
                this.invoiceService = invoiceService;
                this.roomOfferRepository = roomOfferRepository;
                this.roomRepository = roomRepository;
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
                                                                                .flatMap(userClient -> generatePaymentRejectionEmailBody(
                                                                                                userClient,
                                                                                                savedRefusePayment)
                                                                                                .flatMap(emailBody -> emailService
                                                                                                                .sendEmail(userClient
                                                                                                                                .getEmail(),
                                                                                                                                "Pago rechazado para la reserva ",
                                                                                                                                emailBody)
                                                                                                                .thenReturn(savedRefusePayment))))));
        }

        @Override
        public Mono<Void> deleteRefusePayment(Integer id) {
                return refusePaymentRepository.deleteById(id);
        }

        private Mono<String> generatePaymentRejectionEmailBody(UserClientEntity userClient,
                        RefusePaymentEntity refusePayment) {
                return getPaymentDetails(refusePayment.getPaymentBookId())
                                .map(paymentDetails -> {
                                        // Extrae los valores del Map
                                        String roomName = (String) paymentDetails.get("RoomName");
                                        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
                                        RejectedPaymentTemplateEmail rejectedPaymentTemplateEmail = new RejectedPaymentTemplateEmail(
                                                        userClient.getFirstName(), refusePayment.getDetail(), roomName);
                                        baseEmailReserve.addEmailHandler(rejectedPaymentTemplateEmail);
                                        return baseEmailReserve.execute();
                                });
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
                                                InvoiceClientDomain clientDomain = new InvoiceClientDomain(
                                                                paymenbook.getUsername(),
                                                                paymenbook.getInvoicedocumentnumber(),
                                                                paymenbook.getUseraddress(), paymenbook.getUserphone(),
                                                                paymenbook.getUseremail(),
                                                                paymenbook.getUserclientid());
                                                InvoiceCurrency invoiceCurrency = InvoiceCurrency
                                                                .getInvoiceCurrencyByCurrency(
                                                                                paymenbook.getCurrencytypename());
                                                InvoiceType type = InvoiceType.getInvoiceTypeByName(
                                                                paymenbook.getInvoicetype().toUpperCase());
                                                InvoiceDomain invoiceDomain = new InvoiceDomain(
                                                                clientDomain,
                                                                paymenbook.getPaymentbookid(), 18, invoiceCurrency,
                                                                type, paymenbook.getPercentagediscount());
                                                invoiceDomain.setOperationCode(paymenbook.getOperationcode());
                                                invoiceDomain.addItemWithIncludedIgv(new InvoiceItemDomain(
                                                                paymenbook.getRoomname(),
                                                                paymenbook.getRoomname(), 1,
                                                                BigDecimal.valueOf(paymenbook
                                                                                .getTotalcostwithoutdiscount())));
                                                invoiceDomain.calculatedTotals();
                                                return this.generatePaymentConfirmationEmailBody(paymentBookId)
                                                                .flatMap(emailBody -> this.invoiceService
                                                                                .save(invoiceDomain)
                                                                                .then(Mono.zip(
                                                                                                paymentBookRepository
                                                                                                                .confirmPayment(paymentBookId),
                                                                                                this.emailService
                                                                                                                .sendEmail(
                                                                                                                                paymenbook.getUseremail(),
                                                                                                                                "Confirmación de Pago Aceptado",
                                                                                                                                emailBody))));

                                        }
                                        return Mono.empty();
                                });
        }

        private Mono<String> generatePaymentConfirmationEmailBody(Integer paymentBookId) {
                return getPaymentDetails(paymentBookId)
                                .map(paymentDetails -> {
                                        String nombres = (String) paymentDetails.get("Nombres");
                                        Integer codigoReserva = (Integer) paymentDetails.get("Codigo Reserva");
                                        String checkIn = (String) paymentDetails.get("Check In");
                                        String checkOut = (String) paymentDetails.get("Check Out");
                                        long duracionEstancia = (long) paymentDetails.get("Duración Estancia");
                                        String cantidadPersonas = (String) paymentDetails.get("Cantidad de Personas");
                                        String imagen = (String) paymentDetails.get("Imagen");
                                        String roomName = (String) paymentDetails.get("RoomName");
                                        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
                                        BookingEmailDto bookingEmailDto = new BookingEmailDto(
                                                        roomName, nombres, codigoReserva.toString(), checkIn, checkOut,
                                                        checkIn, imagen, (int) duracionEstancia,
                                                        "Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593",
                                                        cantidadPersonas);
                                        ConfirmPaymentByBankTransferAndCardTemplateEmail confirmReserveBookingTemplateEmail = new ConfirmPaymentByBankTransferAndCardTemplateEmail(
                                                        nombres, bookingEmailDto);
                                        baseEmailReserve.addEmailHandler(confirmReserveBookingTemplateEmail);
                                        return baseEmailReserve.execute();
                                });
        }

        @Override
        public Mono<Map<String, Object>> getPaymentDetails(Integer paymentBookId) {
                return paymentBookRepository.findById(paymentBookId)
                                .flatMap(paymentBook -> {
                                        // Obtener bookingId desde paymentBook
                                        Integer bookingId = paymentBook.getBookingId();

                                        // Código de reserva
                                        Integer codigoReserva = paymentBook.getPaymentBookId();

                                        // Obtener datos relacionados desde bookingId
                                        return bookingRepository.findById(bookingId)
                                                        .flatMap(booking -> {
                                                                Integer userClientId = booking.getUserClientId();
                                                                Integer roomOfferId = booking.getRoomOfferId();

                                                                // Check-in y check-out
                                                                Timestamp checkIn = booking.getDayBookingInit();
                                                                Timestamp checkOut = booking.getDayBookingEnd();

                                                                // Métodos personalizados para calcular duración de
                                                                // estancia y cantidad de personas
                                                                long duracionEstancia = TransformDate
                                                                                .calculateDaysDifference(
                                                                                                checkIn,
                                                                                                checkOut);

                                                                String cantidadPersonas = TransformDate
                                                                                .calculatePersons(
                                                                                                booking.getNumberAdults(),
                                                                                                booking.getNumberChildren(),
                                                                                                booking.getNumberBabies(),
                                                                                                booking.getNumberAdultsExtra(),
                                                                                                booking.getNumberAdultsMayor());

                                                                // Obtener nombre del cliente
                                                                Mono<String> nombreCliente = userClientRepository
                                                                                .findById(Integer.parseInt(userClientId
                                                                                                .toString()))
                                                                                .map(u -> u.getFirstName().concat(" ")
                                                                                                .concat(u.getLastName()));

                                                                // Obtener imagen del cuarto
                                                                Mono<String> imagenCuarto = roomOfferRepository
                                                                                .findById(roomOfferId)
                                                                                .flatMap(roomOffer -> roomRepository
                                                                                                .findById(roomOffer
                                                                                                                .getRoomId()))
                                                                                .map(RoomEntity::getImage);

                                                                // Obtener descripcion del cuarto
                                                                Mono<String> descCuarto = roomOfferRepository
                                                                                .findById(roomOfferId)
                                                                                .flatMap(roomOffer -> roomRepository
                                                                                                .findById(roomOffer
                                                                                                                .getRoomId()))
                                                                                .map(RoomEntity::getRoomName);

                                                                // Unir los datos en un mapa
                                                                return Mono.zip(nombreCliente, imagenCuarto, descCuarto)
                                                                                .map(tuple -> {
                                                                                        String nombre = tuple.getT1();
                                                                                        String imagen = tuple.getT2();
                                                                                        String roomName = tuple.getT3();

                                                                                        Map<String, Object> response = new HashMap<>();
                                                                                        response.put("Nombres", nombre);
                                                                                        response.put("Codigo Reserva",
                                                                                                        codigoReserva);
                                                                                        response.put("Check In",
                                                                                                        TransformDate.getMonthDayOfWeekAndNumber(
                                                                                                                        checkIn,
                                                                                                                        "EE, d MMM"));
                                                                                        response.put("Check Out",
                                                                                                        TransformDate.getMonthDayOfWeekAndNumber(
                                                                                                                        checkOut,
                                                                                                                        "EE, d MMM"));
                                                                                        response.put("Duración Estancia",
                                                                                                        duracionEstancia);
                                                                                        response.put("Cantidad de Personas",
                                                                                                        cantidadPersonas);
                                                                                        response.put("Imagen", imagen);
                                                                                        response.put("RoomName",
                                                                                                        roomName);
                                                                                        return response;
                                                                                });
                                                        });
                                });
        }

        /*
         * private String calculatePersons(int numberAdults, int numberChildren, int
         * numberBabies, int numberAdultsExtra,
         * int numberAdultsMayor) {
         * StringBuilder result = new StringBuilder();
         * 
         * if (numberAdults > 0) {
         * result.append(numberAdults).append(" adulto").append(numberAdults > 1 ? "s" :
         * "").append(" ");
         * }
         * if (numberChildren > 0) {
         * result.append(numberChildren).append(" niño").append(numberChildren > 1 ? "s"
         * : "").append(" ");
         * }
         * if (numberBabies > 0) {
         * result.append(numberBabies).append(" bebé").append(numberBabies > 1 ? "s" :
         * "").append(" ");
         * }
         * if (numberAdultsExtra > 0) {
         * result.append(numberAdultsExtra).append(" adulto extra")
         * .append(numberAdultsExtra > 1 ? "s" : "").append(" ");
         * }
         * if (numberAdultsMayor > 0) {
         * result.append(numberAdultsMayor).append(" adulto mayor")
         * .append(numberAdultsMayor > 1 ? "es" : "").append(" ");
         * }
         * 
         * // Elimina espacios extra al final
         * return result.toString().trim();
         * }
         */

}