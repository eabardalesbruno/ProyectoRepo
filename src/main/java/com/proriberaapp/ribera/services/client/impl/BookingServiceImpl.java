package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.*;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.PartnerPointsService;
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

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

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
    //private final WebClient webClient;
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
            CancelPaymentRepository cancelPaymentRepository, RefusePaymentRepository refusePaymentRepository, BedsTypeRepository bedsTypeRepository,
            RoomOfferRepository roomOfferRepository, FinalCostumerRepository finalCostumerRepository,
            PaymentBookRepository paymentBookRepository, RoomRepository roomRepository
            //WebClient.Builder webClientBuilder
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
        //this.webClient = webClientBuilder.baseUrl(uploadDir).build();
    }

    @Override
    public Mono<BigDecimal> getRiberaPointsByBookingId(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .flatMap(booking -> roomOfferRepository.findById(booking.getRoomOfferId()))
                .map(roomOffer -> {
                    // Convert Integer to BigDecimal
                    Integer riberaPoints = roomOffer.getRiberaPoints();
                    return riberaPoints != null ? BigDecimal.valueOf(riberaPoints) : BigDecimal.ZERO;
                });
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
                bookingStateId, roomTypeId, capacity, offertimeInit, offertimeEnd, size, offset);

        Mono<Long> totalElements = bookingRepository.countBookingsByStateId(
                bookingStateId, roomTypeId, capacity, offertimeInit, offertimeEnd);

        return bookings.collectList()
                .zipWith(totalElements)
                .map(tuple -> new PaginatedResponse<>(tuple.getT2(), tuple.getT1()));
    }

    @Override
    public Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginatedAndUserId(Integer bookingStateId, Integer roomTypeId, Integer capacity, LocalDateTime offertimeInit, LocalDateTime offertimeEnd, int page, int size, Integer userId) {
        int offset = page * size;

        Flux<BookingStates> bookings = bookingRepository.findBookingsByStateIdPaginatedAndUserId(
                bookingStateId, roomTypeId, capacity, offertimeInit, offertimeEnd, size, offset,userId);

        Mono<Long> totalElements = bookingRepository.countBookingsByStateIdAndUserId(
                bookingStateId, roomTypeId, capacity, offertimeInit, offertimeEnd,userId);

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
                    return paymentBookRepository.findAllByBookingIdAndCancelReasonIdIsNull(booking.getBookingId())
                            .collectList()
                            .flatMap(payments -> {
                                if (!payments.isEmpty()) {
                                    return Mono.empty();
                                } else {
                                    return bookingRepository.deleteById(booking.getBookingId());
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

    // Método para generar el cuerpo del correo electrónico con el nombre de la habitación
    private String generateEmailBody(BookingEntity bookingEntity, String roomName) {
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Bienvenido</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: black;\n" +
                "            background-color: white;\n" +
                "        }\n" +
                "        .header {\n" +
                "            width: 100%;\n" +
                "            position: relative;\n" +
                "            background-color: white;\n" +
                "            padding: 20px 0;\n" +
                "        }\n" +
                "        .logo-left {\n" +
                "            width: 50px;\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            left: 10px;\n" +
                "        }\n" +
                "        .banner {\n" +
                "            width: 100%;\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 500px;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: left;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .content h3 {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: block;\n" +
                "            width: 200px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 10px;\n" +
                "            background-color: green;\n" +
                "            color: white;\n" +
                "            text-align: center;\n" +
                "            border-radius: 5px;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .help-section {\n" +
                "            width: 500px;\n" +
                "            background-color: #f4f4f4;\n" +
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
                "        <!-- Encabezado con logo -->\n" +
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1>Gracias por su preferencia!</h1>\n" +
                "            <h3>Estimado cliente</h3>\n" +
                "            <p>Se completo exitosamente el registro de su reserva de: <strong>" + roomName + "</strong>. Por favor, no se olvide de pagar su reserva.</p>\n" +
                "            <div style=\"background-color: #e0e0e0; padding: 10px; border-radius: 5px;\">\n" +
                "                <p><strong>Los datos de tu reserva</strong></p>\n" +
                "                <p>Habitacion: " + roomName + "</p>\n" +
                "                <p>Costo: " + bookingEntity.getCostFinal() + "</p>\n" +
                "                <p>Fecha de inicio: " + bookingEntity.getDayBookingInit() + "</p>\n" +
                "                <p>Fecha de fin: " + bookingEntity.getDayBookingEnd() + "</p>\n" +
                "            </div>\n" +
                "            <a href=\"http://www.cieneguillariberadelrio.com/reservas\" class=\"button\">Pagar ahora</a>\n" +
                "            <p>Recuerde que el pago lo puede realizar mediante deposito en nuestra cuenta a traves de agente BCP, agencias o cualquier metodo de pago dentro de la plataforma usando este enlace: <a href=\"http://www.cieneguillariberadelrio.com/reservas\">www.cieneguillariberadelrio.com/reservas</a></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Sección de ayuda -->\n" +
                "    <div class=\"help-section\">\n" +
                "        <h3>Necesitas ayuda?</h3>\n" +
                "        <p>Envie sus comentarios e informacion de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>\n" +
                "    </div>\n" +
                "</body>\n" +
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
                                    bookingEntity1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                                    return !bookingEntities.contains(bookingEntity1);
                                }
                        ).toList()
                ));
    }

    @Override
    public Mono<BigDecimal> getCostFinalByBookingId(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .flatMap(bookingEntity -> {
                    if (bookingEntity != null) {
                        return Mono.just(bookingEntity.getCostFinal());
                    } else {
                        return Mono.error(new RuntimeException("BookingEntity not found for bookingId: " + bookingId));
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
    //TODO: por cada 50 dolares es un punto
    //TODO: silver 10 alojamientos, gold 15, premium 20
    //TODO: considerar el consumo

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

                    return partnerPointsService.incrementPoints(partnerPointsEntity, partnerPointsEntity.getPoints())
                            .flatMap(updatedBookingEntity -> bookingRepository.save(bookingEntity));
                });
    }

    @Override
    public Flux<CalendarDate> calendarDate(Integer id) {
        return bookingRepository.findAllCalendarDate(id);
    }

    /*
    @Override
    public Mono<BookingEntity> save(Integer userClientId, BookingSaveRequest bookingSaveRequest) {

        if (bookingSaveRequest.getDayBookingInit().isBefore(LocalDate.now())) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST,"La fecha de inicio no puede ser anterior al día actual"));
        }
        assert bookingSaveRequest.getNumberBaby() != null;
        assert bookingSaveRequest.getNumberChild() != null;
        assert bookingSaveRequest.getNumberAdult() != null;
        if(bookingSaveRequest.getNumberBaby()+bookingSaveRequest.getNumberChild()+bookingSaveRequest.getNumberAdult() == 0){
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Debe seleccionar al menos un huésped"));
        }
        
        Integer numberOfDays = calculateDaysBetween(bookingSaveRequest.getDayBookingInit(), bookingSaveRequest.getDayBookingEnd());

        Mono<RoomOfferEntity> roomOfferEntityMono = roomOfferRespository.findById(bookingSaveRequest.getRoomOfferId());

        BookingEntity bookingEntity = BookingEntity.createBookingEntity(userClientId, bookingSaveRequest, numberOfDays);

        return bookingRepository.findExistingBookings(
                        bookingEntity.getRoomOfferId(),
                        bookingEntity.getDayBookingInit(),
                        bookingEntity.getDayBookingEnd())
                .hasElements()
                .flatMap(exists -> exists
                        ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST,"La reserva ya existe para las fechas seleccionadas"))
                        : Mono.just(bookingEntity)
                        .flatMap(bookingEntity1 -> roomOfferEntityMono
                                .map(roomOfferEntity -> {
                                    bookingEntity1.setCostFinal(roomOfferEntity.getCost().multiply(BigDecimal.valueOf(numberOfDays)));
                                    return bookingEntity1;
                                })
                        )
                        .flatMap(bookingRepository::save)
                );
    }
     */

    @Override
    public Mono<BookingEntity> save(Integer userClientId, BookingSaveRequest bookingSaveRequest) {
        // Validar que la fecha de inicio no sea anterior al día actual
        if (bookingSaveRequest.getDayBookingInit().isBefore(LocalDate.now())) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "La fecha de inicio no puede ser anterior al día actual"));
        }
        System.out.println(bookingSaveRequest.getNumberBaby());
        System.out.println(bookingSaveRequest.getNumberChild());
        System.out.println(bookingSaveRequest.getNumberAdult());
        System.out.println(bookingSaveRequest.getNumberAdultExtra());
        // Calcular el número de días entre la fecha de inicio y fin
        Integer numberOfDays = calculateDaysBetween(bookingSaveRequest.getDayBookingInit(), bookingSaveRequest.getDayBookingEnd());


        // Obtener el nombre de la habitación
        return getRoomName(bookingSaveRequest.getRoomOfferId())
                .flatMap(roomName -> {
                    // Obtener la oferta de habitación
                    return roomOfferRepository.findById(bookingSaveRequest.getRoomOfferId())
                            .flatMap(roomOfferEntity -> {
                                // Crear la entidad de reserva con los datos proporcionados
                                BookingEntity bookingEntity = BookingEntity.createBookingEntity(userClientId, bookingSaveRequest, numberOfDays);
                                bookingEntity.setCostFinal(roomOfferEntity.getCost().multiply(BigDecimal.valueOf(numberOfDays)));

                                // Verificar si ya existe una reserva para las fechas seleccionadas
                                return bookingRepository.findExistingBookings(
                                                bookingEntity.getRoomOfferId(),
                                                bookingEntity.getDayBookingInit(),
                                                bookingEntity.getDayBookingEnd())
                                        .hasElements()
                                        .flatMap(exists -> {
                                            if (exists) {
                                                // Si la reserva ya existe, lanzar una excepción
                                                return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "La reserva ya existe para las fechas seleccionadas"));
                                            } else {
                                                // Si la reserva no existe, guardarla en la base de datos
                                                return bookingRepository.save(bookingEntity)
                                                        .flatMap(savedBooking -> sendBookingConfirmationEmail(savedBooking, roomName)
                                                                .then(Mono.just(savedBooking)));
                                            }
                                        })
                                        .map(bookingEntity1 -> {
                                            if (bookingSaveRequest.getFinalCostumer() != null) {
                                                // Guardar los datos de los huéspedes finales
                                                bookingSaveRequest.getFinalCostumer().stream()
                                                        .map(finalCostumer ->
                                                                finalCostumerRepository.save(FinalCostumer.toFinalCostumerEntity(bookingEntity1.getBookingId(), finalCostumer))
                                                        );
                                            } else {
                                                userClientRepository.findById(userClientId)
                                                        .map(userClient -> {
                                                            FinalCostumer finalCostumer = FinalCostumer.builder()
                                                                    .firstName(userClient.getFirstName())
                                                                    .lastName(userClient.getLastName())
                                                                    .documentType(userClient.getDocumenttypeId() == 1 ? "DNI" : "PAS")
                                                                    .documentNumber(userClient.getDocumentNumber())
                                                                    .yearOld(calculateAge(userClient.getBirthDate()))
                                                                    .build();
                                                            finalCostumerRepository.save(FinalCostumer.toFinalCostumerEntity(bookingEntity1.getBookingId(), finalCostumer));
                                                            return finalCostumer;
                                                        });
                                            }
                                            return bookingEntity1;
                                        });
                            });
                });
    }

    public Integer calculateAge(Timestamp birthDate) {
        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDateLocal, currentDate).getYears();
    }

    // Método para enviar el correo de confirmación de reserva con el nombre de la habitación
    private Mono<BookingEntity> sendBookingConfirmationEmail(BookingEntity bookingEntity, String roomName) {
        return userClientRepository.findByUserClientId(bookingEntity.getUserClientId())
                .flatMap(userClient -> {
                    // Generar el cuerpo del correo electrónico con el nombre de la habitación
                    String emailBody = generateEmailBody(bookingEntity, roomName);
                    // Enviar el correo electrónico utilizando el servicio de correo
                    return emailService.sendEmail(userClient.getEmail(), "Confirmación de Reserva", emailBody)
                            .thenReturn(bookingEntity);
                });
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

                            return partnerPointsService.incrementPoints(partnerPointsEntity, partnerPointsEntity.getPoints())
                                    .flatMap(bookingEntity1 -> bookingRepository.save(bookingEntity));
                        }
                );
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
    public Flux<ViewBookingReturn> findAllByUserClientIdAndBookingStateIdIn(Integer userClientId, Integer bookingStateId) {
        return bookingRepository.findAllViewBookingReturnByUserClientIdAndBookingStateId(userClientId, bookingStateId)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Flux<ViewBookingReturn> findAllByRoomTypeIdAndUserClientIdAndBookingStateId(Integer roomTypeId, Integer userClientId, Integer bookingStateId) {
        return bookingRepository.findAllViewBookingReturnByRoomTypeIdAndUserClientIdAndBookingStateId(roomTypeId, userClientId, bookingStateId)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Flux<ViewBookingReturn> findAllByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(Timestamp dayBookingInit, Timestamp dayBookingEnd, Integer userClientId, Integer bookingStateId) {
        return bookingRepository.findAllViewBookingReturnByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(dayBookingInit, dayBookingEnd, userClientId, bookingStateId)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Flux<ViewBookingReturn> findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(Integer numberAdults, Integer numberChildren, Integer numberBabies, Integer userClientId, Integer bookingStateId) {
        return bookingRepository.findAllViewBookingReturnByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(numberAdults, numberChildren, numberBabies, userClientId, bookingStateId)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Flux<ViewBookingReturn> findAllByBookingStateId(Integer bookingStateId) {
        return bookingRepository.findAllViewBookingReturnByBookingStateId(bookingStateId)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Flux<ViewBookingReturn> findAllByUserClientIdAndBookingIn(Integer userClientId) {
        return bookingRepository.findAllViewBookingReturnByUserClientId(userClientId)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Flux<ViewBookingReturn> findAllView() {
        return bookingRepository.findAllViewBookingReturn()
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Mono<BookingEntity> findByIdAndIdUserAdmin(Integer idUserAdmin, Integer bookingId) {
        return bookingRepository.findByBookingIdAndUserClientId(idUserAdmin, bookingId);
    }

}
