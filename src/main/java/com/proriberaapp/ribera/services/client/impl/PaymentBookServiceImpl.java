package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Domain.dto.PaymentBookWithChannelDto;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.*;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.PaymentByBankTransferTemplateEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PaymentBookServiceImpl implements PaymentBookService {
    /*
     * ANTES DEL CORREO
     * private final PaymentBookRepository paymentBookRepository;
     * private final BookingService bookingService;
     * 
     * private final S3Uploader s3Uploader;
     * 
     * @Autowired
     * public PaymentBookServiceImpl(PaymentBookRepository
     * paymentBookRepository,BookingService bookingService, S3Uploader s3Uploader) {
     * this.paymentBookRepository = paymentBookRepository;
     * this.bookingService = bookingService;
     * this.s3Uploader = s3Uploader;
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity
     * paymentBook) {
     * LocalDateTime now = LocalDateTime.now();
     * Timestamp timestamp = Timestamp.valueOf(now);
     * paymentBook.setPaymentDate(timestamp);
     * 
     * return paymentBookRepository.save(paymentBook);
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity
     * paymentBook) {
     * LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
     * Timestamp timestamp = Timestamp.valueOf(localDateTime);
     * paymentBook.setPaymentDate(timestamp);
     * return paymentBookRepository.save(paymentBook)
     * .flatMap(savedPaymentBook ->
     * updateBookingStateIfRequired(savedPaymentBook.getBookingId()).thenReturn(
     * savedPaymentBook));
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> updatePaymentBook(Integer id,
     * PaymentBookEntity paymentBook) {
     * return paymentBookRepository.findById(id)
     * .flatMap(existingBook -> {
     * existingBook.setBookingId(paymentBook.getBookingId());
     * existingBook.setUserClientId(paymentBook.getUserClientId());
     * existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
     * existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
     * existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
     * existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
     * existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
     * existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
     * existingBook.setAmount(paymentBook.getAmount());
     * existingBook.setDescription(paymentBook.getDescription());
     * existingBook.setPaymentDate(paymentBook.getPaymentDate());
     * existingBook.setOperationCode(paymentBook.getOperationCode());
     * existingBook.setNote(paymentBook.getNote());
     * existingBook.setTotalCost(paymentBook.getTotalCost());
     * existingBook.setImageVoucher(paymentBook.getImageVoucher());
     * existingBook.setTotalPoints(paymentBook.getTotalPoints());
     * existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
     * return paymentBookRepository.save(existingBook);
     * });
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
     * return paymentBookRepository.findById(id);
     * }
     * 
     * @Override
     * public Flux<PaymentBookEntity> getAllPaymentBooks() {
     * return paymentBookRepository.findAll();
     * }
     * 
     * @Override
     * public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer
     * userClientId) {
     * return paymentBookRepository.findByUserClientId(userClientId);
     * }
     * 
     * @Override
     * public Mono<Void> deletePaymentBook(Integer id) {
     * return paymentBookRepository.deleteById(id);
     * }
     * 
     * @Override
     * public Mono<Void> updateBookingStateIfRequired(Integer bookingId) {
     * return bookingService.updateBookingStatePay(bookingId, 3) // Aquí verificas y
     * actualizas el estado a 3
     * .filter(booking -> booking.getBookingStateId() == 2) // Verifica si el estado
     * es 2
     * .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2)) // Si
     * es 2, lo actualizas
     * .then();
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> findById(Integer id) {
     * return paymentBookRepository.findById(id);
     * }
     */

    /*
     * DESPUES DE CORREO
     * private final PaymentBookRepository paymentBookRepository;
     * private final UserClientRepository userClientRepository;
     * private final BookingService bookingService;
     * private final S3Uploader s3Uploader;
     * private final EmailService emailService;
     * 
     * @Autowired
     * public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
     * UserClientRepository userClientRepository,
     * BookingService bookingService,
     * S3Uploader s3Uploader,
     * EmailService emailService) {
     * this.paymentBookRepository = paymentBookRepository;
     * this.userClientRepository = userClientRepository;
     * this.bookingService = bookingService;
     * this.s3Uploader = s3Uploader;
     * this.emailService = emailService;
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity
     * paymentBook) {
     * LocalDateTime now = LocalDateTime.now();
     * Timestamp timestamp = Timestamp.valueOf(now);
     * paymentBook.setPaymentDate(timestamp);
     * 
     * return paymentBookRepository.save(paymentBook);
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity
     * paymentBook) {
     * LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
     * Timestamp timestamp = Timestamp.valueOf(localDateTime);
     * paymentBook.setPaymentDate(timestamp);
     * 
     * return paymentBookRepository.save(paymentBook)
     * .flatMap(savedPaymentBook -> {
     * return updateBookingStateIfRequired(savedPaymentBook.getBookingId())
     * .then(userClientRepository.findById(savedPaymentBook.getUserClientId())
     * .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook,
     * userClient.getEmail()))
     * )
     * .thenReturn(savedPaymentBook);
     * });
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> updatePaymentBook(Integer id,
     * PaymentBookEntity paymentBook) {
     * return paymentBookRepository.findById(id)
     * .flatMap(existingBook -> {
     * existingBook.setBookingId(paymentBook.getBookingId());
     * existingBook.setUserClientId(paymentBook.getUserClientId());
     * existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
     * existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
     * existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
     * existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
     * existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
     * existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
     * existingBook.setAmount(paymentBook.getAmount());
     * existingBook.setDescription(paymentBook.getDescription());
     * existingBook.setPaymentDate(paymentBook.getPaymentDate());
     * existingBook.setOperationCode(paymentBook.getOperationCode());
     * existingBook.setNote(paymentBook.getNote());
     * existingBook.setTotalCost(paymentBook.getTotalCost());
     * existingBook.setImageVoucher(paymentBook.getImageVoucher());
     * existingBook.setTotalPoints(paymentBook.getTotalPoints());
     * existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
     * return paymentBookRepository.save(existingBook);
     * });
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
     * return paymentBookRepository.findById(id);
     * }
     * 
     * @Override
     * public Flux<PaymentBookEntity> getAllPaymentBooks() {
     * return paymentBookRepository.findAll();
     * }
     * 
     * @Override
     * public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer
     * userClientId) {
     * return paymentBookRepository.findByUserClientId(userClientId);
     * }
     * 
     * @Override
     * public Mono<Void> deletePaymentBook(Integer id) {
     * return paymentBookRepository.deleteById(id);
     * }
     * 
     * @Override
     * public Mono<Void> updateBookingStateIfRequired(Integer bookingId) {
     * return bookingService.updateBookingStatePay(bookingId, 3) // Aquí verificas y
     * actualizas el estado a 3
     * .filter(booking -> booking.getBookingStateId() == 2) // Verifica si el estado
     * es 2
     * .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2)) // Si
     * es 2, lo actualizas
     * .then();
     * }
     * 
     * @Override
     * public Mono<PaymentBookEntity> findById(Integer id) {
     * return paymentBookRepository.findById(id);
     * }
     * 
     * private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity
     * paymentBook, String email) {
     * String emailBody = generatePaymentConfirmationEmailBody(paymentBook);
     * return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
     * }
     * 
     * private String generatePaymentConfirmationEmailBody(PaymentBookEntity
     * paymentBook) {
     * String body = "<html><head><title></title></head><body style='color:black'>";
     * body += "<div style='width: 100%'>";
     * body += "<h1 style='text-align: center;'>Confirmación de Pago</h1>";
     * body += "<p>Estimado cliente,</p>";
     * body += "<p>Su pago ha sido recibido con éxito.</p>";
     * body += "<p>Detalles del pago:</p>";
     * body += "<ul>";
     * body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
     * body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
     * body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
     * body += "</ul>";
     * body += "<p>Gracias por su confianza.</p>";
     * body += "</div>";
     * body += "</body></html>";
     * return body;
     * }
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
    private final CommissionService commissionService;

    private final InvoiceServiceI invoiceService;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
            UserClientRepository userClientRepository,
            BookingService bookingService,
            RoomOfferRepository roomOfferRepository, RoomRepository roomRepository, S3Uploader s3Uploader,
            EmailService emailService,
            PaymentMethodRepository paymentMethodRepository,
            PaymentStateRepository paymentStateRepository, PaymentTypeRepository paymentTypeRepository,
            PaymentSubtypeRepository paymentSubtypeRepository, CurrencyTypeRepository currencyTypeRepository,
            CommissionService commissionService,
            InvoiceServiceI invoiceService) {
        this.invoiceService = invoiceService;
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

        this.commissionService = commissionService;
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
                                .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook,
                                        userClient.getEmail(), userClient.getFirstName())))
                        .thenReturn(savedPaymentBook));
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
     * @Override
     * public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails() {
     * return paymentBookRepository.findAll()
     * .flatMap(paymentBook ->
     * Mono.zip(
     * Mono.just(paymentBook),
     * userClientRepository.findById(paymentBook.getUserClientId()),
     * bookingService.findById(paymentBook.getBookingId()),
     * paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
     * paymentStateRepository.findById(paymentBook.getPaymentStateId()),
     * paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
     * paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
     * currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
     * ).map(tuple -> {
     * PaymentBookEntity paymentBookEntity = tuple.getT1();
     * UserClientEntity userClient = tuple.getT2();
     * BookingEntity booking = tuple.getT3();
     * PaymentMethodEntity paymentMethod = tuple.getT4();
     * PaymentStateEntity paymentState = tuple.getT5();
     * PaymentTypeEntity paymentType = tuple.getT6();
     * PaymentSubtypeEntity paymentSubtype = tuple.getT7();
     * CurrencyTypeEntity currencyType = tuple.getT8();
     * 
     * PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder =
     * PaymentBookDetailsDTO.builder()
     * .paymentBookId(paymentBookEntity.getPaymentBookId())
     * .bookingId(paymentBookEntity.getBookingId())
     * .userClientId(paymentBookEntity.getUserClientId())
     * .paymentMethodId(paymentBookEntity.getPaymentMethodId())
     * .paymentStateId(paymentBookEntity.getPaymentStateId())
     * .refuseReasonId(paymentBookEntity.getRefuseReasonId())
     * .paymentTypeId(paymentBookEntity.getPaymentTypeId())
     * .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
     * .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
     * .amount(paymentBookEntity.getAmount())
     * .description(paymentBookEntity.getDescription())
     * .paymentDate(paymentBookEntity.getPaymentDate())
     * .operationCode(paymentBookEntity.getOperationCode())
     * .note(paymentBookEntity.getNote())
     * .totalCost(paymentBookEntity.getTotalCost())
     * .imageVoucher(paymentBookEntity.getImageVoucher())
     * .totalPoints(paymentBookEntity.getTotalPoints())
     * .paymentComplete(paymentBookEntity.getPaymentComplete())
     * .pendingPay(paymentBookEntity.getPendingpay());
     * 
     * if (userClient != null) {
     * builder.userClientName(userClient.getFirstName());
     * }
     * if (booking != null) {
     * builder.bookingName(booking.getDetail());
     * }
     * if (paymentMethod != null) {
     * builder.paymentMethod(paymentMethod.getDescription());
     * }
     * if (paymentState != null) {
     * builder.paymentState(paymentState.getPaymentStateName());
     * }
     * if (paymentType != null) {
     * builder.paymentType(paymentType.getPaymentTypeDesc());
     * }
     * if (paymentSubtype != null) {
     * builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
     * }
     * if (currencyType != null) {
     * builder.currencyType(currencyType.getCurrencyTypeDescription());
     * }
     * 
     * return builder.build();
     * })
     * );
     * }
     */

    /*
     * @Override
     * public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails(int page, int
     * size) {
     * int offset = page * size;
     * return paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 0, size,
     * offset)
     * .flatMap(paymentBook ->
     * Mono.zip(
     * Mono.just(paymentBook),
     * userClientRepository.findById(paymentBook.getUserClientId()),
     * bookingService.findById(paymentBook.getBookingId()),
     * paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
     * paymentStateRepository.findById(paymentBook.getPaymentStateId()),
     * paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
     * paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
     * currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
     * ).map(tuple -> {
     * PaymentBookEntity paymentBookEntity = tuple.getT1();
     * UserClientEntity userClient = tuple.getT2();
     * BookingEntity booking = tuple.getT3();
     * PaymentMethodEntity paymentMethod = tuple.getT4();
     * PaymentStateEntity paymentState = tuple.getT5();
     * PaymentTypeEntity paymentType = tuple.getT6();
     * PaymentSubtypeEntity paymentSubtype = tuple.getT7();
     * CurrencyTypeEntity currencyType = tuple.getT8();
     * 
     * PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder =
     * PaymentBookDetailsDTO.builder()
     * .paymentBookId(paymentBookEntity.getPaymentBookId())
     * .bookingId(paymentBookEntity.getBookingId())
     * .userClientId(paymentBookEntity.getUserClientId())
     * .paymentMethodId(paymentBookEntity.getPaymentMethodId())
     * .paymentStateId(paymentBookEntity.getPaymentStateId())
     * .refuseReasonId(paymentBookEntity.getRefuseReasonId())
     * .paymentTypeId(paymentBookEntity.getPaymentTypeId())
     * .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
     * .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
     * .amount(paymentBookEntity.getAmount())
     * .description(paymentBookEntity.getDescription())
     * .paymentDate(paymentBookEntity.getPaymentDate())
     * .operationCode(paymentBookEntity.getOperationCode())
     * .note(paymentBookEntity.getNote())
     * .totalCost(paymentBookEntity.getTotalCost())
     * .imageVoucher(paymentBookEntity.getImageVoucher())
     * .totalPoints(paymentBookEntity.getTotalPoints())
     * .paymentComplete(paymentBookEntity.getPaymentComplete())
     * .pendingPay(paymentBookEntity.getPendingpay());
     * 
     * if (userClient != null) {
     * builder.userClientName(userClient.getFirstName());
     * }
     * if (booking != null) {
     * builder.bookingName(booking.getDetail());
     * }
     * if (paymentMethod != null) {
     * builder.paymentMethod(paymentMethod.getDescription());
     * }
     * if (paymentState != null) {
     * builder.paymentState(paymentState.getPaymentStateName());
     * }
     * if (paymentType != null) {
     * builder.paymentType(paymentType.getPaymentTypeDesc());
     * }
     * if (paymentSubtype != null) {
     * builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
     * }
     * if (currencyType != null) {
     * builder.currencyType(currencyType.getCurrencyTypeDescription());
     * }
     * 
     * return builder.build();
     * })
     * );
     * }
     */

    @Override
    public Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetails(int page, int size) {
        int offset = page * size;

        return paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 0, size,
                offset)
                .flatMap(paymentBook -> Mono.zip(
                        Mono.just(paymentBook),
                        userClientRepository.findById(paymentBook.getUserClientId()),
                        bookingService.searchById(paymentBook.getBookingId()),
                        paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                        paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                        paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                        paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                        currencyTypeRepository.findById(paymentBook.getCurrencyTypeId()))
                        .map(tuple -> {
                            PaymentBookWithChannelDto paymentBookEntity = tuple.getT1();
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
                                    .pendingPay(paymentBookEntity.getPendingpay())
                                    .totalDiscount(paymentBookEntity.getTotalDiscount())
                                    .percentageDiscount(paymentBookEntity.getPercentageDiscount())
                                    .channel(paymentBookEntity.getChannel())
                                    .nights(paymentBookEntity.getNights())
                                    .dayBookingEnd(paymentBookEntity.getDayBookingEnd())
                                    .dayBookingInit(paymentBookEntity.getDayBookingInit())
                                    .totalCostWithOutDiscount(paymentBookEntity.getTotalCostWithOutDiscount());

                            if (userClient != null) {
                                builder.userClientName(userClient.getFirstName());
                                builder.userClientLastName(userClient.getLastName());
                                builder.userClientEmail(userClient.getEmail());
                                builder.userCellphoneNumber(userClient.getCellNumber());
                                builder.userDocumentType(userClient.getDocumenttypeId());
                                builder.userDocumentNumber(userClient.getDocumentNumber());
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
                        }))
                .collectList()
                .flatMap(paymentBookDetails -> {
                    long totalElements = paymentBookDetails.size();
                    return Mono.just(new PaginatedResponse<>(totalElements, paymentBookDetails));
                });

    }

/*     @Override
    public Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetailsPagado(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.countByRefuseReasonIdAndPendingPay(1, 1)
                .flatMap(elements -> paymentBookRepository.findAllPaged(1, size, offset)
                        .collectList()
                        .flatMap(paymentBookDetails -> {
                            long totalElements = elements;
                            return Mono.just(new PaginatedResponse<>(totalElements, paymentBookDetails));
                        }));
    } */
    @Override
    public Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetailsPagado(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.countByRefuseReasonIdAndPendingPay(1, 1)
                .flatMap(elements -> paymentBookRepository.findAllPaged(1, size, offset)
                        .collectList()
                        .flatMap(paymentBookDetails -> {
                            long totalElements = elements;
                            return Mono.just(new PaginatedResponse<>(totalElements, paymentBookDetails));
                        }));
    }

    private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity paymentBook, String email, String userName) {
        return bookingService.findById(paymentBook.getBookingId())
                .flatMap(booking -> roomOfferRepository.findById(booking.getRoomOfferId()))
                .flatMap(roomOffer -> roomRepository.findById(roomOffer.getRoomId()))
                .flatMap(room -> {
                    String roomName = room.getRoomName(); // Extract roomName
                    BaseEmailReserve baseEmailReserve = new BaseEmailReserve();
                    baseEmailReserve.addEmailHandler(
                            new PaymentByBankTransferTemplateEmail(userName, paymentBook.getTotalCost()));
                    /*
                     * String emailBody = generatePaymentConfirmationEmailBody(paymentBook,
                     * roomName);
                     */
                    String emailBody = baseEmailReserve.execute();
                    return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
                });
    }

    /*
     * private String generatePaymentConfirmationEmailBody(PaymentBookEntity
     * paymentBook) {
     * String body = "<html><head><title></title></head><body style='color:black'>";
     * body += "<div style='width: 100%'>";
     * body += "<h1 style='text-align: center;'>Confirmación de Pago</h1>";
     * body += "<p>Estimado cliente,</p>";
     * body += "<p>Su pago ha sido recibido con éxito.</p>";
     * body += "<p>Detalles del pago:</p>";
     * body += "<ul>";
     * body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
     * body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
     * body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
     * body += "</ul>";
     * body += "<p>Gracias por su confianza.</p>";
     * body += "</div>";
     * body += "</body></html>";
     * return body;
     * }
     * 
     */

    @Override
    public Mono<PaymentBookEntity> savePaymentBook(PaymentBookEntity paymentBook) {
        return paymentBookRepository.save(paymentBook);
    }

    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook, String roomName) {
        String body = "<!DOCTYPE html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Bienvenido</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; color: black; background-color: #F6F7FB; }"
                +
                "        .header { width: 100%; position: relative; background-color: white; padding: 20px 0; }" +
                "        .logo-left { width: 50px; position: absolute; top: 10px; left: 10px; }" +
                "        .banner { width: 100%; display: block; margin: 0 auto; }" +
                "        .container { width: 100%; background-color: #FFFFFF; margin: 0px auto 0; padding: 0px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; }"
                +
                "        .content { text-align: left; padding: 20px; }" +
                "        .content h3 { margin: 10px 0; }" +
                "        .content p { margin: 10px 0; }" +
                "        .table-layout { width: 30%; margin-right: auto; border-collapse: collapse; table-layout: auto; }"
                +
                "        .table-layout td { vertical-align: top; padding-top: 0px; text-align: left; }" +
                "        .button { min-width: 90%; display: inline-block; padding: 10px; background-color: #025928; color: white; text-align: center; text-decoration: none; border-radius: 5px; }"
                +
                "        .footer { width: 100%; text-align: left; padding-top: 0px; padding-left: 40px; color: #9D9D9D !important }"
                +
                "        .help-section { width: 100%; background-color: #FFFFFF; margin: 20px auto; padding: 5px 40px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: left; color: #384860; }"
                +
                "        .content-data { padding: 20px; background-color: #FFFFFF; color: #384860 !important }" +
                "        .content-data p {color: #384860 !important }" +
                "        .content-data small { font-weight: bold; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <img class=\"logo-left\" src=\"https://i.postimg.cc/7PsVyMZz/Email-Template-Header.png\" alt=\"Logo Izquierda\">"
                +
                "    </div>" +
                "    <img class=\"banner\" src=\"https://i.postimg.cc/pX3JmW8X/Image.png\" alt=\"Bienvenido\">" +
                "    <div class=\"container\">" +
                "        <div class=\"content\">" +
                "            <div class=\"content-data\">" +
                "                <p>Hola; <br> Muchas gracias por tu reserva. Ya estas más cerca en disfrutar con nosotros <br> El total de tu estancia es de: S/ "
                + paymentBook.getTotalCost() +
                "                <br> Para confirmar la reserva realizaremos la verificacion del pago." +
                "                <small><br> En menos de 48 hr recibirás un correo electronico con la constancia de confirmacion donde estará el detalle de tu reserva. </small></p>"
                +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "    <div class=\"help-section\">" +
                "        <h3>¿Necesitas ayuda?</h3>" +
                "        <p>Envía tus comentarios e informacion de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>"
                +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Si prefiere no recibir este tipo de correo electronico, ¿no quiere más correos electronicos de Ribera? <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">Darse de baja</a>. <br> Valle Encantado S.A.C, Perú. <br> © 2023 Inclub</p>"
                +
                "    </div>" +
                "</body>" +
                "</html>";
        return body;
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBookAndCalculateCommission(PaymentBookEntity paymentBook,
            Integer caseType) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook)
                .flatMap(savedPaymentBook -> updateBookingStateIfRequired(savedPaymentBook.getBookingId())
                        .then(userClientRepository.findById(savedPaymentBook.getUserClientId())
                                .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook,
                                        userClient.getEmail(), userClient.getFirstName())))
                        .then(
                                commissionService.calculateAndSaveCommission(savedPaymentBook, caseType))
                        .thenReturn(savedPaymentBook));
    }

    @Override
    public Mono<Void> createInvoice(Integer paymentBookId) {
        return this.paymentBookRepository.loadUserDataAndBookingData(paymentBookId)
                .flatMap(paymentBook -> {
                    InvoiceClientDomain clientDomain = new InvoiceClientDomain(
                            paymentBook.getUsername(),
                            paymentBook.getInvoicedocumentnumber(),
                            paymentBook.getUseraddress(), paymentBook.getUserphone(),
                            paymentBook.getUseremail(),
                            paymentBook.getUserclientid());

                    InvoiceCurrency invoiceCurrency = InvoiceCurrency
                            .getInvoiceCurrencyByCurrency(paymentBook.getCurrencytypename());
                    InvoiceType type = InvoiceType.getInvoiceTypeByName(paymentBook.getInvoicetype().toUpperCase());
                    InvoiceDomain invoiceDomain = new InvoiceDomain(
                            clientDomain,
                            paymentBook.getPaymentbookid(), 18, invoiceCurrency,
                            type, paymentBook.getPercentagediscount());
                    invoiceDomain.setOperationCode(paymentBook.getOperationcode());
                    invoiceDomain.addItemWithIncludedIgv(new InvoiceItemDomain(
                            paymentBook.getRoomname(),
                            paymentBook.getRoomname(), 1,
                            BigDecimal.valueOf(paymentBook.getTotalcostwithoutdiscount())));
                    invoiceDomain.calculatedTotals();
                    return this.invoiceService.save(invoiceDomain);

                }).then();
    }

}
