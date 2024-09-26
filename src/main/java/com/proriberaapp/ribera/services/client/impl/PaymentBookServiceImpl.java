package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.PaymentBookService;
import com.proriberaapp.ribera.services.client.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PaymentBookServiceImpl implements PaymentBookService {
    /*ANTES DEL CORREO
    private final PaymentBookRepository paymentBookRepository;
    private final BookingService bookingService;

    private final S3Uploader s3Uploader;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,BookingService bookingService, S3Uploader s3Uploader) {
        this.paymentBookRepository = paymentBookRepository;
        this.bookingService = bookingService;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook);
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity paymentBook) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        paymentBook.setPaymentDate(timestamp);
        return paymentBookRepository.save(paymentBook)
                .flatMap(savedPaymentBook -> updateBookingStateIfRequired(savedPaymentBook.getBookingId()).thenReturn(savedPaymentBook));
    }

    @Override
    public Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook) {
        return paymentBookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setBookingId(paymentBook.getBookingId());
                    existingBook.setUserClientId(paymentBook.getUserClientId());
                    existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
                    existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
                    existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
                    existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
                    existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
                    existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
                    existingBook.setAmount(paymentBook.getAmount());
                    existingBook.setDescription(paymentBook.getDescription());
                    existingBook.setPaymentDate(paymentBook.getPaymentDate());
                    existingBook.setOperationCode(paymentBook.getOperationCode());
                    existingBook.setNote(paymentBook.getNote());
                    existingBook.setTotalCost(paymentBook.getTotalCost());
                    existingBook.setImageVoucher(paymentBook.getImageVoucher());
                    existingBook.setTotalPoints(paymentBook.getTotalPoints());
                    existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
                    return paymentBookRepository.save(existingBook);
                });
    }

    @Override
    public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    @Override
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookRepository.findAll();
    }

    @Override
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId) {
        return paymentBookRepository.findByUserClientId(userClientId);
    }

    @Override
    public Mono<Void> deletePaymentBook(Integer id) {
        return paymentBookRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updateBookingStateIfRequired(Integer bookingId) {
        return bookingService.updateBookingStatePay(bookingId, 3) // Aquí verificas y actualizas el estado a 3
                .filter(booking -> booking.getBookingStateId() == 2) // Verifica si el estado es 2
                .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2)) // Si es 2, lo actualizas
                .then();
    }
    @Override
    public Mono<PaymentBookEntity> findById(Integer id) {
        return paymentBookRepository.findById(id);
    }
     */

    /*DESPUES DE CORREO
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final BookingService bookingService;
    private final S3Uploader s3Uploader;
    private final EmailService emailService;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
                                  UserClientRepository userClientRepository,
                                  BookingService bookingService,
                                  S3Uploader s3Uploader,
                                  EmailService emailService) {
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingService = bookingService;
        this.s3Uploader = s3Uploader;
        this.emailService = emailService;
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook);
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity paymentBook) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook)
                .flatMap(savedPaymentBook -> {
                    return updateBookingStateIfRequired(savedPaymentBook.getBookingId())
                            .then(userClientRepository.findById(savedPaymentBook.getUserClientId())
                                    .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook, userClient.getEmail()))
                            )
                            .thenReturn(savedPaymentBook);
                });
    }

    @Override
    public Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook) {
        return paymentBookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setBookingId(paymentBook.getBookingId());
                    existingBook.setUserClientId(paymentBook.getUserClientId());
                    existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
                    existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
                    existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
                    existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
                    existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
                    existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
                    existingBook.setAmount(paymentBook.getAmount());
                    existingBook.setDescription(paymentBook.getDescription());
                    existingBook.setPaymentDate(paymentBook.getPaymentDate());
                    existingBook.setOperationCode(paymentBook.getOperationCode());
                    existingBook.setNote(paymentBook.getNote());
                    existingBook.setTotalCost(paymentBook.getTotalCost());
                    existingBook.setImageVoucher(paymentBook.getImageVoucher());
                    existingBook.setTotalPoints(paymentBook.getTotalPoints());
                    existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
                    return paymentBookRepository.save(existingBook);
                });
    }

    @Override
    public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    @Override
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookRepository.findAll();
    }

    @Override
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId) {
        return paymentBookRepository.findByUserClientId(userClientId);
    }

    @Override
    public Mono<Void> deletePaymentBook(Integer id) {
        return paymentBookRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updateBookingStateIfRequired(Integer bookingId) {
        return bookingService.updateBookingStatePay(bookingId, 3) // Aquí verificas y actualizas el estado a 3
                .filter(booking -> booking.getBookingStateId() == 2) // Verifica si el estado es 2
                .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2)) // Si es 2, lo actualizas
                .then();
    }

    @Override
    public Mono<PaymentBookEntity> findById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity paymentBook, String email) {
        String emailBody = generatePaymentConfirmationEmailBody(paymentBook);
        return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
    }

    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<h1 style='text-align: center;'>Confirmación de Pago</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido recibido con éxito.</p>";
        body += "<p>Detalles del pago:</p>";
        body += "<ul>";
        body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
        body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
        body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
        body += "</ul>";
        body += "<p>Gracias por su confianza.</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }
     */
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final BookingService bookingService;
    private final RoomOfferRepository roomOfferRepository;
    private final RoomRepository roomRepository;
    private final S3Uploader s3Uploader;
    private final EmailService emailService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStateRepository paymentStateRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentSubtypeRepository paymentSubtypeRepository;

    private final CurrencyTypeRepository currencyTypeRepository;


    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
                                  UserClientRepository userClientRepository,
                                  BookingService bookingService,
                                  RoomOfferRepository roomOfferRepository, RoomRepository roomRepository, S3Uploader s3Uploader,
                                  EmailService emailService,
                                  PaymentMethodRepository paymentMethodRepository,
                                  PaymentStateRepository paymentStateRepository, PaymentTypeRepository paymentTypeRepository, PaymentSubtypeRepository paymentSubtypeRepository, CurrencyTypeRepository currencyTypeRepository) {
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingService = bookingService;
        this.roomOfferRepository = roomOfferRepository;
        this.roomRepository = roomRepository;
        this.s3Uploader = s3Uploader;
        this.emailService = emailService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStateRepository = paymentStateRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.paymentSubtypeRepository = paymentSubtypeRepository;
        this.currencyTypeRepository = currencyTypeRepository;

    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook);
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity paymentBook) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook)
                .flatMap(savedPaymentBook -> updateBookingStateIfRequired(savedPaymentBook.getBookingId())
                        .then(userClientRepository.findById(savedPaymentBook.getUserClientId())
                                .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook, userClient.getEmail()))
                        ).thenReturn(savedPaymentBook)
                );
    }

    @Override
    public Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook) {
        return paymentBookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setBookingId(paymentBook.getBookingId());
                    existingBook.setUserClientId(paymentBook.getUserClientId());
                    existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
                    existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
                    existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
                    existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
                    existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
                    existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
                    existingBook.setAmount(paymentBook.getAmount());
                    existingBook.setDescription(paymentBook.getDescription());
                    existingBook.setPaymentDate(paymentBook.getPaymentDate());
                    existingBook.setOperationCode(paymentBook.getOperationCode());
                    existingBook.setNote(paymentBook.getNote());
                    existingBook.setTotalCost(paymentBook.getTotalCost());
                    existingBook.setImageVoucher(paymentBook.getImageVoucher());
                    existingBook.setTotalPoints(paymentBook.getTotalPoints());
                    existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
                    return paymentBookRepository.save(existingBook);
                });
    }

    @Override
    public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    @Override
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookRepository.findAll();
    }

    @Override
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId) {
        return paymentBookRepository.findByUserClientId(userClientId);
    }

    @Override
    public Mono<Void> deletePaymentBook(Integer id) {
        return paymentBookRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updateBookingStateIfRequired(Integer bookingId) {
        return bookingService.updateBookingStatePay(bookingId, 3)
                .filter(booking -> booking.getBookingStateId() == 2)
                .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2))
                .then();
    }

    @Override
    public Mono<PaymentBookEntity> findById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    /*
    @Override
    public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails() {
        return paymentBookRepository.findAll()
                .flatMap(paymentBook ->
                        Mono.zip(
                                Mono.just(paymentBook),
                                userClientRepository.findById(paymentBook.getUserClientId()),
                                bookingService.findById(paymentBook.getBookingId()),
                                paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                        ).map(tuple -> {
                            PaymentBookEntity paymentBookEntity = tuple.getT1();
                            UserClientEntity userClient = tuple.getT2();
                            BookingEntity booking = tuple.getT3();
                            PaymentMethodEntity paymentMethod = tuple.getT4();
                            PaymentStateEntity paymentState = tuple.getT5();
                            PaymentTypeEntity paymentType = tuple.getT6();
                            PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                            CurrencyTypeEntity currencyType = tuple.getT8();

                            PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                    .paymentBookId(paymentBookEntity.getPaymentBookId())
                                    .bookingId(paymentBookEntity.getBookingId())
                                    .userClientId(paymentBookEntity.getUserClientId())
                                    .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                    .paymentStateId(paymentBookEntity.getPaymentStateId())
                                    .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                    .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                    .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                    .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                    .amount(paymentBookEntity.getAmount())
                                    .description(paymentBookEntity.getDescription())
                                    .paymentDate(paymentBookEntity.getPaymentDate())
                                    .operationCode(paymentBookEntity.getOperationCode())
                                    .note(paymentBookEntity.getNote())
                                    .totalCost(paymentBookEntity.getTotalCost())
                                    .imageVoucher(paymentBookEntity.getImageVoucher())
                                    .totalPoints(paymentBookEntity.getTotalPoints())
                                    .paymentComplete(paymentBookEntity.getPaymentComplete())
                                    .pendingPay(paymentBookEntity.getPendingpay());

                            if (userClient != null) {
                                builder.userClientName(userClient.getFirstName());
                            }
                            if (booking != null) {
                                builder.bookingName(booking.getDetail());
                            }
                            if (paymentMethod != null) {
                                builder.paymentMethod(paymentMethod.getDescription());
                            }
                            if (paymentState != null) {
                                builder.paymentState(paymentState.getPaymentStateName());
                            }
                            if (paymentType != null) {
                                builder.paymentType(paymentType.getPaymentTypeDesc());
                            }
                            if (paymentSubtype != null) {
                                builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                            }
                            if (currencyType != null) {
                                builder.currencyType(currencyType.getCurrencyTypeDescription());
                            }

                            return builder.build();
                        })
                );
    }
     */

    /*
    @Override
    public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 0, size, offset)
                .flatMap(paymentBook ->
                        Mono.zip(
                                Mono.just(paymentBook),
                                userClientRepository.findById(paymentBook.getUserClientId()),
                                bookingService.findById(paymentBook.getBookingId()),
                                paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                        ).map(tuple -> {
                            PaymentBookEntity paymentBookEntity = tuple.getT1();
                            UserClientEntity userClient = tuple.getT2();
                            BookingEntity booking = tuple.getT3();
                            PaymentMethodEntity paymentMethod = tuple.getT4();
                            PaymentStateEntity paymentState = tuple.getT5();
                            PaymentTypeEntity paymentType = tuple.getT6();
                            PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                            CurrencyTypeEntity currencyType = tuple.getT8();

                            PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                    .paymentBookId(paymentBookEntity.getPaymentBookId())
                                    .bookingId(paymentBookEntity.getBookingId())
                                    .userClientId(paymentBookEntity.getUserClientId())
                                    .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                    .paymentStateId(paymentBookEntity.getPaymentStateId())
                                    .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                    .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                    .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                    .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                    .amount(paymentBookEntity.getAmount())
                                    .description(paymentBookEntity.getDescription())
                                    .paymentDate(paymentBookEntity.getPaymentDate())
                                    .operationCode(paymentBookEntity.getOperationCode())
                                    .note(paymentBookEntity.getNote())
                                    .totalCost(paymentBookEntity.getTotalCost())
                                    .imageVoucher(paymentBookEntity.getImageVoucher())
                                    .totalPoints(paymentBookEntity.getTotalPoints())
                                    .paymentComplete(paymentBookEntity.getPaymentComplete())
                                    .pendingPay(paymentBookEntity.getPendingpay());

                            if (userClient != null) {
                                builder.userClientName(userClient.getFirstName());
                            }
                            if (booking != null) {
                                builder.bookingName(booking.getDetail());
                            }
                            if (paymentMethod != null) {
                                builder.paymentMethod(paymentMethod.getDescription());
                            }
                            if (paymentState != null) {
                                builder.paymentState(paymentState.getPaymentStateName());
                            }
                            if (paymentType != null) {
                                builder.paymentType(paymentType.getPaymentTypeDesc());
                            }
                            if (paymentSubtype != null) {
                                builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                            }
                            if (currencyType != null) {
                                builder.currencyType(currencyType.getCurrencyTypeDescription());
                            }

                            return builder.build();
                        })
                );
    }
     */

    @Override
    public Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetails(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 0, size, offset)
                .flatMap(paymentBook ->
                        Mono.zip(
                                Mono.just(paymentBook),
                                userClientRepository.findById(paymentBook.getUserClientId()),
                                bookingService.findById(paymentBook.getBookingId()),
                                paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                        ).map(tuple -> {
                            PaymentBookEntity paymentBookEntity = tuple.getT1();
                            UserClientEntity userClient = tuple.getT2();
                            BookingEntity booking = tuple.getT3();
                            PaymentMethodEntity paymentMethod = tuple.getT4();
                            PaymentStateEntity paymentState = tuple.getT5();
                            PaymentTypeEntity paymentType = tuple.getT6();
                            PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                            CurrencyTypeEntity currencyType = tuple.getT8();

                            PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                    .paymentBookId(paymentBookEntity.getPaymentBookId())
                                    .bookingId(paymentBookEntity.getBookingId())
                                    .userClientId(paymentBookEntity.getUserClientId())
                                    .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                    .paymentStateId(paymentBookEntity.getPaymentStateId())
                                    .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                    .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                    .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                    .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                    .amount(paymentBookEntity.getAmount())
                                    .description(paymentBookEntity.getDescription())
                                    .paymentDate(paymentBookEntity.getPaymentDate())
                                    .operationCode(paymentBookEntity.getOperationCode())
                                    .note(paymentBookEntity.getNote())
                                    .totalCost(paymentBookEntity.getTotalCost())
                                    .imageVoucher(paymentBookEntity.getImageVoucher())
                                    .totalPoints(paymentBookEntity.getTotalPoints())
                                    .paymentComplete(paymentBookEntity.getPaymentComplete())
                                    .pendingPay(paymentBookEntity.getPendingpay());

                            if (userClient != null) {
                                builder.userClientName(userClient.getFirstName());
                                builder.userClientLastName(userClient.getLastName());
                            }
                            if (booking != null) {
                                builder.bookingName(booking.getDetail());
                            }
                            if (paymentMethod != null) {
                                builder.paymentMethod(paymentMethod.getDescription());
                            }
                            if (paymentState != null) {
                                builder.paymentState(paymentState.getPaymentStateName());
                            }
                            if (paymentType != null) {
                                builder.paymentType(paymentType.getPaymentTypeDesc());
                            }
                            if (paymentSubtype != null) {
                                builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                            }
                            if (currencyType != null) {
                                builder.currencyType(currencyType.getCurrencyTypeDescription());
                            }

                            return builder.build();
                        })
                )
                .collectList()
                .flatMap(paymentBookDetails -> {
                    long totalElements = paymentBookDetails.size();
                    return Mono.just(new PaginatedResponse<>(totalElements, paymentBookDetails));
                });
    }

    @Override
    public Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetailsPagado(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.countByRefuseReasonIdAndPendingPay(1, 1)
                .flatMap(totalElements -> paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 1, size, offset)
                        .flatMap(paymentBook ->
                                Mono.zip(
                                        Mono.just(paymentBook),
                                        userClientRepository.findById(paymentBook.getUserClientId()),
                                        bookingService.findById(paymentBook.getBookingId()),
                                        paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                        paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                        paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                        paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                        currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                                ).map(tuple -> {
                                    PaymentBookEntity paymentBookEntity = tuple.getT1();
                                    UserClientEntity userClient = tuple.getT2();
                                    BookingEntity booking = tuple.getT3();
                                    PaymentMethodEntity paymentMethod = tuple.getT4();
                                    PaymentStateEntity paymentState = tuple.getT5();
                                    PaymentTypeEntity paymentType = tuple.getT6();
                                    PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                                    CurrencyTypeEntity currencyType = tuple.getT8();

                                    PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                            .paymentBookId(paymentBookEntity.getPaymentBookId())
                                            .bookingId(paymentBookEntity.getBookingId())
                                            .userClientId(paymentBookEntity.getUserClientId())
                                            .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                            .paymentStateId(paymentBookEntity.getPaymentStateId())
                                            .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                            .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                            .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                            .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                            .amount(paymentBookEntity.getAmount())
                                            .description(paymentBookEntity.getDescription())
                                            .paymentDate(paymentBookEntity.getPaymentDate())
                                            .operationCode(paymentBookEntity.getOperationCode())
                                            .note(paymentBookEntity.getNote())
                                            .totalCost(paymentBookEntity.getTotalCost())
                                            .imageVoucher(paymentBookEntity.getImageVoucher())
                                            .totalPoints(paymentBookEntity.getTotalPoints())
                                            .paymentComplete(paymentBookEntity.getPaymentComplete())
                                            .pendingPay(paymentBookEntity.getPendingpay());

                                    if (userClient != null) {
                                        builder.userClientName(userClient.getFirstName());
                                        builder.userClientLastName(userClient.getLastName());
                                    }
                                    if (booking != null) {
                                        builder.bookingName(booking.getDetail());
                                    }
                                    if (paymentMethod != null) {
                                        builder.paymentMethod(paymentMethod.getDescription());
                                    }
                                    if (paymentState != null) {
                                        builder.paymentState(paymentState.getPaymentStateName());
                                    }
                                    if (paymentType != null) {
                                        builder.paymentType(paymentType.getPaymentTypeDesc());
                                    }
                                    if (paymentSubtype != null) {
                                        builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                                    }
                                    if (currencyType != null) {
                                        builder.currencyType(currencyType.getCurrencyTypeDescription());
                                    }

                                    return builder.build();
                                })
                        )
                        .collectList()
                        .map(paymentBookDetails -> new PaginatedResponse<>(totalElements, paymentBookDetails)));
    }

    private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity paymentBook, String email) {
        return bookingService.findById(paymentBook.getBookingId())
                .flatMap(booking -> roomOfferRepository.findById(booking.getRoomOfferId()))
                .flatMap(roomOffer -> roomRepository.findById(roomOffer.getRoomId()))
                .flatMap(room -> {
                    String roomName = room.getRoomName(); // Extract roomName
                    String emailBody = generatePaymentConfirmationEmailBody(paymentBook, roomName);
                    return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
                });
    }

    /*
    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<h1 style='text-align: center;'>Confirmación de Pago</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido recibido con éxito.</p>";
        body += "<p>Detalles del pago:</p>";
        body += "<ul>";
        body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
        body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
        body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
        body += "</ul>";
        body += "<p>Gracias por su confianza.</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }

     */

    @Override
    public Mono<PaymentBookEntity> savePaymentBook(PaymentBookEntity paymentBook) {
        return paymentBookRepository.save(paymentBook);
    }

    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook, String roomName) {
        String body = "<html>\n" +
                "<head>\n" +
                "    <title>Bienvenido</title>\n" +
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
                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n" +
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
                "        <img class=\"logo-left\" src=\"https://i.postimg.cc/7PsVyMZz/Email-Template-Header.png\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://i.postimg.cc/pX3JmW8X/Image.png\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h2 style='text-align: center;'>Confirmación de Pago</h2>\n" +
                "            <p>Estimado cliente,</p>\n" +
                "            <p>Su pago ha sido recibido con éxito.</p>\n" +
                "            <p>Detalles del pago:</p>\n" +
                "            <ul>\n" +
                "                <li>Reserva: " + roomName + "</li>\n" +
                "                <li>Monto: " + paymentBook.getTotalCost() + "</li>\n" +
                "                <li>Fecha de pago: " + paymentBook.getPaymentDate() + "</li>\n" +
                "            </ul>\n" +
                "            <p>Gracias por su confianza.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Sección de ayuda -->\n" +
                "    <div class=\"help-section\">\n" +
                "        <h3>Necesitas ayuda?</h3>\n" +
                "        <p>Comunicate con nosotros a traves de los siguientes medios:</p>\n" +
                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return body;
    }
}
