package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final RoomOfferRepository roomOfferRespository;

    private final BedsTypeRepository bedsTypeRepository;
    private final RoomRepository roomRepository;
    //private final WebClient webClient;
    @Value("${app.upload.dir}")
    private String uploadDir;
    @Value("${app.upload.folderDir}")
    private String folderDir;
    @Value("${room.offer.ratio.base}")
    private Integer RATIO_BASE;


    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserClientRepository userClientRepository,
            EmailService emailService,
            PartnerPointsService partnerPointsService,
            ComfortTypeRepository comfortTypeRepository,
            BedsTypeRepository bedsTypeRepository,
            RoomOfferRepository roomOfferRespository,
            RoomRepository roomRepository
            //WebClient.Builder webClientBuilder
    ) {
        this.bookingRepository = bookingRepository;
        this.userClientRepository = userClientRepository;
        this.emailService = emailService;
        this.partnerPointsService = partnerPointsService;
        this.comfortTypeRepository = comfortTypeRepository;
        this.bedsTypeRepository = bedsTypeRepository;
        this.roomOfferRespository = roomOfferRespository;
        this.roomRepository = roomRepository;
        //this.webClient = webClientBuilder.baseUrl(uploadDir).build();
    }

    private Mono<String> getRoomName(Integer roomOfferId) {
        return roomOfferRespository.findById(roomOfferId)
                .flatMap(roomOfferEntity -> roomRepository.findById(roomOfferEntity.getRoomId()))
                .map(RoomEntity::getRoomName)
                .switchIfEmpty(Mono.just("Habitación no encontrada"));
    }

    // Método para generar el cuerpo del correo electrónico con el nombre de la habitación
    private String generateEmailBody(BookingEntity bookingEntity, String roomName) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Bienvenido </h1>";
        body += "<h3 style='text-align: center;'>Producto por Adquirir: Reserva</h3>";
        body += "<h3 style='text-align: center;'>Descripcion: Reserva de Habitacion</h3>";
        body += "<h2 style='text-align: center;'>Detalles de la reserva:</h2>";
        body += "<p style='text-align: center;'>Habitación: " + roomName + "</p>";
        body += "<p style='text-align: center;'>Costo: " + bookingEntity.getCostFinal() + "</p>";
        body += "<p style='text-align: center;'>Fecha de inicio: " + bookingEntity.getDayBookingInit() + "</p>";
        body += "<p style='text-align: center;'>Fecha de fin: " + bookingEntity.getDayBookingEnd() + "</p>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Recuerda que el pago lo puedes realizar mediante los medios de pago disponibles en el portal.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div></center>";
        body += "</body></html>";

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

        // Calcular el número de días entre la fecha de inicio y fin
        Integer numberOfDays = calculateDaysBetween(bookingSaveRequest.getDayBookingInit(), bookingSaveRequest.getDayBookingEnd());

        // Obtener el nombre de la habitación
        return getRoomName(bookingSaveRequest.getRoomOfferId())
                .flatMap(roomName -> {
                    // Obtener la oferta de habitación
                    return roomOfferRespository.findById(bookingSaveRequest.getRoomOfferId())
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
                                                        .flatMap(savedBooking -> sendBookingConfirmationEmail(savedBooking, roomName));
                                            }
                                        });
                            });
                });
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

    private MultiValueMap buildMultipartData(String folderNumber, byte[] imageBytes) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", imageBytes)
                .filename("image.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-Disposition", "form-data; name=\"file\"; filename=\"image.jpg\"");
        builder.part("folderNumber", folderNumber);
        return builder.build();
    }

}
