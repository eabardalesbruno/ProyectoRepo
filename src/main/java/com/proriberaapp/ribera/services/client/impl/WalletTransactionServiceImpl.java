package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.WalletTransactionService;
import com.proriberaapp.ribera.Api.controllers.client.dto.WithdrawRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import org.springframework.http.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.proriberaapp.ribera.services.PDFGeneratorService.generatePdfFromHtml;


@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final EmailService emailService;
    private final UserClientRepository userClientRepository;
    private final UserPromoterRepository userPromoterRepository;
    private final BookingRepository bookingRepository;
    private final CurrencyTypeRepository currencyTypeRepository;
    private final RoomOfferRepository roomOfferRepository;
    private final RoomRepository roomRepository;
    private final PaymentBookRepository paymentBookRepository;
    @Value("${url.api.tipo-cambio}")
    private String urlApiTipoCambio;
    @Value("${url.api.tipo-cambio.token}")
    private String tokenApiTipoCambio;
    private final  RefusePaymentServiceImpl refusePaymentService;
    private final  CommissionRepository commissionRepository;

    @Override
    public Mono<WalletTransactionEntity> makeTransfer(Integer walletIdOrigin, Integer walletIdDestiny, String emailDestiny, String cardNumber, BigDecimal amount, String motiveDescription) {
        return walletRepository.findById(walletIdOrigin)
                .flatMap(walletEntityOrigin -> {
                    if (walletEntityOrigin.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                    Mono<WalletEntity> walletEntityDestinyMono = findDestinationWallet(walletIdDestiny, emailDestiny, cardNumber);

                    return walletEntityDestinyMono.flatMap(walletEntityDestiny -> {
                        walletEntityOrigin.setBalance(walletEntityOrigin.getBalance().subtract(amount));
                        walletEntityDestiny.setBalance(walletEntityDestiny.getBalance().add(amount));

                        return walletRepository.save(walletEntityOrigin)
                                .then(walletRepository.save(walletEntityDestiny))
                                .flatMap(savedDestiny -> generateUniqueOperationCode()
                                        .flatMap(operationCode -> {
                                            WalletTransactionEntity senderTransaction = WalletTransactionEntity.builder()
                                                    .walletId(walletIdOrigin)
                                                    .currencyTypeId(walletEntityOrigin.getCurrencyTypeId())
                                                    .transactionCategoryId(1) // Categoría: Envío
                                                    .amount(amount)
                                                    .description("Transferencia a " + walletEntityDestiny.getCardNumber())
                                                    .motivedescription(motiveDescription != null && !motiveDescription.trim().isEmpty()
                                                            ? motiveDescription
                                                            : null)
                                                    .operationCode(operationCode)
                                                    .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                                    .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                                    .build();

                                            WalletTransactionEntity recipientTransaction = WalletTransactionEntity.builder()
                                                    .walletId(walletIdDestiny)
                                                    .currencyTypeId(walletEntityDestiny.getCurrencyTypeId())
                                                    .transactionCategoryId(6) // Categoría: Recepción
                                                    .amount(amount)
                                                    .description("Transferencia recibida de " + walletEntityOrigin.getCardNumber())
                                                    .operationCode(operationCode)
                                                    .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                                    .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                                    .build();

                                            Mono<Void> sendOriginEmail = getEmailAndLastNameByWalletOwner(walletEntityOrigin)
                                                    .flatMap(details -> {
                                                        String emailOrigin = details.getT1();
                                                        return generateSuccessEmailBodyOrigin(walletEntityOrigin, walletEntityDestiny, amount, operationCode, Timestamp.valueOf(LocalDateTime.now()))
                                                                .flatMap(emailBody -> emailService.sendEmail(emailOrigin, "Transferencia Exitosa", emailBody));
                                                    });

                                            Mono<Void> sendDestinyEmail = getEmailAndLastNameByWalletOwner(walletEntityDestiny)
                                                    .flatMap(details -> {
                                                        String emailDestinyActual = details.getT1();
                                                        String lastNameDestiny = details.getT2();
                                                        return generateSuccessEmailBodyForDestination(lastNameDestiny, amount, Timestamp.valueOf(LocalDateTime.now()))
                                                                .flatMap(emailBody -> emailService.sendEmail(emailDestinyActual, "Has Recibido Fondos", emailBody));
                                                    });

                                            return walletTransactionRepository.save(senderTransaction)
                                                    .then(walletTransactionRepository.save(recipientTransaction))
                                                    .then(sendOriginEmail)
                                                    .then(sendDestinyEmail)
                                                    .thenReturn(senderTransaction);
                                        }));
                    });
                });
    }

    private Mono<WalletEntity> findDestinationWallet(Integer walletIdDestiny, String emailDestiny, String cardNumber) {
        if (walletIdDestiny != null) {
            return walletRepository.findById(walletIdDestiny)
                    .switchIfEmpty(Mono.error(new Exception("No se encontró la wallet con el ID proporcionado.")));
        } else if (emailDestiny != null && !emailDestiny.isEmpty()) {
            return findWalletByEmail(emailDestiny);
        } else if (cardNumber != null && !cardNumber.isEmpty()) {
            return findWalletByCardNumber(cardNumber);
        }
        return Mono.error(new Exception("Debe proporcionar walletIdDestiny, emailDestiny o cardNumber."));
    }

    private Mono<WalletEntity> findWalletByCardNumber(String cardNumber) {
        return walletRepository.findByCardNumber(cardNumber)
                .switchIfEmpty(Mono.error(new Exception("No se encontró ninguna wallet asociada al número de tarjeta.")));
    }

    private Mono<WalletEntity> findWalletByUserOrPromoter(Mono<UserClientEntity> userMono, Mono<UserPromoterEntity> promoterMono) {
        return userMono
                .flatMap(user -> {
                    if (user.getWalletId() != null) {
                        return walletRepository.findById(user.getWalletId());
                    } else {
                        return Mono.error(new Exception("El usuario no tiene una wallet asociada."));
                    }
                })
                .switchIfEmpty(promoterMono.flatMap(promoter -> {
                    if (promoter.getWalletId() != null) {
                        return walletRepository.findById(promoter.getWalletId());
                    } else {
                        return Mono.error(new Exception("El promotor no tiene una wallet asociada."));
                    }
                }));
    }

    @Override
    public Mono<WalletEntity> findWalletByEmail(String email) {
        if (email != null && !email.isEmpty()) {
            return findWalletByUserOrPromoter(
                    userClientRepository.findByEmail(email),
                    userPromoterRepository.findByEmail(email)
            ).switchIfEmpty(Mono.error(new Exception("No se encontró ninguna wallet asociada al email.")));
        }
        throw new IllegalArgumentException("Debe proporcionar email para buscar la wallet de destino.");
    }

    //PAGO CON LA WALLET
    @Override
    public Mono<String> makePayment(Integer walletId, List<Integer> bookingIds) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (bookingIds.isEmpty()) {
                        return Mono.error(new RuntimeException("No se especificaron reservas para procesar."));
                    }
                    return Flux.fromIterable(bookingIds)
                            .flatMap(bookingRepository::findById)
                            .collectList()
                            .flatMap(bookings -> {
                                BigDecimal totalAmount = bookings.stream()
                                        .map(BookingEntity::getCostFinal)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                if (walletEntity.getBalance().compareTo(totalAmount) < 0) {
                                    return Mono.error(new RuntimeException("Saldo insuficiente."));
                                }
                                walletEntity.setBalance(walletEntity.getBalance().subtract(totalAmount));
                                return walletRepository.save(walletEntity)
                                        .flatMap(savedWallet -> processAndRegisterTransactions(bookings, walletEntity));
                            });
                });
    }

    private Mono<String> processAndRegisterTransactions(List<BookingEntity> bookings, WalletEntity walletEntity) {
        return Flux.fromIterable(bookings)
                .flatMap(booking -> generateUniqueOperationCode()
                        .flatMap(operationCode -> {
                            booking.setBookingStateId(2);

                            return bookingRepository.save(booking)
                                    .flatMap(savedBooking -> {
                                        WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                                .walletId(walletEntity.getWalletId())
                                                .currencyTypeId(walletEntity.getCurrencyTypeId())
                                                .transactionCategoryId(2)
                                                .amount(savedBooking.getCostFinal())
                                                .operationCode(operationCode)
                                                .description("Pago de servicio")
                                                .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                                .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                                .build();

                                        return walletTransactionRepository.save(transaction)
                                                .flatMap(savedTransaction -> {
                                                    return registerPaymentBook(savedBooking, walletEntity, savedTransaction)
                                                            .then(sendPdfToUser(walletEntity, savedBooking, savedTransaction));
                                                });
                                    });
                        }))
                .then(Mono.just("Pago procesado con éxito para todas las reservas."));
    }

    private Mono<Void> sendPdfToUser(WalletEntity walletEntity, BookingEntity booking, WalletTransactionEntity transaction) {
        return userClientRepository.findById(booking.getUserClientId())
                .flatMap(user -> roomOfferRepository.findById(booking.getRoomOfferId())
                        .flatMap(roomOffer -> roomRepository.findById(roomOffer.getRoomId())
                                .flatMap(room -> currencyTypeRepository.findById(walletEntity.getCurrencyTypeId())
                                        .flatMap(currency -> {
                                            String clientName = user.getFirstName() + " " + user.getLastName();
                                            String roomName = room.getRoomName();
                                            String imgSrc = room.getImage();
                                            String titular = clientName;
                                            String code = booking.getBookingId().toString();
                                            LocalDate dateCheckIn = booking.getDayBookingInit().toLocalDateTime().toLocalDate();
                                            LocalDate dateCheckOut = booking.getDayBookingEnd().toLocalDateTime().toLocalDate();
                                            String dateCheckInStr = dateCheckIn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                            String dateCheckOutStr = dateCheckOut.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                            long days = ChronoUnit.DAYS.between(dateCheckIn, dateCheckOut);
                                            int cantidadPersonas = booking.getNumberAdults() + booking.getNumberAdultsExtra() +
                                                    booking.getNumberAdultsMayor() + booking.getNumberChildren() + booking.getNumberBabies();
                                            String hourCheckIn = String.valueOf(booking.getCheckout());
                                            String location = "Km 29.5 Carretera Cieneguilla Mz B. Lt. 72 OTR. Predio Rustico Etapa III, Cercado de Lima 15593";
                                            String emailBody = generateSuccessEmailBody(
                                                    clientName, roomName, imgSrc, titular, code,
                                                    dateCheckInStr, dateCheckOutStr, hourCheckIn, String.valueOf(days),
                                                    String.valueOf(cantidadPersonas), location
                                            );
                                            String pdfFilePath = System.getProperty("user.dir") + "/payment_receipt_" +
                                                    transaction.getWalletId() + "_" + System.currentTimeMillis() + ".pdf";

                                            try {
                                                File pdfFile = generatePdfFromHtml(
                                                        transaction.getOperationCode(),
                                                        clientName,
                                                        user.getDocumentNumber(),
                                                        currency.getCurrencyTypeDescription() + " Wallet",
                                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                                                        booking.getBookingId().toString(),
                                                        roomName,
                                                        booking.getCostFinal().setScale(2, RoundingMode.HALF_UP).toString(),
                                                        pdfFilePath
                                                );

                                                return sendSuccessEmail(user.getEmail(), pdfFilePath, emailBody);
                                            } catch (IOException e) {
                                                return Mono.error(new RuntimeException("Error al generar el PDF", e));
                                            }
                                        }))));
    }

    private Mono<Void> registerPaymentBook(BookingEntity booking, WalletEntity walletEntity, WalletTransactionEntity transaction) {

        Integer userClientId = booking.getUserClientId() != null
                ? booking.getUserClientId()
                : walletEntity.getUserPromoterId();

        PaymentBookEntity paymentBook = PaymentBookEntity.builder()
                .bookingId(booking.getBookingId())
                .userClientId(userClientId)
                .paymentMethodId(5)
                .paymentStateId(2)
                .paymentTypeId(4)
                .currencyTypeId(walletEntity.getCurrencyTypeId())
                .amount(booking.getCostFinal())
                .description("Pago asociado a la reserva")
                .paymentDate(Timestamp.valueOf(LocalDateTime.now()))
                .operationCode(transaction.getOperationCode())
                .totalCost(booking.getCostFinal())
                .paymentComplete(true)
                .refuseReasonId(1)
                .totalPoints(0)
                .invoiceDocumentNumber("N/A")
                .build();

        return paymentBookRepository.save(paymentBook).then();
    }

    private Mono<Tuple3<String, String,String>> getEmailAndLastNameByWalletOwner(WalletEntity walletEntity) {
        if (walletEntity.getUserClientId() != null) {
            return userClientRepository.findById(walletEntity.getUserClientId())
                    .flatMap(user -> Mono.zip(
                            Mono.just(user.getEmail()),
                            Mono.just(user.getFirstName() + " " + user.getLastName()),
                            Mono.just(user.getDocumentNumber())
                    ))
                    .switchIfEmpty(Mono.error(new Exception("Cliente no encontrado.")));
        } else if (walletEntity.getUserPromoterId() != null) {
            return userPromoterRepository.findById(walletEntity.getUserPromoterId())
                    .flatMap(promoter -> Mono.zip(
                            Mono.just(promoter.getEmail()),
                            Mono.just(promoter.getFirstName() + " " + promoter.getLastName()),
                            Mono.just(promoter.getDocumentNumber())
                    ))
                    .switchIfEmpty(Mono.error(new Exception("Promotor no encontrado.")));
        } else {
            return Mono.error(new Exception("La wallet no tiene asociado un cliente o promotor válido."));
        }
    }

    private Mono<Void> sendSuccessEmail(String email, String pdfFilePath, String emailBody) {
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            return Mono.error(new RuntimeException("El archivo PDF no existe: " + pdfFilePath));
        }

        return emailService.sendEmailWithAttachment(email, emailBody, "Confirmación de Pago", pdfFilePath)
                .doOnSuccess(v -> {
                    System.out.println("Correo enviado correctamente a: " + email);
                    if (pdfFile.delete()) {
                        System.out.println("Archivo PDF eliminado correctamente: " + pdfFilePath);
                    }
                })
                .doOnError(e -> System.err.println("Error al enviar el correo: " + e.getMessage()));
    }

    private String generateSuccessEmailBody(String clientName, String roomName, String imgSrc, String titular,
                                            String code, String dateCheckIn, String dateCheckOut,
                                            String hourCheckIn, String days, String cantidadPersonas, String location) {

        return "<!DOCTYPE html>"
                + "<html lang=\"es\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>Document</title>"
                + "    <style>"
                + "        body {width: 100%;background: rgb(246, 247, 251);padding-bottom: 40px;padding-top: 40px;font-family: Arial, sans-serif;margin: 0;}"
                + "        .button {width: 90%;display: inline-block;padding: 10px;background-color: #025928;color: white !important; text-align: center;text-decoration: none; border-radius: 0px;}"
                + "        .card {background-color: rgb(246, 247, 251);padding: 24px;}"
                + "        .container {width: 100%;max-width: 900px;margin: 0 auto;background-color: white;border-radius: 8px;box-sizing: border-box;font-family: 'Product Sans', sans-serif;}"
                + "        .header {position: relative;}"
                + "        .header img.banner {width: 100%;border-top-left-radius: 8px;border-top-right-radius: 8px;}"
                + "        .header img.logo {width: 105px;top: 25.5px;right: 22px;position: absolute;}"
                + "        .body {padding: 40px;box-sizing: border-box;font-family: 'Product Sans', sans-serif;}"
                + "        .footer-message {width: 100%;max-width: 900px;background-color: white;padding: 24px 40px;border-radius: 8px;box-sizing: border-box;margin: 20px auto;text-align: center;font-family: Arial, sans-serif;}"
                + "        .font-italic {font-style: italic;}"
                + "        .font-size {font-size: 16px;}"
                + "        .extra-style {color: #333;font-weight: bold;}"
                + "        .img {width: 100% !important;height: 100% !important;object-fit: cover;}"
                + "        .check-in {margin: 0;font-size: 12px;color: #216D42;font-weight: 400;}"
                + "        .room-name {margin: 0;font-size: 20px;}"
                + "        p.no-margin {margin: 0;}"
                + "        .container-data {vertical-align: baseline;font-size: 14px;}"
                + "        .container-img {width: 433px;padding-right: 16px;}"
                + "        .container-img .img {width: 433px;}"
                + "        .table-layout {font-family: 'Product Sans', sans-serif;width: 100%;}"
                + "        .hr {border: 1px solid #E1E1E1;margin: 0;}"
                + "        .font {font-size: 16px;font-family: 'Product Sans', sans-serif;}"
                + "        .card {width: 100%;padding: 24px;box-sizing: border-box;}"
                + "        .strong-text {color: #384860;font-style: italic;}"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <table class=\"container\" cellpadding=\"0\" cellspacing=\"0\">"
                + "        <tr>"
                + "            <td class=\"header\">"
                + "                <img class=\"banner\" src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.png\" alt=\"Bienvenido\"/>"
                + "            </td>"
                + "        </tr>"
                + "        <tr>"
                + "            <td class=\"body\">"
                + "                <p class=\"font\">Estimado(a), <strong>" + clientName + "</strong></p>"
                + "                <p class=\"font\">El presente es para informar que se completó exitosamente el registro de su pago para la reserva de:"
                + "                    <strong class=\"strong-text\">" + roomName + "</strong></p>"
                + "                <div class=\"card\">"
                + "                    <table class=\"table-layout\">"
                + "                        <tbody>"
                + "                            <tr>"
                + "                                <td style=\"height: 320px; width: 433px;\">"
                + "                                    <table style=\"height: 100%;\">"
                + "                                        <tbody>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <img src=\"" + imgSrc + "\" class=\"img\" alt=\"calendario\" />"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                        </tbody>"
                + "                                    </table>"
                + "                                </td>"
                + "                                <td width=\"16\"></td>"
                + "                                <td class=\"container-data\">"
                + "                                    <table width=\"100%\" style=\"box-sizing: border-box;\">"
                + "                                        <tbody>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"room-name\"><strong>" + roomName + "</strong></p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\">Titular de la reserva:</p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\"><strong>" + titular + "</strong></p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\">Código de reserva:</p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\"><strong>" + code + "</strong></p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <hr class=\"hr\" />"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <table style=\"width: 100%\">"
                + "                                                        <tbody>"
                + "                                                            <tr>"
                + "                                                                <td width=\"100\">"
                + "                                                                    <p class=\"no-margin\">Checkin:</p>"
                + "                                                                    <p class=\"no-margin\"><strong>" + dateCheckIn + "</strong></p>"
                + "                                                                </td>"
                + "                                                                <td width=\"200\"></td>"
                + "                                                                <td style=\"width: 100px; text-align: end;\">"
                + "                                                                    <p class=\"no-margin\">Checkout:</p>"
                + "                                                                    <p class=\"no-margin\"><strong>" + dateCheckOut + "</strong></p>"
                + "                                                                </td>"
                + "                                                            </tr>"
                + "                                                        </tbody>"
                + "                                                    </table>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\">Hora de llegada aproximada: " + hourCheckIn + "</p>"
                + "                                                    <p class=\"check-in\">(*) Recuerda que el check-in es a las 3:00 P.M.</p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\">Duración total de estancia:<strong> " + days + "</strong> noches</p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\">Cantidad de personas: <strong>" + cantidadPersonas + "</strong></p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <hr class=\"hr\" />"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                            <tr>"
                + "                                                <td>"
                + "                                                    <p class=\"no-margin\">Ubicación:</p>"
                + "                                                    <p class=\"no-margin\"><strong>" + location + "</strong></p>"
                + "                                                </td>"
                + "                                            </tr>"
                + "                                        </tbody>"
                + "                                    </table>"
                + "                                </td>"
                + "                            </tr>"
                + "                        </tbody>"
                + "                    </table>"
                + "                </div>"
                + "                <p class=\"font\">"
                + "                    Este correo es solo de carácter informativo, no es un comprobante de pago. En caso de no poder usar la reservación, por favor llamar con 2 días de anticipación.<br>"
                + "                    Muchas gracias."
                + "                </p>"
                + "            </td>"
                + "        </tr>"
                + "    </table>"
                + "    <div class=\"footer-message\">"
                + "        <h3>¿Necesitas ayuda?</h3>"
                + "        <p>Envía tus comentarios e información de errores a <a href=\"mailto:informesyreservas@cieneguilladelrio.com\">informesyreservas@cieneguilladelrio.com</a></p>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public Mono<BigDecimal> getTotalAmountForPromoter(Integer walletId) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity ->
                        bookingRepository.findTotalAmountByUserPromoterIdAndBookingStateId(
                                        walletEntity.getUserPromoterId(), 3)
                                .map(totalAmount -> totalAmount != null ? totalAmount : BigDecimal.ZERO)
                );
    }

    @Override
    public Mono<Map<String, Object>> getBookingDetailsForPromoter(Integer walletId) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    Integer userPromoterId = walletEntity.getUserPromoterId();
                    if (userPromoterId == null) {
                        return Mono.error(new Exception("La wallet no tiene un promotor asociado."));
                    }
                    return bookingRepository.findByUserPromotorIdAndBookingStateId(userPromoterId, 3)
                            .flatMap(booking ->
                                    roomOfferRepository.findById(booking.getRoomOfferId())
                                            .flatMap(roomOffer -> roomRepository.findById(roomOffer.getRoomId())
                                                    .map(room -> {
                                                        Map<String, Object> bookingDetails = new HashMap<>();
                                                        bookingDetails.put("bookingId", booking.getBookingId());
                                                        bookingDetails.put("roomName", room.getRoomName());
                                                        bookingDetails.put("costFinal", booking.getCostFinal());
                                                        bookingDetails.put("bookingState", "Pendiente");
                                                        return bookingDetails;
                                                    })
                                            )
                            )
                            .collectList()
                            .map(details -> {
                                BigDecimal total = details.stream()
                                        .map(booking -> (BigDecimal) booking.get("costFinal"))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                Map<String, Object> response = new HashMap<>();
                                response.put("total", total);
                                response.put("details", details);
                                return response;
                            });
                });
    }


    @Override
    public Mono<WalletTransactionEntity> makeWithdrawal(WithdrawRequestDTO withdrawRequest) {
        
        if (withdrawRequest == null || withdrawRequest.getWalletId() == null || withdrawRequest.getAmount() == null || withdrawRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("Datos de retiro inválidos."));
        }
    
        return walletRepository.findById(withdrawRequest.getWalletId())
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance().compareTo(withdrawRequest.getAmount()) < 0) {
                        return Mono.error(new IllegalStateException("Saldo insuficiente para realizar el retiro."));
                    }
    
                    walletEntity.setBalance(walletEntity.getBalance().subtract(withdrawRequest.getAmount()));
    
                    return walletRepository.save(walletEntity)
                            .flatMap(savedWallet -> 
                                generateUniqueOperationCode().flatMap(operationCode -> {
                                    String description = String.format("Retiro a cuenta bancaria: %s. Titular: %s %s. Documento: %s %s.",
                                            withdrawRequest.getBank(),
                                            withdrawRequest.getHolderFirstName(),
                                            withdrawRequest.getHolderLastName(),
                                            withdrawRequest.getDocumentType(),
                                            withdrawRequest.getDocumentNumber()
                                    );
    
                                    WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                            .walletId(withdrawRequest.getWalletId())
                                            .currencyTypeId(savedWallet.getCurrencyTypeId())
                                            .transactionCategoryId(3)
                                            .amount(withdrawRequest.getAmount())
                                            .operationCode(operationCode)
                                            .description(description)
                                            .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                            .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                            .build();
                                    
                                    return walletTransactionRepository.save(transaction);
                                })
                            );
                })
                .switchIfEmpty(Mono.error(new IllegalStateException("La billetera no fue encontrada.")));
    }

    @Override
    public Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    walletEntity.setBalance(walletEntity.getBalance().add(amount));
                    return walletRepository.save(walletEntity)
                            .flatMap(walletEntity1 -> generateUniqueOperationCode()
                                    .flatMap(operationCode -> {
                                        WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                                .walletId(walletId)
                                                .currencyTypeId(walletEntity.getCurrencyTypeId())
                                                .transactionCategoryId(transactioncatid)
                                                .amount(amount)
                                                .operationCode(operationCode)
                                                .description("Deposito de efectivo")
                                                .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                                .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                                .build();
                                        return walletTransactionRepository.save(transaction);
                                    }));
                });
    }

    @Override
    public Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    walletEntity.setBalance(walletEntity.getBalance().add(amount));
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    return generateUniqueOperationCode()
                            .flatMap(operationCode -> {
                                WalletTransactionEntity transactionEntity = WalletTransactionEntity.builder()
                                        .walletId(walletId)
                                        .currencyTypeId(walletEntity.getCurrencyTypeId())
                                        .transactionCategoryId(transactioncatid)
                                        .amount(amount)
                                        .operationCode(operationCode)
                                        .inicialDate(currentTimestamp)
                                        .avalibleDate(currentTimestamp)
                                        .description("Recarga de saldo")
                                        .build();
                                return walletRepository.save(walletEntity)
                                        .then(walletTransactionRepository.save(transactionEntity));
                            });
                });
    }

    private Mono<String> generateSuccessEmailBodyOrigin(WalletEntity walletEntityOrigin, WalletEntity walletEntityDestiny, BigDecimal amount, String operationCode, Timestamp transactionDate) {
        String formattedDate = transactionDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        String body = "<!DOCTYPE html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Transferencia Exitosa</title>" +
                "    <style>" +
                "        body {font-family: Arial, sans-serif;margin: 0;padding: 0;color: black;background-color: white;}" +
                "        .header {width: 100%;position: relative;background-color: white;padding: 20px 0;}" +
                "        .logo-left {width: 50px;position: absolute;top: 10px;left: 10px;}" +
                "        .banner {width: 100%;display: block;margin: 0 auto;}" +
                "        .container {width: 500px;background-color: #f4f4f4;margin: 20px auto;padding: 20px;border-radius: 10px;box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);text-align: center;}" +
                "        .content {text-align: left;padding: 20px;}" +
                "        .content h3 {margin: 10px 0;}" +
                "        .content p {margin: 10px 0;}" +
                "        .footer {width: 100%;text-align: center;margin: 20px 0;}" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "    </div>" +
                "    <img class=\"banner\" src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.png\" alt=\"Bienvenido\">" +
                "    <div class=\"container\">" +
                "        <div class=\"content\">" +
                "            <h1>Transferencia Exitosa</h1>" +
                "            <h3>Estimado cliente,</h3>" +
                "            <p>Su transferencia ha sido realizada con éxito.</p>" +
                "            <div style=\"background-color: #e0e0e0; padding: 10px; border-radius: 5px;\">" +
                "                <p><strong>Detalles de la Transferencia</strong></p>" +
                "                <p><strong>Monto Transferido:</strong> S/." + amount + "</p>" +
                "                <p><strong>Tarjeta Origen:</strong> " + walletEntityOrigin.getCardNumber() + "</p>" +
                "                <p><strong>Tarjeta Destino:</strong> " + walletEntityDestiny.getCardNumber() + "</p>" +
                "                <p><strong>Código de Operación:</strong> " + operationCode + "</p>" +
                "                <p><strong>Fecha de Transacción:</strong> " + formattedDate + "</p>" +
                "            </div>" +
                "            <p>¡Gracias por usar nuestros servicios! Si tienes alguna pregunta, no dudes en contactarnos.</p>" +
                "        </div>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>&copy; 2024 Ciéneguilla Ribera del Río</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
        return Mono.just(body);
    }


    private Mono<String> generateSuccessEmailBodyForDestination(String walletEntityOrigin, BigDecimal amount, Timestamp transactionDate) {
        String formattedDate = transactionDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String body = "<html>" +
                "<head>" +
                "    <title>Recibiste Fondos</title>" +
                "    <style>" +
                "        body {font-family: Arial, sans-serif;margin: 0;padding: 0;color: black;background-color: white;}" +
                "        .header {width: 100%;position: relative;background-color: white;padding: 20px 0;}" +
                "        .logo-left {width: 50px;position: absolute;top: 10px;left: 10px;}" +
                "        .banner {width: 100%;display: block;margin: 0 auto;}" +
                "        .container {width: 500px;background-color: #f4f4f4;margin: 20px auto;padding: 20px;border-radius: 10px;box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);text-align: center;}" +
                "        .content {text-align: left;padding: 20px;}" +
                "        .content h3 {margin: 10px 0;}" +
                "        .content p {margin: 10px 0;}" +
                "        .footer {width: 100%;text-align: center;margin: 20px 0;}" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "    </div>" +
                "    <img class=\"banner\" src=\"https://s3.us-east-2.amazonaws.com/backoffice.documents/email/panoramica_resort.png\" alt=\"Bienvenido\">" +
                "    <div class=\"container\">" +
                "        <div class=\"content\">" +
                "            <h1>Recibiste Fondos a la Wallet</h1>" +
                "            <h3>Estimado cliente,</h3>" +
                "            <p>¡Felicidades! Has recibido los fondos de una transferencia exitosa.</p>" +
                "            <div style=\"background-color: #e0e0e0; padding: 10px; border-radius: 5px;\">" +
                "                <p><strong>Detalles de la Transferencia</strong></p>" +
                "                <p><strong>Monto Recibido:</strong> S/." + amount + "</p>" +
                "                <p><strong>Fecha de Transacción:</strong> " + formattedDate + "</p>" +
                "            </div>" +
                "            <p>¡Gracias por usar nuestros servicios! Si tienes alguna pregunta, no dudes en contactarnos.</p>" +
                "        </div>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>&copy; 2024 Ciéneguilla Ribera del Río</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        return Mono.just(body);
    }

    @Scheduled(cron = "0 0 10 5,20 * ?" , zone = "America/Lima")
    public Flux<WalletTransactionEntity> processPendingCommissions() {
        return commissionRepository.findValidCommissionsForProcessing()
                .groupBy(CommissionEntity::getPromoterId)
                .flatMap(groupedCommissions -> groupedCommissions.collectList()
                        .flatMap(commissions -> {
                            if(commissions.isEmpty()) {
                                return Mono.empty();
                            }
                            Integer promoterId = commissions.get(0).getPromoterId();
                            BigDecimal totalAmount = commissions.stream()
                                    .map(CommissionEntity::getCommissionAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            return walletRepository.findByUserPromoterId(promoterId)
                                    .flatMap(wallet -> {
                                        wallet.setBalance(wallet.getBalance().add(totalAmount));

                                        return generateUniqueOperationCode()
                                                .flatMap(operationCode -> {
                                                    WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                                            .walletId(wallet.getWalletId())
                                                            .currencyTypeId(wallet.getCurrencyTypeId())
                                                            .transactionCategoryId(4)
                                                            .amount(totalAmount)
                                                            .description("Recarga acumulada de comisiones")
                                                            .operationCode(operationCode)
                                                            .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                                            .avalibleDate(Timestamp.valueOf(LocalDateTime.now()))
                                                            .build();
                                                    commissions.forEach(commission -> {
                                                        commission.setProcessed(true);
                                                        commission.setProcessedAt(Timestamp.from(Instant.now()));
                                                    });

                                                    return walletRepository.save(wallet)
                                                            .thenMany(commissionRepository.saveAll(commissions))
                                                            .then(walletTransactionRepository.save(transaction));
                                                });
                                    });
                        }));
    }


    @Override
    public Map<String, Object> getExchangeRate(String date) {
        String url = urlApiTipoCambio + "?date=" + date;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", tokenApiTipoCambio);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }

    private Mono<String> generateUniqueOperationCode() {
        return Mono.fromSupplier(() -> {
            Random random = new Random();
            return String.valueOf(10000000 + random.nextInt(90000000));
        }).flatMap(code ->
                walletTransactionRepository.findByOperationCode(code)
                        .flatMap(transaction -> generateUniqueOperationCode())
                        .switchIfEmpty(Mono.just(code))
        );
    }
}
