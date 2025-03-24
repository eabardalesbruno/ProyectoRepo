package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.BookingFeedingDto;
import com.proriberaapp.ribera.Domain.dto.FullDayDetailDTO;
import com.proriberaapp.ribera.Domain.dto.PaymentBookUserDTO;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Domain.invoice.ProductSunatDomain;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceItemRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceTypeRepsitory;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.ProductSunatRepository;
import com.proriberaapp.ribera.services.client.CommissionService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;
import com.proriberaapp.ribera.utils.TransformDate;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.BookingEmailDto;
import com.proriberaapp.ribera.utils.emails.ConfirmPaymentByBankTransferAndCardTemplateEmail;
import com.proriberaapp.ribera.utils.emails.ConfirmReserveBookingTemplateEmail;
import com.proriberaapp.ribera.utils.emails.RejectedPaymentTemplateEmail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
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
        private final RoomOfferRepository roomOfferRepository;
        private final RoomRepository roomRepository;
        private final BookingRepository bookingRepository;
        private final EmailService emailService;
        private final InvoiceServiceI invoiceService;
        private final CommissionService commissionService;
        private final ProductSunatRepository productSunatRepository;
        @Autowired
        private BookingFeedingRepository bookingFeedingRepository;

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
                                        RoomRepository roomRepository, CommissionService commissionService, ProductSunatRepository productSunatRepository) {
                this.refusePaymentRepository = refusePaymentRepository;
                this.paymentBookRepository = paymentBookRepository;
                this.userClientRepository = userClientRepository;
                this.bookingRepository = bookingRepository;
                this.emailService = emailService;
                this.invoiceService = invoiceService;
                this.roomOfferRepository = roomOfferRepository;
                this.roomRepository = roomRepository;
            this.commissionService = commissionService;
            this.productSunatRepository = productSunatRepository;
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
                                        /*
                                         * return "<!DOCTYPE html>" +
                                         * "<html lang=\"es\">" +
                                         * "<head>" +
                                         * "    <meta charset=\"UTF-8\">" +
                                         * "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                                         * +
                                         * "    <title>Bienvenido</title>" +
                                         * "    <style>" +
                                         * "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; color: black; background-color: #F6F7FB; }"
                                         * +
                                         * "        .header { width: 100%; position: relative; background-color: white; padding: 20px 0; }"
                                         * +
                                         * "        .logo-left { width: 50px; position: absolute; top: 10px; left: 10px; }"
                                         * +
                                         * "        .banner { width: 100%; display: block; margin: 0 auto; }" +
                                         * "        .container { width: 100%; background-color: #FFFFFF; margin: 0px auto 0; padding: 0px 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; }"
                                         * +
                                         * "        .content { text-align: left; padding: 20px; }" +
                                         * "        .content h3 { margin: 10px 0; }" +
                                         * "        .content p { margin: 10px 0; }" +
                                         * "        .table-layout { width: 30%; margin-right: auto; border-collapse: collapse; table-layout: auto; }"
                                         * +
                                         * "        .table-layout td { vertical-align: top; padding-top: 0px; text-align: left; }"
                                         * +
                                         * "        .button { min-width: 40%; max-height: 18px; display: inline-block; padding: 10px; background-color: #025928; color: white; text-align: center; text-decoration: none; border-radius: 5px; }"
                                         * +
                                         * "        .footer { width: 100%; text-align: left; padding-top: 0px; padding-left: 40px; margin: 0px 0; color: #9D9D9D }"
                                         * +
                                         * "        .help-section { width: 100%; background-color: #FFFFFF; margin: 0px auto; padding: 5px 40px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: left; color: #384860; }"
                                         * +
                                         * "        .content-data { padding: 00px 0 0 0; background-color: #FFFFFF; }" +
                                         * "        .content-data small{ font-weight : bold; }" +
                                         * "        .content-data p{ color: #384860 !important }" +
                                         * "        .button a { display: block; color: white; text-align: center; text-decoration: none; border-radius: 0; }"
                                         * +
                                         * "        .im { color: inherit }" +
                                         * "    </style>" +
                                         * "</head>" +
                                         * "<body>" +
                                         * "    <img class=\"banner\" src=\"https://i.postimg.cc/pX3JmW8X/Image.png\" alt=\"Bienvenido\">"
                                         * +
                                         * "    <div class=\"container\">" +
                                         * "        <div class=\"content\">" +
                                         * "            <div class=\"content-data\">" +
                                         * "                <p>Hola," + userClient.getFirstName() +
                                         * ": <br> Verificamos tu pago para la reserva del <small>"
                                         * +roomName+"</small>. Lo sentimos, pero no hemos podido completar su pago en este momento. <br> Motivo de rechazo: <small>"
                                         * + refusePayment.getDetail() +
                                         * "</small>.<br> Por favor, inténtelo de nuevo. Gracias.</p>" +
                                         * "                <p>Recuerde que puede realizar su nueva reserva haciendo clic en el botón o usando este enlace: <a href=\"www.riberadelrio/reservas.com\"> www.riberadelrio/reservas.com </a></p>"
                                         * +
                                         * "                <div class=\"button\">" +
                                         * "                    <a href=\"https://cieneguillariberadelrio.online/bookings/disponibles\">Quiero reservar nuevamente</a>"
                                         * +
                                         * "                </div>" +
                                         * "            </div>" +
                                         * "        </div>" +
                                         * "    </div>" +
                                         * "    <div class=\"help-section\">" +
                                         * "        <h3>¿Necesitas ayuda?</h3>" +
                                         * "        <p>Envía tus comentarios e información de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>"
                                         * +
                                         * "    </div>" +
                                         * "    <div class=\"footer\">" +
                                         * "        <p>Si prefiere no recibir este tipo de correo electrónico, ¿no quiere más correos electrónicos de Ribera? <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">Darse de baja</a>.<br> Valle Encantado S.A.C, Perú.<br> © 2023 Inclub</p>"
                                         * +
                                         * "    </div>" +
                                         * "</body>" +
                                         * "</html>";
                                         */
                                });
        }

    @Override
    public Mono<Void> updatePendingPayAndSendConfirmation(Integer paymentBookId) {
        return this.paymentBookRepository
                .loadUserDataAndBookingData(paymentBookId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El ID de pago no existe")))
                .flatMap(paymentBook -> {
                    if (paymentBook.getPendingpay() == 0) {
                        paymentBook.setPaymentstateid(2);
                        paymentBook.setPendingpay(1);

                        String roomOrType = paymentBook.getRoomname() != null ? paymentBook.getRoomname() : paymentBook.getType();
                        String normalizedRoomOrType = normalizeText(roomOrType);
                        Integer fulldayId = paymentBook.getFulldayid();

                        Mono<List<FullDayDetailDTO>> fulldayDetailsMono = paymentBookRepository.findByFullDayId(fulldayId).collectList();

                        Mono<String> codSunatMono = this.productSunatRepository.findAll()
                                .filter(product -> normalizeText(product.getDescription()).equals(normalizedRoomOrType))
                                .map(ProductSunatDomain::getCodSunat)
                                .defaultIfEmpty("631210")
                                .next();

                        return Mono.zip(codSunatMono, fulldayDetailsMono)
                                .flatMap(tuple -> {
                                    String codSunat = tuple.getT1();
                                    List<FullDayDetailDTO> fullDayDetails = tuple.getT2();

                                    InvoiceClientDomain clientDomain = new InvoiceClientDomain(
                                            paymentBook.getUsername(),
                                            paymentBook.getInvoicedocumentnumber(),
                                            paymentBook.getUseraddress(),
                                            paymentBook.getUserphone(),
                                            paymentBook.getUseremail(),
                                            paymentBook.getUserclientid());

                                    InvoiceCurrency invoiceCurrency = InvoiceCurrency.getInvoiceCurrencyByCurrency(paymentBook.getCurrencytypename());
                                    InvoiceType type = InvoiceType.getInvoiceTypeByName(paymentBook.getInvoicetype().toUpperCase());

                                    InvoiceDomain invoiceDomain = new InvoiceDomain(
                                            clientDomain,
                                            paymentBook.getPaymentbookid(), 18, invoiceCurrency,
                                            type, paymentBook.getPercentagediscount());
                                    invoiceDomain.setOperationCode(paymentBook.getOperationcode());

                                    List<InvoiceItemDomain> invoiceItems = new ArrayList<>();

                                    if (paymentBook.getRoomname() != null) {
                                        invoiceItems.add(new InvoiceItemDomain(
                                                roomOrType,
                                                codSunat,
                                                roomOrType,
                                                1,
                                                BigDecimal.valueOf(paymentBook.getTotalcostwithoutdiscount())
                                        ));
                                    } else {
                                        for (FullDayDetailDTO detail : fullDayDetails) {
                                            String sunatCode = getSunatCode(paymentBook.getType(), detail.getTypePerson());
                                            String itemDescription = getItemDescription(paymentBook.getType(), detail.getTypePerson());
                                            invoiceItems.add(new InvoiceItemDomain(
                                                    itemDescription,
                                                    sunatCode,
                                                    itemDescription,
                                                    detail.getQuantity(),
                                                    detail.getFinalPrice()
                                            ));
                                        }
                                    }

                                    invoiceDomain.calculatedTotals();

                                    if (paymentBook.getRoomname() != null) {
                                        return generatePaymentConfirmationEmailBody(paymentBookId)
                                                .flatMap(emailBody -> this.invoiceService.save(invoiceDomain)
                                                        .then(Mono.zip(
                                                                paymentBookRepository.confirmPayment(paymentBookId),
                                                                this.emailService.sendEmail(
                                                                        paymentBook.getUseremail(),
                                                                        "Confirmación de Pago Aceptado",
                                                                        emailBody)))
                                                        .then());
                                    } else {
                                        return this.invoiceService.save(invoiceDomain)
                                                .then(paymentBookRepository.confirmPayment(paymentBookId))
                                                .then();
                                    }
                                });
                    }
                    return Mono.empty();
                });
    }

    private String normalizeText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }

    private String getSunatCode(String fullDayType, String typePerson) {
        if (fullDayType.equalsIgnoreCase("Full Day Normal")) {
            switch (typePerson) {
                case "ADULTO":
                    return "631230";
                case "ADULTO_MAYOR":
                    return "631229";
                case "NINO":
                    return "631231";
                default:
                    return "631230";
            }
        } else if (fullDayType.equalsIgnoreCase("Full Day Todo Completo")) {
            switch (typePerson) {
                case "ADULTO":
                    return "631226";
                case "ADULTO_MAYOR":
                    return "631228";
                case "NINO":
                    return "631227";
                default:
                    return "631226";
            }
        }
        return "631226";
    }

    private String getItemDescription(String fullDayType, String typePerson) {
        if (fullDayType.equalsIgnoreCase("Full Day Normal")) {
            switch (typePerson) {
                case "ADULTO":
                    return "Full day - Solo Entrada - Adulto";
                case "ADULTO_MAYOR":
                    return "Full day - Solo Entrada - Adulto Mayor";
                case "NINO":
                    return "Full day - Solo Entrada - Niño";
                default:
                    return "Full day - Solo Entrada - Adulto";
            }
        } else if (fullDayType.equalsIgnoreCase("Full Day Todo Completo")) {
            switch (typePerson) {
                case "ADULTO":
                    return "Full day - Todo Incluido - Adulto";
                case "ADULTO_MAYOR":
                    return "Full day - Todo Incluido - Adulto Mayor";
                case "NINO":
                    return "Full day - Todo Incluido - Niño";
                default:
                    return "Full day - Todo Incluido - Adulto";
            }
        }
        return "Full day - Todo Incluido - Adulto";
    }

    private PaymentBookEntity mapToPaymentBookEntity(PaymentBookUserDTO dto) {
        return PaymentBookEntity.builder()
                .paymentBookId(dto.getPaymentbookid())
                .operationCode(dto.getOperationcode())
                .note(dto.getNote())
                .totalCost(dto.getTotalCost())
                .imageVoucher(dto.getImagevoucher())
                .totalPoints(dto.getTotalpoints())
                .paymentComplete(dto.getPaymentcomplete())
                .pendingpay(dto.getPendingpay())
                .userClientId(dto.getUserclientid())
                .paymentStateId(dto.getPaymentstateid())
                .currencyTypeId(dto.getCurrencytypeid())
                .percentageDiscount(dto.getPercentagediscount())
                .totalCostWithOutDiscount(dto.getTotalcostwithoutdiscount())
                .invoiceDocumentNumber(dto.getInvoicedocumentnumber())
                .invoiceType(dto.getInvoicetype())
                .bookingId(dto.getBookingid())
                .build();
    }

        private Mono<String> generatePaymentConfirmationEmailBody(Integer paymentBookId) {

                // Obtén los datos de `getPaymentDetails`
                return getPaymentDetails(paymentBookId)
                                .map(paymentDetails -> {
                                        // Extrae los valores del Map
                                        String nombres = (String) paymentDetails.get("Nombres");
                                        Integer codigoReserva = (Integer) paymentDetails.get("Codigo Reserva");
                                        String checkIn = (String) paymentDetails.get("Check In");
                                        String checkOut = (String) paymentDetails.get("Check Out");
                                        long duracionEstancia = (long) paymentDetails.get("Duración Estancia");
                                        String cantidadPersonas = (String) paymentDetails.get("Cantidad de Personas");
                                        String imagen = (String) paymentDetails.get("Imagen");
                                        String roomName = (String) paymentDetails.get("RoomName");
                                        List<BookingFeedingDto> bookingFeeding = (List<BookingFeedingDto>) paymentDetails
                                                        .get("BookingFeeding");
                                        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();

                                        BookingEmailDto bookingEmailDto = new BookingEmailDto(
                                                        roomName, nombres, codigoReserva.toString(), checkIn, checkOut,
                                                        checkIn, imagen, (int) duracionEstancia,
                                                        "Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593",
                                                        cantidadPersonas);
                                        ConfirmPaymentByBankTransferAndCardTemplateEmail confirmReserveBookingTemplateEmail = new ConfirmPaymentByBankTransferAndCardTemplateEmail(
                                                        nombres, bookingEmailDto, bookingFeeding.size() > 0);
                                        baseEmailReserve.addEmailHandler(confirmReserveBookingTemplateEmail);
                                        return baseEmailReserve.execute();

                                        /*
                                         * return "<!DOCTYPE html>" +
                                         * "<html lang=\"es\">" +
                                         * "<head>" +
                                         * "    <meta charset=\"UTF-8\">" +
                                         * "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                                         * +
                                         * "    <title>Bienvenido</title>" +
                                         * "    <style>" +
                                         * "        .header { width: 100%; position: relative; background-color: white; padding: 20px 0; }"
                                         * +
                                         * "        .logo-left { width: 50px; position: absolute; top: 10px; left: 10px; }"
                                         * +
                                         * "        .banner { width: 100%; display: block; margin: 0 auto; }"
                                         * +
                                         * "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; color: black; background-color: #F6F7FB; }"
                                         * +
                                         * "        .container { width: 100%; max-width: 100%; background-color: #FFFFFF; margin: 0px auto; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                                         * +
                                         * "        .details-table { width: 100%; border-collapse: collapse; }"
                                         * +
                                         * "        .details-table td { vertical-align: top; padding: 5px; }"
                                         * +
                                         * "        .header img { width: 100%; height: auto; border-radius: 10px; }"
                                         * +
                                         * "        .title { font-size: 1.2rem; font-weight: bold; margin-bottom: 10px; color: #384860 !important }"
                                         * +
                                         * "        .section-title { font-weight: bold; margin-top: 10px; }"
                                         * +
                                         * "        .highlight { color: #025928; font-weight: bold; }" +
                                         * "        .info { font-size: 0.9rem; color: #025928 !important; }"
                                         * +
                                         * "        .help-section { max-width: 100%; background-color: #FFFFFF; margin: 20px auto; padding: 10px 40px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: left; color: #384860; }"
                                         * +
                                         * "    </style>" +
                                         * "</head>" +
                                         * "<body>" +
                                         * "<div class=\"header\">" +
                                         * "</div>" +
                                         * "<img class=\"banner\" src=\"https://i.postimg.cc/pX3JmW8X/Image.png\" alt=\"Bienvenido\">"
                                         * +
                                         * "<div class=\"container\">" +
                                         * "            <p style=\"padding: 0px 10px; color: #384860 !important\">Estimado(a), "
                                         * + nombres + " <br><br>" +
                                         * "                El presente es para informar que se completó exitosamente el registro de su pago para la reserva de: Departamento estándar vista piscina.</p>"
                                         * +
                                         * "    <table class=\"details-table\">" +
                                         * "        <tr>" +
                                         * "            <td style=\"width: 40%;\">" +
                                         * "                <div class=\"header\">" +
                                         * "                    <img src=\"" + imagen
                                         * + "\" alt=\"Departamento Estándar\">" +
                                         * "                </div>" +
                                         * "            </td>" +
                                         * "            <td style=\"width: 60%;\">" +
                                         * "                <div class=\"details\">" +
                                         * "                    <p class=\"title\">" + roomName + "</p>" +
                                         * "                    <table>" +
                                         * "                        <tr><td><span class=\"section-title\">Titular de la reserva:</span></td><td>"
                                         * + nombres + "</td></tr>" +
                                         * "                        <tr><td><span class=\"section-title\">Código de reserva:</span></td><td>"
                                         * + codigoReserva + "</td></tr>" +
                                         * "                        <tr><td><span class=\"section-title\">Check-in:</span></td><td><span class=\"highlight\">"
                                         * + checkIn + "</span></td></tr>" +
                                         * "                        <tr><td><span class=\"section-title\">Check-out:</span></td><td><span class=\"highlight\">"
                                         * + checkOut + "</span></td></tr>" +
                                         * "                        <tr><td colspan=\"2\"><span class=\"info\">Hora de llegada aproximada: 10:00 A.M <br> (*) Recuerda que el check-in es las 3:00 P.M.</span></td></tr>"
                                         * +
                                         * "                        <tr><td><span class=\"section-title\">Duración total de estancia:</span></td><td>"
                                         * + duracionEstancia + "</td></tr>" +
                                         * "                        <tr><td><span class=\"section-title\">Cantidad de personas:</span></td><td>"
                                         * + cantidadPersonas + "</td></tr>" +
                                         * "                        <tr><td colspan=\"2\"><span class=\"section-title\">Ubicación:</span> Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rústico Etapa III, Cercado de Lima 15593</td></tr>"
                                         * +
                                         * "                    </table>" +
                                         * "                </div>" +
                                         * "            </td>" +
                                         * "        </tr>" +
                                         * "    </table>" +
                                         * "    <p style=\"padding: 0px 10px; color: #384860 !important\">Este correo es solo de caracter informativo, no es un comprobante de pago. En caso de no poder usar la reservacion, por favor llamar con 2 días de anticipacion. Muchas gracias.</p>"
                                         * +
                                         * "</div>" +
                                         * "<div class=\"help-section\">" +
                                         * "    <h3>¿Necesitas ayuda?</h3>" +
                                         * "    <p>Envia tus comentarios e información de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>"
                                         * +
                                         * "</div>" +
                                         * "<div class=\"\" style=\"max-width: 100%; padding: 5px 40px; color: #9D9D9D; margin: 0 auto;\">"
                                         * +
                                         * "    <p>Si prefiere no recibir este tipo de correo electrónico, ¿no quiere más correos electrónicos de Ribera? <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">Darse de baja.</a> <br> Valle Encantado S.A.C, Perú.<br> © 2023 Inclub</p>"
                                         * +
                                         * "</div>" +
                                         * "</body>" +
                                         * "</html>";
                                         */
                                });
        }

        /*
         * public static long calculateDaysDifference(Timestamp dayBookingInit,
         * Timestamp dayBookingEnd) {
         * if (dayBookingInit == null || dayBookingEnd == null) {
         * throw new IllegalArgumentException("Both Timestamps must be non-null");
         * }
         * 
         * // Convertir los Timestamp a LocalDate
         * LocalDate startDate = dayBookingInit.toInstant()
         * .atZone(ZoneId.systemDefault())
         * .toLocalDate();
         * 
         * LocalDate endDate = dayBookingEnd.toInstant()
         * .atZone(ZoneId.systemDefault())
         * .toLocalDate();
         * 
         * // Calcular la diferencia de días
         * return ChronoUnit.DAYS.between(startDate, endDate);
         * }
         */

        /*
         * public static String getMonthDayOfWeekAndNumber(Timestamp timestamp) {
         * // Formateador para el mes, día de la semana abreviados y número de día
         * SimpleDateFormat formatter = new SimpleDateFormat("EE, d MMM", new
         * Locale("es", "ES"));
         * // Convertir el Timestamp a Date y formatearlo
         * return formatter.format(timestamp);
         * }
         */
        @Override
        public Mono<Map<String, Object>> getPaymentDetails(Integer paymentBookId) {
                return paymentBookRepository.findByPaymentBookId(paymentBookId)
                                .flatMap(paymentBook -> {
                                        // Obtener bookingId desde paymentBook
                                        Integer bookingId = paymentBook.getBookingId();

                                        // Código de reserva
                                        Integer codigoReserva = paymentBook.getBookingId();

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
                                                                                .map(room -> room.getImage() != null
                                                                                                ? room.getImage()
                                                                                                : "");

                                                                // Obtener descripcion del cuarto
                                                                Mono<String> descCuarto = roomOfferRepository
                                                                                .findById(roomOfferId)
                                                                                .flatMap(roomOffer -> roomRepository
                                                                                                .findById(roomOffer
                                                                                                                .getRoomId()))
                                                                                .map(RoomEntity::getRoomName);

                                                                Mono<List<BookingFeedingDto>> bookingFeedingMono = this.bookingFeedingRepository
                                                                                .listBookingFeedingByBookingId(
                                                                                                bookingId)
                                                                                .collectList();

                                                                // Unir los datos en un mapa
                                                                return Mono.zip(nombreCliente, imagenCuarto, descCuarto,
                                                                                bookingFeedingMono)
                                                                                .map(tuple -> {
                                                                                        String nombre = tuple.getT1();
                                                                                        String imagen = tuple.getT2();
                                                                                        String roomName = tuple.getT3();
                                                                                        List<BookingFeedingDto> bookingFeeding = tuple
                                                                                                        .getT4();

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
                                                                                        response.put("BookingFeeding",
                                                                                                        bookingFeeding);
                                                                                        return response;
                                                                                });
                                                        });
                                });
        }

        // Métodos de cálculo personalizados
        private int calculateDuration(int... values) {
                return Arrays.stream(values).sum();
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