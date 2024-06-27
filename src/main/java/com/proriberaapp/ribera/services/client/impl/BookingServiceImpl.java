package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
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
            RoomOfferRepository roomOfferRespository
            //WebClient.Builder webClientBuilder
    ) {
        this.bookingRepository = bookingRepository;
        this.userClientRepository = userClientRepository;
        this.emailService = emailService;
        this.partnerPointsService = partnerPointsService;
        this.comfortTypeRepository = comfortTypeRepository;
        this.bedsTypeRepository = bedsTypeRepository;
        this.roomOfferRespository = roomOfferRespository;
        //this.webClient = webClientBuilder.baseUrl(uploadDir).build();
    }

    private String generateEmailBody(BookingEntity bookingEntity) {
        return "Estimado usuario,\n\n" +
                "Enviamos su reserva:\n" +
                "Booking ID: " + bookingEntity.getBookingId() + "\n" +
                "Room Offer ID: " + bookingEntity.getRoomOfferId() + "\n" +
                "Costo: " + bookingEntity.getCostFinal() + "\n" +
                "Fecha de inicio: " + bookingEntity.getDayBookingInit() + "\n" +
                "Fecha de fin: " + bookingEntity.getDayBookingEnd() + "\n\n" +
                "Gracias por su confianza. Atentamente. Jefito y su equipo.";
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
                        .flatMap(savedBooking ->
                                userClientRepository.findByUserClientId(userClientId)
                                        .flatMap(userClient -> {
                                            String emailBody = generateEmailBody(savedBooking);
                                            return emailService.sendEmail(userClient.getEmail(), "Booking Confirmation", emailBody)
                                                    .thenReturn(savedBooking);
                                        })
                        )
                );
    }

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
