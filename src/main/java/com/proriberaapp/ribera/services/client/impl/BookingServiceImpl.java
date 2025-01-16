package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Api.controllers.client.dto.*;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Domain.dto.BookingFeedingDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.BookingFeedingEntity;
import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.PartnerPointsService;
import com.proriberaapp.ribera.utils.GeneralMethods;
import com.proriberaapp.ribera.utils.TransformDate;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.ConfirmReserveBookingTemplateEmail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
        private final FeedingRepository feedingRepository;
        private final BookingFeedingRepository bookingFeedingRepository;
        private final BookingRepository bookingRepository;
        private final UserClientRepository userClientRepository;
        private final EmailService emailService;
        private final PartnerPointsService partnerPointsService;
        private final ComfortTypeRepository comfortTypeRepository;
        private final RoomOfferRepository roomOfferRepository;
        private final CancelPaymentRepository cancelPaymentRepository;
        private final FinalCostumerRepository finalCostumerRepository;
        private final PaymentBookRepository paymentBookRepository;
        private final RefusePaymentRepository refusePaymentRepository;
        private final BedsTypeRepository bedsTypeRepository;
        private final RoomRepository roomRepository;
        // private final WebClient webClient;
        @Value("${app.upload.dir}")
        private String uploadDir;

        @Value("${room.offer.ratio.base}")
        private Integer RATIO_BASE;

        public BookingServiceImpl(
                        BookingRepository bookingRepository,
                        UserClientRepository userClientRepository,
                        EmailService emailService,
                        PartnerPointsService partnerPointsService,
                        ComfortTypeRepository comfortTypeRepository,
                        CancelPaymentRepository cancelPaymentRepository,
                        RefusePaymentRepository refusePaymentRepository,
                        BedsTypeRepository bedsTypeRepository,
                        RoomOfferRepository roomOfferRepository, FinalCostumerRepository finalCostumerRepository,
                        PaymentBookRepository paymentBookRepository, RoomRepository roomRepository,
                        BookingFeedingRepository bookingFeedingRepository,
                        FeedingRepository feedingRepository
        // WebClient.Builder webClientBuilder
        ) {
                this.bookingRepository = bookingRepository;
                this.userClientRepository = userClientRepository;
                this.emailService = emailService;
                this.partnerPointsService = partnerPointsService;
                this.comfortTypeRepository = comfortTypeRepository;
                this.cancelPaymentRepository = cancelPaymentRepository;
                this.refusePaymentRepository = refusePaymentRepository;
                this.bedsTypeRepository = bedsTypeRepository;
                this.roomOfferRepository = roomOfferRepository;
                this.finalCostumerRepository = finalCostumerRepository;
                this.paymentBookRepository = paymentBookRepository;
                this.roomRepository = roomRepository;
                this.bookingFeedingRepository = bookingFeedingRepository;
                this.feedingRepository = feedingRepository;
                // this.webClient = webClientBuilder.baseUrl(uploadDir).build();
        }

        @Override
        public Mono<BigDecimal> getRiberaPointsByBookingId(Integer bookingId) {
                return bookingRepository.findById(bookingId)
                                .flatMap(booking -> roomOfferRepository.findById(booking.getRoomOfferId()))
                                .map(roomOffer -> {
                                        // Convert Integer to BigDecimal
                                        Integer riberaPoints = roomOffer.getRiberaPoints();
                                        return riberaPoints != null ? BigDecimal.valueOf(riberaPoints)
                                                        : BigDecimal.ZERO;
                                });
        }

        @Override
        public Flux<BookingEntity> findBookingsByStateId(Integer bookingStateId) {
                return bookingRepository.findAllByBookingStateId(bookingStateId);
        }

        @Override
        public Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginated(
                        Integer bookingStateId,
                        Integer roomTypeId,
                        Integer capacity,
                        LocalDateTime offertimeInit,
                        LocalDateTime offertimeEnd,
                        int page,
                        int size) {
                int offset = page * size;

                Flux<BookingStates> bookings = bookingRepository.findBookingsByStateIdPaginated(
                                bookingStateId, roomTypeId, offertimeInit, offertimeEnd, size, offset);

                Mono<Long> totalElements = bookingRepository.countBookingsByStateId(
                                bookingStateId, roomTypeId, offertimeInit, offertimeEnd);

                return bookings.collectList()
                                .zipWith(totalElements)
                                .map(tuple -> new PaginatedResponse<>(tuple.getT2(), tuple.getT1()));
        }

        @Override
        public Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginatedAndUserId(Integer bookingStateId,
                        Integer roomTypeId, Integer capacity, LocalDateTime offertimeInit, LocalDateTime offertimeEnd,
                        int page,
                        int size, Integer userId) {
                int offset = page * size;

                Flux<BookingStates> bookings = bookingRepository.findBookingsByStateIdPaginatedAndUserId(
                                bookingStateId, roomTypeId, offertimeInit, offertimeEnd, size, offset, userId);

                Mono<Long> totalElements = bookingRepository.countBookingsByStateIdAndUserId(
                                bookingStateId, roomTypeId, offertimeInit, offertimeEnd, userId);

                return bookings.collectList()
                                .zipWith(totalElements)
                                .map(tuple -> new PaginatedResponse<>(tuple.getT2(), tuple.getT1()));
        }

        @Override
        public Mono<Boolean> deleteBookingNotPay() {
                return bookingRepository.findAll()
                                .filter(BookingEntity::hasPassed1Hours)
                                .flatMap(booking -> {
                                        if (booking.getUserClientId() == 0) {
                                                return bookingRepository.deleteById(booking.getBookingId());
                                        }
                                        return paymentBookRepository
                                                        .findAllByBookingIdAndCancelReasonIdIsNull(
                                                                        booking.getBookingId())
                                                        .collectList()
                                                        .flatMap(payments -> {
                                                                if (!payments.isEmpty()) {
                                                                        return Mono.empty();
                                                                } else {
                                                                        return bookingRepository.deleteById(
                                                                                        booking.getBookingId());
                                                                }
                                                        });
                                })
                                .then(Mono.just(true));
        }

        @Override
        public Mono<BookingEntity> assignClientToBooking(Integer bookingId, Integer userClientId) {
                return bookingRepository.findByBookingId(bookingId)
                                .map(bookingEntity -> {
                                        bookingEntity.setUserClientId(userClientId);
                                        return bookingEntity;
                                })
                                .flatMap(bookingRepository::save);
        }

        @Override
        public Mono<Boolean> deleteBooking(Integer bookingId) {
                return bookingRepository.deleteById(bookingId)
                                .then(Mono.just(true));
        }

        @Override
        public Mono<Integer> getUserClientIdByBookingId(Integer bookingId) {
                return bookingRepository.findById(bookingId)
                                .map(booking -> booking.getUserClientId());
        }

        private Mono<String> getRoomName(Integer roomOfferId) {
                return roomOfferRepository.findById(roomOfferId)
                                .flatMap(roomOfferEntity -> roomRepository.findById(roomOfferEntity.getRoomId()))
                                .map(RoomEntity::getRoomName)
                                .switchIfEmpty(Mono.just("Habitación no encontrada"));
        }

        // Método para generar el cuerpo del correo electrónico con el nombre de la
        // habitación
        private String generateEmailBody(BookingEntity bookingEntity, String roomName) {
                String monthInit = TransformDate.getAbbreviatedMonth(bookingEntity.getDayBookingInit());
                String monthEnd = TransformDate.getAbbreviatedMonth(bookingEntity.getDayBookingEnd());
                int dayInit = TransformDate.getDayNumber(bookingEntity.getDayBookingInit());
                int dayEnd = TransformDate.getDayNumber(bookingEntity.getDayBookingEnd());
                long dayInterval = TransformDate.calculateDaysDifference(bookingEntity.getDayBookingInit(),
                                bookingEntity.getDayBookingEnd());

                String body = "<!DOCTYPE html>" +
                                "<html lang=\"es\">" +
                                "<head>" +
                                "    <meta charset=\"UTF-8\">" +
                                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                                "    <title>Bienvenido</title>" +
                                "    <style>" +
                                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; color: black; background-color: #F6F7FB; }"
                                +
                                "        .header { width: 100%; position: relative; background-color: white; padding: 20px 0; }"
                                +
                                "        .logo-left { width: 50px; position: absolute; top: 10px; left: 10px; }" +
                                "        .banner { width: 100%; display: block; margin: 0 auto; }" +
                                "        .container { width: 100%; background-color: #FFFFFF; margin: 0px auto 0; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; }"
                                +
                                "        .content { text-align: left; padding: 20px; }" +
                                "        .content h3 { margin: 10px 0; }" +
                                "        .content p { margin: 10px 0; color : #353535 !important}" +
                                "        .table-layout { width: 30%; margin-right: auto; border-collapse: collapse; table-layout: auto; }"
                                +
                                "        .table-layout td { vertical-align: top; padding-top: 0px; text-align: left; }"
                                +
                                "        .button { min-width: 90%; display: inline-block; padding: 10px; background-color: #025928; color: white !important; text-align: center; text-decoration: none; border-radius: 0px; }"
                                +
                                "        .footer { width: 100%; text-align: left; padding-top: 0px; padding-left: 40px; margin: 20px 0; color: #9D9D9D !important }"
                                +
                                "        .help-section { width: 100%; background-color: #FFFFFF; margin: 10px auto; padding: 10px 40px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: left; color: #384860; }"
                                +
                                "        .content-data { padding: 10px; background-color: #F9F9F9; max-width: 400px; color : #353535 !important}"
                                +
                                "    </style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class=\"header\">" +
                                "        <img class=\"logo-left\" src=\"https://i.postimg.cc/7PsVyMZz/Email-Template-Header.png\" alt=\"Logo Izquierda\">"
                                +
                                "    </div>" +
                                "    <img class=\"banner\" src=\"https://i.postimg.cc/pX3JmW8X/Image.png\" alt=\"Bienvenido\">"
                                +
                                "    <div class=\"container\">" +
                                "        <div class=\"content\">" +
                                "            <h3  style=\"color: #384860 !important; font-weight: bold\">Estimado cliente</h3>"
                                +
                                "            <p>Se completó exitosamente el registro de su reserva de: <strong>"
                                + roomName
                                + "</strong>. Por favor, no se olvide de pagar su reserva.</p>" +
                                "            <div class=\"content-data\">" +
                                "                <p style=\"font-size: 1rem; font-weight: 600; font-family: 'Poppins', sans-serif; margin: 0; padding: 0; padding-top: 10px; padding-bottom: 20px\">Los datos de tu reserva</p>"
                                +
                                "                <table class=\"table-layout\">" +
                                "                    <tr><td>Entrada</td><td>Salida</td></tr>" +
                                "                    <tr><td><span style=\"font-size: 1.05rem; font-weight: 500;\">"
                                + monthInit + " "
                                + dayInit + "</span></td><td><span style=\"font-size: 1.05rem; font-weight: 500;\">"
                                + monthEnd + " "
                                + dayEnd + "</span></td></tr>" +
                                "                </table>" +
                                "                <p>Duracion de estancia: <br> <strong>" + dayInterval
                                + " días</strong></p>" +
                                "                <p>Seleccion de reserva: <br> <strong>" + roomName + "</strong></p>" +
                                "                <a href=\"https://cieneguillariberadelrio.online/bookings/disponibles\" class=\"button\">Pagar ahora</a>"
                                +
                                "            </div>" +
                                "            <p style=\"padding: 10px 0 0 0; color: #384860\">Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a través de agente BCP, agencias o cualquier método de pago dentro de la plataforma usando este enlace: <a href=\"mailto:informesyreservas@cieneguilladelrio.com\"> www.riberadelrio/reservas.com</a></p>"
                                +
                                "        </div>" +
                                "    </div>" +
                                "    <div class=\"help-section\">" +
                                "        <h3>¿Necesitas ayuda?</h3>" +
                                "        <p>Envia tus comentarios e informacion de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>"
                                +
                                "    </div>" +
                                "    <div class=\"footer\">" +
                                "        <p>Si prefiere no recibir este tipo de correo electrónico, ¿no quiere más correos electrónicos de Ribera? <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">Darse de baja</a>. <br> Valle Encantado S.A.C, Perú. <br> © 2023 Inclub</p>"
                                +
                                "    </div>" +
                                "</body>" +
                                "</html>";
                return body;
        }

        @Override
        public Mono<BookingEntity> save(BookingEntity entity) {
                return null;
        }

        @Override
        public Flux<BookingEntity> saveAll(List<BookingEntity> bookingEntity) {
                return bookingRepository.findAllByBookingStateIdIn(bookingEntity)
                                .collectList()
                                .flatMapMany(bookingEntities -> bookingRepository.saveAll(
                                                bookingEntity.stream().filter(
                                                                bookingEntity1 -> {
                                                                        bookingEntity1.setBookingStateId(3);
                                                                        bookingEntity1.setCreatedAt(new Timestamp(
                                                                                        System.currentTimeMillis()));
                                                                        return !bookingEntities
                                                                                        .contains(bookingEntity1);
                                                                }).toList()));
        }

        @Override
        public Mono<BigDecimal> getCostFinalByBookingId(Integer bookingId) {
                return bookingRepository.findById(bookingId)
                                .flatMap(bookingEntity -> {
                                        if (bookingEntity != null) {
                                                return Mono.just(bookingEntity.getCostFinal());
                                        } else {
                                                return Mono.error(new RuntimeException(
                                                                "BookingEntity not found for bookingId: " + bookingId));
                                        }
                                });
        }

        @Override
        public Mono<BookingEntity> findByBookingId(Integer bookingId) {
                return bookingRepository.findById(bookingId);
        }

        @Override
        public Mono<BookingEntity> findById(Integer id) {
                return bookingRepository.findById(id);
        }

        @Override
        public Flux<BookingEntity> findAll() {
                return bookingRepository.findAll();
        }

        @Override
        public Mono<Void> deleteById(Integer id) {
                return bookingRepository.deleteById(id);
        }

        @Override
        public Mono<BookingEntity> update(BookingEntity bookingEntity) {
                return bookingRepository.save(bookingEntity);
        }
        // TODO: por cada 50 dolares es un punto
        // TODO: silver 10 alojamientos, gold 15, premium 20
        // TODO: considerar el consumo

        @Override
        public Mono<BookingEntity> updateBookingStatePay(Integer bookingId, Integer bookingStateId) {
                return bookingRepository.findById(bookingId)
                                .map(bookingEntity -> {
                                        bookingEntity.setBookingStateId(bookingStateId);
                                        return bookingEntity;
                                })
                                .flatMap(bookingEntity -> {
                                        if (bookingStateId == 3) {
                                                bookingEntity.setBookingStateId(2);
                                        }
                                        PartnerPointsEntity partnerPointsEntity = PartnerPointsEntity.builder()
                                                        .userClientId(bookingEntity.getUserClientId())
                                                        .points(bookingEntity.getCostFinal().intValue() / RATIO_BASE)
                                                        .build();

                                        return partnerPointsService
                                                        .incrementPoints(partnerPointsEntity,
                                                                        partnerPointsEntity.getPoints())
                                                        .flatMap(updatedBookingEntity -> bookingRepository
                                                                        .save(bookingEntity));
                                });
        }

        @Override
        public Flux<CalendarDate> calendarDate(Integer id) {
                return bookingRepository.findAllCalendarDate(id);
        }

        /*
         * @Override
         * public Mono<BookingEntity> save(Integer userClientId, BookingSaveRequest
         * bookingSaveRequest) {
         *
         * if (bookingSaveRequest.getDayBookingInit().isBefore(LocalDate.now())) {
         * return Mono.error(new CustomException(HttpStatus.
         * BAD_REQUEST,"La fecha de inicio no puede ser anterior al día actual"));
         * }
         * assert bookingSaveRequest.getNumberBaby() != null;
         * assert bookingSaveRequest.getNumberChild() != null;
         * assert bookingSaveRequest.getNumberAdult() != null;
         * if(bookingSaveRequest.getNumberBaby()+bookingSaveRequest.getNumberChild()+
         * bookingSaveRequest.getNumberAdult() == 0){
         * return Mono.error(new CustomException(HttpStatus.BAD_REQUEST,
         * "Debe seleccionar al menos un huésped"));
         * }
         *
         * Integer numberOfDays =
         * calculateDaysBetween(bookingSaveRequest.getDayBookingInit(),
         * bookingSaveRequest.getDayBookingEnd());
         *
         * Mono<RoomOfferEntity> roomOfferEntityMono =
         * roomOfferRespository.findById(bookingSaveRequest.getRoomOfferId());
         *
         * BookingEntity bookingEntity = BookingEntity.createBookingEntity(userClientId,
         * bookingSaveRequest, numberOfDays);
         *
         * return bookingRepository.findExistingBookings(
         * bookingEntity.getRoomOfferId(),
         * bookingEntity.getDayBookingInit(),
         * bookingEntity.getDayBookingEnd())
         * .hasElements()
         * .flatMap(exists -> exists
         * ? Mono.error(new CustomException(HttpStatus.
         * BAD_REQUEST,"La reserva ya existe para las fechas seleccionadas"))
         * : Mono.just(bookingEntity)
         * .flatMap(bookingEntity1 -> roomOfferEntityMono
         * .map(roomOfferEntity -> {
         * bookingEntity1.setCostFinal(roomOfferEntity.getCost().multiply(BigDecimal.
         * valueOf(numberOfDays)));
         * return bookingEntity1;
         * })
         * )
         * .flatMap(bookingRepository::save)
         * );
         * }
         */

        @Override
        public Mono<BookingEntity> save(Integer userClientId, BookingSaveRequest bookingSaveRequest, Boolean isPromotor,
                        Boolean isReceptionist) {
                // Validar que la fecha de inicio no sea anterior al día actual
                if (bookingSaveRequest.getDayBookingInit().isBefore(LocalDate.now())) {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST,
                                        "La fecha de inicio no puede ser anterior al día actual"));
                }
                int totalPeople = bookingSaveRequest.getNumberAdult() +
                                bookingSaveRequest.getNumberAdultExtra() +
                                bookingSaveRequest.getNumberAdultMayor() +
                                bookingSaveRequest.getNumberBaby() +
                                bookingSaveRequest.getNumberChild();

                /*
                 * if (totalPeople < 2 || totalPeople > 7) {
                 * return Mono.error(
                 * new CustomException(HttpStatus.BAD_REQUEST,
                 * "El número total de personas debe ser entre 2 y 7"));
                 * }
                 */
                int totalAdults = bookingSaveRequest.getNumberAdult() +
                                bookingSaveRequest.getNumberAdultExtra() +
                                bookingSaveRequest.getNumberAdultMayor();
                int totalChildren = bookingSaveRequest.getNumberBaby() +
                                bookingSaveRequest.getNumberChild();
                String totalPeoples = TransformDate.calculatePersons(bookingSaveRequest.getNumberAdult(),
                                bookingSaveRequest.getNumberChild(),
                                bookingSaveRequest.getNumberBaby(),
                                bookingSaveRequest.getNumberAdultExtra(),
                                bookingSaveRequest.getNumberAdultMayor());
                bookingSaveRequest.setTotalCapacity(totalAdults + totalChildren);
                if (bookingSaveRequest.getNumberBaby() < 0 ||
                                bookingSaveRequest.getNumberAdult() < 0 ||
                                bookingSaveRequest.getNumberAdultExtra() < 0 ||
                                bookingSaveRequest.getNumberAdultMayor() < 0 ||
                                bookingSaveRequest.getNumberChild() < 0) {
                        return Mono.error(
                                        new CustomException(HttpStatus.BAD_REQUEST,
                                                        "Las cantidades no pueden ser menores que Cero"));
                }
                // Calcular el número de días entre la fecha de inicio y fin
                Integer numberOfDays = calculateDaysBetween(bookingSaveRequest.getDayBookingInit(),
                                bookingSaveRequest.getDayBookingEnd());

                // Obtener el nombre de la habitación
                /*
                 * return getRoomName(bookingSaveRequest.getRoomOfferId())
                 * .flatMap(roomName -> {
                 * // Obtener la oferta de habitación
                 * return roomOfferRepository.findById(bookingSaveRequest.getRoomOfferId())
                 * .flatMap(roomOfferEntity -> {
                 * // Crear la entidad de reserva con los datos
                 * // proporcionados
                 * BookingEntity bookingEntity = BookingEntity
                 * .createBookingEntity(userClientId,
                 * bookingSaveRequest,
                 * numberOfDays,
                 * isPromotor,
                 * isReceptionist);
                 * 
                 * // Cálculo del costo inicial (bebés, niños, adultos,
                 * // etc.)
                 * 
                 * BigDecimal costFinal = GeneralMethods
                 * .calculateCostTotal(
                 * bookingSaveRequest
                 * .getAdultCost()
                 * .floatValue(),
                 * bookingSaveRequest
                 * .getAdultExtraCost()
                 * .floatValue(),
                 * bookingSaveRequest
                 * .getAdultMayorCost()
                 * .floatValue(),
                 * bookingSaveRequest
                 * .getKidCost()
                 * .floatValue(),
                 * bookingSaveRequest
                 * .getInfantCost()
                 * .floatValue(),
                 * bookingSaveRequest
                 * .getNumberAdult(),
                 * bookingSaveRequest
                 * .getNumberAdultExtra(),
                 * bookingSaveRequest
                 * .getNumberAdultMayor(),
                 * bookingSaveRequest
                 * .getNumberBaby(),
                 * bookingSaveRequest
                 * .getNumberChild(),
                 * numberOfDays - 1);
                 * List<Integer> feedingIDsAsIntegers = bookingSaveRequest
                 * .getFeedingIDs()
                 * .stream()
                 * .map(Long::intValue)
                 * .collect(Collectors.toList());
                 * return feedingRepository
                 * .findAllById(feedingIDsAsIntegers)
                 * .collectList()
                 * .flatMap(feedingList -> {
                 * BigDecimal extraCost = feedingList
                 * .stream()
                 * .map(feeding -> feeding
                 * .getCost()
                 * .multiply(
                 * BigDecimal.valueOf(
                 * bookingSaveRequest
                 * .getTotalCapacity())))
                 * .reduce(BigDecimal.ZERO,
                 * BigDecimal::add);
                 * 
                 * bookingEntity.setCostFinal(
                 * costFinal.add(extraCost));
                 * 
                 * return bookingRepository
                 * .findExistingBookings(
                 * bookingEntity.getRoomOfferId(),
                 * bookingEntity.getDayBookingInit(),
                 * bookingEntity.getDayBookingEnd())
                 * .hasElements()
                 * .flatMap(exists -> {
                 * if (exists) {
                 * return Mono.error(
                 * new CustomException(
                 * HttpStatus.BAD_REQUEST,
                 * "La reserva ya existe para las fechas seleccionadas"));
                 * } else {
                 * return bookingRepository
                 * .save(bookingEntity)
                 * .flatMap(savedBooking -> {
                 * 
                 * return saveBookingFeeding(
                 * Long.parseLong(savedBooking
                 * .getBookingId()
                 * .toString()),
                 * bookingSaveRequest
                 * .getFeedingIDs(),
                 * bookingSaveRequest
                 * .getTotalCapacity())
                 * .then(sendBookingConfirmationEmail(
                 * savedBooking,
                 * roomName,
                 * totalPeoples)
                 * .then(Mono.just(savedBooking)));
                 * });
                 * 
                 * }
                 * })
                 * .map(bookingEntity1 -> {
                 * if (bookingSaveRequest
                 * .getFinalCostumer() != null) {
                 * 
                 * bookingSaveRequest
                 * .getFinalCostumer()
                 * .stream()
                 * .map(finalCostumer -> finalCostumerRepository
                 * .save(FinalCostumer
                 * .toFinalCostumerEntity(
                 * bookingEntity1.getBookingId(),
                 * finalCostumer)));
                 * } else {
                 * userClientRepository
                 * .findById(userClientId)
                 * .map(userClient -> {
                 * FinalCostumer finalCostumer = FinalCostumer
                 * .builder()
                 * .firstName(userClient
                 * .getFirstName())
                 * .lastName(userClient
                 * .getLastName())
                 * .documentType(userClient
                 * .getDocumenttypeId() == 1
                 * ? "DNI"
                 * : "PAS")
                 * .documentNumber(
                 * userClient.getDocumentNumber())
                 * .yearOld(calculateAge(
                 * userClient.getBirthDate()))
                 * .build();
                 * finalCostumerRepository
                 * .save(
                 * FinalCostumer.toFinalCostumerEntity(
                 * bookingEntity1.getBookingId(),
                 * finalCostumer));
                 * return finalCostumer;
                 * });
                 * }
                 * return bookingEntity1;
                 * });
                 * });
                 */
                return getRoomName(bookingSaveRequest.getRoomOfferId())
                                .flatMap(roomName -> {
                                        // Obtener la oferta de habitación
                                        return roomOfferRepository.findById(bookingSaveRequest.getRoomOfferId())
                                                        .flatMap(roomOfferEntity -> {
                                                                // Crear la entidad de reserva con los datos
                                                                // proporcionados
                                                                BookingEntity bookingEntity = BookingEntity
                                                                                .createBookingEntity(userClientId,
                                                                                                bookingSaveRequest,
                                                                                                numberOfDays,
                                                                                                isPromotor,
                                                                                                isReceptionist);

                                                                // Cálculo del costo inicial (bebés, niños, adultos,
                                                                // etc.)
                                                                BigDecimal costFinal = GeneralMethods
                                                                                .calculateCostTotal(
                                                                                                bookingSaveRequest
                                                                                                                .getAdultCost()
                                                                                                                .floatValue(),
                                                                                                bookingSaveRequest
                                                                                                                .getAdultExtraCost()
                                                                                                                .floatValue(),
                                                                                                bookingSaveRequest
                                                                                                                .getAdultMayorCost()
                                                                                                                .floatValue(),
                                                                                                bookingSaveRequest
                                                                                                                .getKidCost()
                                                                                                                .floatValue(),
                                                                                                bookingSaveRequest
                                                                                                                .getInfantCost()
                                                                                                                .floatValue(),
                                                                                                bookingSaveRequest
                                                                                                                .getNumberAdult(),
                                                                                                bookingSaveRequest
                                                                                                                .getNumberAdultExtra(),
                                                                                                bookingSaveRequest
                                                                                                                .getNumberAdultMayor(),
                                                                                                bookingSaveRequest
                                                                                                                .getNumberBaby(),
                                                                                                bookingSaveRequest
                                                                                                                .getNumberChild(),
                                                                                                numberOfDays - 1);
                                                                List<Integer> feedingIDsAsIntegers = bookingSaveRequest
                                                                                .getFeedingIDs()
                                                                                .stream()
                                                                                .map(Long::intValue)
                                                                                .collect(Collectors.toList());
                                                                return feedingRepository
                                                                                .findAllById(feedingIDsAsIntegers)
                                                                                .collectList()
                                                                                .flatMap(feedingList -> {
                                                                                        BigDecimal extraCost = feedingList
                                                                                                        .stream()
                                                                                                        .map(feeding -> feeding
                                                                                                                        .getCost()
                                                                                                                        .multiply(
                                                                                                                                        BigDecimal.valueOf(
                                                                                                                                                        bookingSaveRequest
                                                                                                                                                                        .getTotalCapacity())))
                                                                                                        .reduce(BigDecimal.ZERO,
                                                                                                                        BigDecimal::add);
                                                                                        bookingEntity.setCostFinal(
                                                                                                        costFinal.add(extraCost));

                                                                                        // Verificar si ya existe una
                                                                                        // reserva para las fechas
                                                                                        // seleccionadas
                                                                                        return bookingRepository
                                                                                                        .findExistingBookings(
                                                                                                                        bookingEntity.getRoomOfferId(),
                                                                                                                        bookingEntity.getDayBookingInit(),
                                                                                                                        bookingEntity.getDayBookingEnd())
                                                                                                        .hasElements()
                                                                                                        .flatMap(exists -> {
                                                                                                                if (exists && bookingSaveRequest
                                                                                                                                .getBookingId() == null) {
                                                                                                                        // Si
                                                                                                                        // la
                                                                                                                        // reserva
                                                                                                                        // ya
                                                                                                                        // existe,
                                                                                                                        // lanzar
                                                                                                                        // una
                                                                                                                        // excepción
                                                                                                                        return Mono.error(
                                                                                                                                        new CustomException(
                                                                                                                                                        HttpStatus.BAD_REQUEST,
                                                                                                                                                        "La reserva ya existe para las fechas seleccionadas"));
                                                                                                                } else {
                                                                                                                        // Si
                                                                                                                        // la
                                                                                                                        // reserva
                                                                                                                        // no
                                                                                                                        // existe,
                                                                                                                        // guardarla
                                                                                                                        // en
                                                                                                                        // la
                                                                                                                        // base
                                                                                                                        // de
                                                                                                                        // datos
                                                                                                                        return bookingRepository
                                                                                                                                        .save(bookingEntity)
                                                                                                                                        .flatMap(savedBooking -> {
                                                                                                                                                // Guardar
                                                                                                                                                // los
                                                                                                                                                // datos
                                                                                                                                                // de
                                                                                                                                                // booking_feeding
                                                                                                                                                // después
                                                                                                                                                // de
                                                                                                                                                // guardar
                                                                                                                                                // booking
                                                                                                                                                return saveBookingFeeding(
                                                                                                                                                                Long.parseLong(savedBooking
                                                                                                                                                                                .getBookingId()
                                                                                                                                                                                .toString()),
                                                                                                                                                                bookingSaveRequest
                                                                                                                                                                                .getFeedingIDs(),
                                                                                                                                                                bookingSaveRequest
                                                                                                                                                                                .getTotalCapacity())
                                                                                                                                                                .then(sendBookingConfirmationEmail(
                                                                                                                                                                                savedBooking,
                                                                                                                                                                                roomName,
                                                                                                                                                                                totalPeoples,
                                                                                                                                                                                bookingSaveRequest
                                                                                                                                                                                                .isPaying())
                                                                                                                                                                                .then(Mono.just(savedBooking)));
                                                                                                                                        });
                                                                                                                }
                                                                                                        })
                                                                                                        .map(bookingEntity1 -> {
                                                                                                                if (bookingSaveRequest
                                                                                                                                .getFinalCostumer() != null) {
                                                                                                                        // Guardar
                                                                                                                        // los
                                                                                                                        // datos
                                                                                                                        // de
                                                                                                                        // los
                                                                                                                        // huéspedes
                                                                                                                        // finales
                                                                                                                        bookingSaveRequest
                                                                                                                                        .getFinalCostumer()
                                                                                                                                        .stream()
                                                                                                                                        .map(finalCostumer -> finalCostumerRepository
                                                                                                                                                        .save(FinalCostumer
                                                                                                                                                                        .toFinalCostumerEntity(
                                                                                                                                                                                        bookingEntity1.getBookingId(),
                                                                                                                                                                                        finalCostumer)));
                                                                                                                } else {
                                                                                                                        userClientRepository
                                                                                                                                        .findById(userClientId)
                                                                                                                                        .map(userClient -> {
                                                                                                                                                FinalCostumer finalCostumer = FinalCostumer
                                                                                                                                                                .builder()
                                                                                                                                                                .firstName(userClient
                                                                                                                                                                                .getFirstName())
                                                                                                                                                                .lastName(userClient
                                                                                                                                                                                .getLastName())
                                                                                                                                                                .documentType(userClient
                                                                                                                                                                                .getDocumenttypeId() == 1
                                                                                                                                                                                                ? "DNI"
                                                                                                                                                                                                : "PAS")
                                                                                                                                                                .documentNumber(
                                                                                                                                                                                userClient.getDocumentNumber())
                                                                                                                                                                .yearOld(calculateAge(
                                                                                                                                                                                userClient.getBirthDate()))
                                                                                                                                                                .build();
                                                                                                                                                finalCostumerRepository
                                                                                                                                                                .save(
                                                                                                                                                                                FinalCostumer.toFinalCostumerEntity(
                                                                                                                                                                                                bookingEntity1.getBookingId(),
                                                                                                                                                                                                finalCostumer));
                                                                                                                                                return finalCostumer;
                                                                                                                                        });
                                                                                                                }
                                                                                                                return bookingEntity1;
                                                                                                        });
                                                                                });
                                                        });
                                });
        }

        public Integer calculateAge(Timestamp birthDate) {
                LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate currentDate = LocalDate.now();
                return Period.between(birthDateLocal, currentDate).getYears();
        }

        // Método para enviar el correo de confirmación de reserva con el nombre de la
        // habitación
        private Mono<BookingEntity> sendBookingConfirmationEmail(BookingEntity bookingEntity, String roomName,
                        String totalPeoples, boolean isPaying) {
                if (isPaying) {
                        return Mono.empty();
                } else {
                        return userClientRepository.findByUserClientId(bookingEntity.getUserClientId())
                                        .flatMap(userClient -> {
                                                return paymentBookRepository
                                                                .countPaymentBookByBookingId(
                                                                                bookingEntity.getBookingId())
                                                                .flatMap(payment -> {
                                                                        int hidePayment = 1; // Por el momento es para
                                                                                             // ocultar el botón pagar
                                                                        BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
                                                                        String monthInit = TransformDate
                                                                                        .getAbbreviatedMonth(
                                                                                                        bookingEntity.getDayBookingInit());
                                                                        String monthEnd = TransformDate
                                                                                        .getAbbreviatedMonth(
                                                                                                        bookingEntity.getDayBookingEnd());
                                                                        int dayInit = TransformDate.getDayNumber(
                                                                                        bookingEntity.getDayBookingInit());
                                                                        int dayEnd = TransformDate.getDayNumber(
                                                                                        bookingEntity.getDayBookingEnd());
                                                                        long dayInterval = TransformDate
                                                                                        .calculateDaysDifference(
                                                                                                        bookingEntity.getDayBookingInit(),
                                                                                                        bookingEntity.getDayBookingEnd());
                                                                        baseEmailReserve.addEmailHandler(
                                                                                        new ConfirmReserveBookingTemplateEmail(
                                                                                                        monthInit,
                                                                                                        monthEnd,
                                                                                                        String.valueOf(dayInit),
                                                                                                        String.valueOf(
                                                                                                                        dayEnd),
                                                                                                        dayInterval,
                                                                                                        roomName,
                                                                                                        userClient.getFirstName(),
                                                                                                        String.valueOf(bookingEntity
                                                                                                                        .getBookingId()),
                                                                                                        totalPeoples,
                                                                                                        hidePayment));
                                                                        String emailBody = baseEmailReserve.execute();
                                                                        // Generar el cuerpo del correo electrónico con
                                                                        // el nombre de la habitación
                                                                        /*
                                                                         * String emailBody =
                                                                         * generateEmailBody(bookingEntity, roomName);
                                                                         */
                                                                        // Enviar el correo electrónico utilizando el
                                                                        // servicio de correo
                                                                        return emailService
                                                                                        .sendEmail(userClient
                                                                                                        .getEmail(),
                                                                                                        "Confirmación de Reserva",
                                                                                                        emailBody)
                                                                                        .thenReturn(bookingEntity);
                                                                });
                                        });
                }
        }

        // Método para calcular el número de días entre dos fechas
        private Integer calculateDaysBetween(LocalDate dayBookingInit, LocalDate dayBookingEnd) {
                return (int) ChronoUnit.DAYS.between(dayBookingInit, dayBookingEnd) + 1;
        }

        public Mono<BookingEntity> updateBookingState(Integer bookingId, Integer bookingStateId) {
                return bookingRepository.findById(bookingId)
                                .map(bookingEntity -> {
                                        bookingEntity.setBookingStateId(bookingStateId);
                                        return bookingEntity;
                                })
                                .flatMap(bookingEntity -> {
                                        PartnerPointsEntity partnerPointsEntity = PartnerPointsEntity.builder()
                                                        .userClientId(bookingEntity.getUserClientId())
                                                        .points(bookingEntity.getCostFinal().intValue() / RATIO_BASE)
                                                        .build();

                                        return partnerPointsService
                                                        .incrementPoints(partnerPointsEntity,
                                                                        partnerPointsEntity.getPoints())
                                                        .flatMap(bookingEntity1 -> bookingRepository
                                                                        .save(bookingEntity));
                                });
        }

        @Override
        public Mono<S3UploadResponse> loadBoucher(Mono<FilePart> file, Integer folderNumber, String token) {
                return file.flatMap(f -> {
                        WebClient webClient = WebClient.create();

                        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
                        bodyBuilder.part("file", f).contentType(MediaType.MULTIPART_FORM_DATA);
                        bodyBuilder.part("folderNumber", folderNumber);

                        return webClient.post()
                                        .uri(uploadDir)
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                                        .retrieve()
                                        .bodyToMono(S3UploadResponse.class)
                                        .map(S3UploadResponse::responseToEntity);
                });
        }

        @Override
        public Flux<ViewBookingReturn> findAllByUserClientIdAndBookingStateIdIn(Integer userClientId,
                        Integer bookingStateId) {
                /*
                 * return bookingRepository
                 * .findAllViewBookingReturnByUserClientIdAndBookingStateId(userClientId,
                 * bookingStateId)
                 * .flatMap(viewBookingReturn -> comfortTypeRepository
                 * .findAllByViewComfortType(viewBookingReturn.getBookingId())
                 * .collectList().map(comfortTypeEntity -> {
                 * viewBookingReturn.setListComfortType(comfortTypeEntity);
                 * return viewBookingReturn;
                 * }))
                 * .flatMap(viewBookingReturn -> bedsTypeRepository
                 * .findAllByViewBedsType(viewBookingReturn.getBookingId())
                 * .collectList().map(bedsTypeEntity -> {
                 * viewBookingReturn.setListBedsType(bedsTypeEntity);
                 * return viewBookingReturn;
                 * }));
                 */

                return bookingRepository
                                .findAllViewBookingReturnByUserClientIdAndBookingStateId(userClientId, bookingStateId)
                                .collectList()
                                .flatMapMany(listViews -> {
                                        List<Integer> ids = listViews.stream().map(ViewBookingReturn::getBookingId)
                                                        .collect(Collectors.toList());
                                        return Mono.zip(Mono.just(listViews), this.getBedsType(ids),
                                                        this.getComfortType(ids),this.getBookingFeeding(ids));
                                }).flatMap(data -> {
                                        List<ViewBookingReturn> listViews = data.getT1();
                                        List<ViewBookingReturn.BedsType> bedsType = data.getT2();
                                        List<ViewBookingReturn.ComfortData> comfortData = data.getT3();
                                        List<com.proriberaapp.ribera.Api.controllers.client.dto.BookingFeedingDto> bookingFeeding = data.getT4();
                                        listViews.forEach(view -> {
                                                view.setListBedsType(bedsType.stream()
                                                                .filter(bed -> bed.getBookingId()
                                                                                .equals(view.getBookingId()))
                                                                .collect(Collectors.toList()));
                                                view.setListComfortType(comfortData.stream()
                                                                .filter(comfort -> comfort.getBookingId()
                                                                                .equals(view.getBookingId()))
                                                                .collect(Collectors.toList()));
                                                view.setListFeeding(bookingFeeding.stream()                                                          
                                                                .filter(feeding -> feeding.getBookingId()
                                                                                .equals(view.getBookingId()))
                                                                .collect(Collectors.toList()));
                                        });

                                        return Flux.fromIterable(listViews);
                                });

        }

        private Mono<List<ViewBookingReturn.BedsType>> getBedsType(List<Integer> bookingsId) {
                return Mono.defer(() -> {
                        return bedsTypeRepository.findAllByViewBedsTypeByBookings(bookingsId).collectList();
                });
        }


        private Mono<List<ViewBookingReturn.ComfortData>> getComfortType(List<Integer> bookingsId) {
                return Mono.defer(() -> {
                        return comfortTypeRepository.findAllByViewComfortTypeByBookings(bookingsId).collectList();
                });
        }
        private Mono<List<com.proriberaapp.ribera.Api.controllers.client.dto.BookingFeedingDto>> getBookingFeeding(
                        List<Integer> bookingsId) {
                return Mono.defer(() -> {
                        return bookingFeedingRepository.listBookingFeedingByBookingId(bookingsId).collectList();
                });
        }



        @Override
        public Mono<ViewBookingReturn> findByUserClientIdAndBookingIdAndBookingStateIdIn(Integer userClientId,
                        Integer bookingId, Integer bookingStateId) {
                return bookingRepository
                                .findViewBookingReturnByUserClientIdAndBookingIdAndBookingStateId(userClientId,
                                                bookingId,
                                                bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByUserPromoterIdAndBookingStateIdIn(Integer userPromoterId,
                        Integer bookingStateId) {
                return bookingRepository
                                .findAllViewBookingReturnByUsePromoterIdAndBookingStateId(userPromoterId,
                                                bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByReceptionistIdAndBookingStateIdIn(Integer receptionistId,
                        Integer bookingStateId) {
                return bookingRepository
                                .findAllViewBookingReturnByReceptionistIdAndBookingStateId(receptionistId,
                                                bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByRoomTypeIdAndUserClientIdAndBookingStateId(Integer roomTypeId,
                        Integer userClientId, Integer bookingStateId) {
                return bookingRepository
                                .findAllViewBookingReturnByRoomTypeIdAndUserClientIdAndBookingStateId(roomTypeId,
                                                userClientId,
                                                bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(
                        Timestamp dayBookingInit, Timestamp dayBookingEnd, Integer userClientId,
                        Integer bookingStateId) {
                return bookingRepository
                                .findAllViewBookingReturnByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(
                                                dayBookingInit, dayBookingEnd, userClientId, bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
                        Integer numberAdults, Integer numberChildren, Integer numberBabies, Integer userClientId,
                        Integer bookingStateId) {
                return bookingRepository
                                .findAllViewBookingReturnByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
                                                numberAdults, numberChildren, numberBabies, userClientId,
                                                bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByBookingStateId(Integer bookingStateId) {
                return bookingRepository.findAllViewBookingReturnByBookingStateId(bookingStateId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllByUserClientIdAndBookingIn(Integer userClientId) {
                return bookingRepository.findAllViewBookingReturnByUserClientId(userClientId)
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Flux<ViewBookingReturn> findAllView() {
                return bookingRepository.findAllViewBookingReturn()
                                .flatMap(viewBookingReturn -> comfortTypeRepository
                                                .findAllByViewComfortType(viewBookingReturn.getBookingId())
                                                .collectList().map(comfortTypeEntity -> {
                                                        viewBookingReturn.setListComfortType(comfortTypeEntity);
                                                        return viewBookingReturn;
                                                }))
                                .flatMap(viewBookingReturn -> bedsTypeRepository
                                                .findAllByViewBedsType(viewBookingReturn.getBookingId())
                                                .collectList().map(bedsTypeEntity -> {
                                                        viewBookingReturn.setListBedsType(bedsTypeEntity);
                                                        return viewBookingReturn;
                                                }));
        }

        @Override
        public Mono<BookingEntity> findByIdAndIdUserAdmin(Integer idUserAdmin, Integer bookingId) {
                return bookingRepository.findByBookingIdAndUserClientId(idUserAdmin, bookingId);
        }

        private Mono<Void> saveBookingFeeding(Long bookingId, List<Long> feedingIds, Integer totalCapacity) {
                return Flux.fromIterable(feedingIds)
                                .flatMap(feedingId -> {
                                        // Obtener el precio del alimento desde el feedingRepository
                                        return feedingRepository.findById(feedingId.intValue())
                                                        .flatMap(feedingEntity -> {
                                                                BookingFeedingEntity bookingFeeding = new BookingFeedingEntity();
                                                                bookingFeeding.setBookingId(bookingId);
                                                                bookingFeeding.setFeedingId(feedingId);

                                                                // Calcular el monto (precio del alimento * capacidad
                                                                // total)
                                                                BigDecimal feedingAmount = feedingEntity.getCost()
                                                                                .multiply(BigDecimal.valueOf(
                                                                                                totalCapacity));
                                                                bookingFeeding.setBookingfeedingamout(
                                                                                feedingAmount.floatValue());
                                                                System.out.println("feedingAmount: " + feedingAmount);
                                                                System.out.println("bookingFeeding: "
                                                                                + bookingFeeding.getFeedingId());
                                                                // Guardar el BookingFeedingEntity en la base de datos
                                                                return bookingFeedingRepository.save(bookingFeeding);
                                                        });
                                })
                                .then(); // Retorna un Mono<Void> cuando se completan todos los guardados
        }

        @Override
        public Mono<Void> saveBookingWithFeedings(BookingFeedingDto requestDTO) {
                return Flux.fromIterable(requestDTO.getFeedingIds())
                                .flatMap(feedingId -> {
                                        BookingFeedingEntity bookingFeeding = new BookingFeedingEntity();
                                        bookingFeeding.setFeedingId(feedingId);
                                        return bookingFeedingRepository.save(bookingFeeding);
                                })
                                .then();
        }

        @Override
        public Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateId(Integer stateId, Integer month,
                        Integer year) {
                return bookingRepository.findBookingsWithPaymentByStateId(stateId, month, year);
        }

        @Override
        public Mono<BigDecimal> totalPaymentSum(Integer stateId, Integer month, Integer year) {
                return bookingRepository.findBookingsWithPaymentByStateId(stateId, month, year)
                                .map(BookingWithPaymentDTO::getTotalCost)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        @Override
        public Mono<TotalSalesDTO> totalPaymentMonthSum(Integer stateId, Integer month, Integer year) {
                TotalSalesDTO resp = new TotalSalesDTO();
                return bookingRepository.getTotalSalesByMonth(stateId, month, year).flatMap(totalMonth -> {
                        return bookingRepository.getTotalSalesBeforeMonth(stateId, month, year)
                                        .flatMap(totalLastMonth -> {
                                                resp.setTotalMonth(totalMonth);
                                                resp.setTotalLastMonth(totalLastMonth);
                                                return Mono.just(resp);
                                        });
                });
        }

        @Override
        public Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateIdAndDate(Integer stateId,
                        LocalDateTime dateini, LocalDateTime datefin) {
                return bookingRepository.findBookingsWithPaymentByStateIdAndDate(stateId, dateini, datefin);
        }

        @Override
        public Mono<TotalCalculationMonthsDTO> totalPaymentMonthSum(Integer month, Integer year) {
                TotalCalculationMonthsDTO resp = new TotalCalculationMonthsDTO();
                return bookingRepository.getTotalCancellSales(month, year).flatMap(totalMonth -> {
                        return bookingRepository.getTotalCancellLastSales(month, year).flatMap(totalLastMonth -> {
                                resp.setTotalMonth(totalMonth);
                                resp.setTotalLastMonth(totalLastMonth);
                                return Mono.just(resp);
                        });
                });
        }

        @Override
        public Flux<BookingResumenPaymentDTO> findBookingsWithResumeByStateId(Integer stateId, Integer month,
                        Integer year) {
                return bookingRepository.findBookingsWithResumeByStateId(stateId, month, year);
        }

        @Override
        public Mono<BigDecimal> getTotalBeforeYear() {
                return bookingRepository.getTotalBeforeYear();
        }

        @Override
        public Mono<Long> getTotalActiveClients(Integer stateId, Integer month) {
                return bookingRepository.getTotalActiveClients(stateId, month);
        }

        @Override
        public Mono<TotalCalculationMonthsDTO> getTotalActiveClientsMonths(Integer stateId, Integer month) {
                TotalCalculationMonthsDTO resp = new TotalCalculationMonthsDTO();
                return bookingRepository.getTotalActiveClients(stateId, month).flatMap(totalMonth -> bookingRepository
                                .getTotalActiveClientsMonths(stateId, month).flatMap(lastTotalMonth -> {
                                        resp.setTotalMonth(totalMonth);
                                        resp.setTotalLastMonth(lastTotalMonth);
                                        return Mono.just(resp);
                                }));
        }

        @Override
        public Mono<Void> updateState(Integer stateId, Integer bookingId) {
                return bookingRepository.updateState(stateId, bookingId);

        }

        @Override
        public Flux<Long> getAllYearsInvoice() {
                return bookingRepository.getAllYearsInvoice();
        }

}
