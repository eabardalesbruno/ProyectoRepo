package com.proriberaapp.ribera.services.client.impl;


import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    //PARTE DE PAGO CON LA WALLET
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
                                        // Crear la transacción en wallet
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
                                            String pdfFilePath = System.getProperty("user.dir") + "/payment_receipt_" +
                                                    transaction.getWalletId() + "_" + System.currentTimeMillis() + ".pdf";

                                            try {
                                                File pdfFile = generatePdfFromHtml(
                                                        transaction.getOperationCode(),
                                                        user.getFirstName() + " " + user.getLastName(),
                                                        user.getDocumentNumber(),
                                                        currency.getCurrencyTypeDescription() + " Wallet",
                                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                                                        booking.getBookingId().toString(),
                                                        room.getRoomName(),
                                                        booking.getCostFinal().setScale(2, RoundingMode.HALF_UP).toString(),
                                                        pdfFilePath
                                                );

                                                return sendSuccessEmail(user.getEmail(), pdfFilePath);
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
                .paymentStateId(1)
                .paymentTypeId(4)
                .currencyTypeId(walletEntity.getCurrencyTypeId())
                .amount(booking.getCostFinal())
                .description("Pago asociado a la reserva")
                .paymentDate(Timestamp.valueOf(LocalDateTime.now()))
                .operationCode(transaction.getOperationCode())
                .totalCost(booking.getCostFinal())
                .paymentComplete(true)
                .refuseReasonId(1)
                .build();

        return paymentBookRepository.save(paymentBook).then();
    }

    private Mono<Tuple3<String, String,String>> getEmailAndLastNameByWalletOwner(WalletEntity walletEntity) {
        if (walletEntity.getUserClientId() != null) {
            return userClientRepository.findById(walletEntity.getUserClientId())
                    .flatMap(user -> Mono.zip(
                            Mono.just(user.getEmail()),
                            Mono.just(user.getUsername()),
                            Mono.just(user.getDocumentNumber())
                    ))
                    .switchIfEmpty(Mono.error(new Exception("Cliente no encontrado.")));
        } else if (walletEntity.getUserPromoterId() != null) {
            return userPromoterRepository.findById(walletEntity.getUserPromoterId())
                    .flatMap(promoter -> Mono.zip(
                            Mono.just(promoter.getEmail()),
                            Mono.just(promoter.getUsername()),
                            Mono.just(promoter.getDocumentNumber())
                    ))
                    .switchIfEmpty(Mono.error(new Exception("Promotor no encontrado.")));
        } else {
            return Mono.error(new Exception("La wallet no tiene asociado un cliente o promotor válido."));
        }
    }

    private Mono<Void> sendSuccessEmail(String email, String pdfFilePath) {
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            return Mono.error(new RuntimeException("El archivo PDF no existe en la ruta especificada: " + pdfFile.getAbsolutePath()));
        }
        String emailBody = generateSuccessEmailBody();

        return emailService.sendEmailWithAttachment(email, emailBody, "Constancia de Pago", pdfFilePath)
                .doOnSuccess(v -> {
                    System.out.println("Correo enviado correctamente a: " + email);

                    if (pdfFile.delete()) {
                        System.out.println("Archivo PDF eliminado correctamente: " + pdfFilePath);
                    } else {
                        System.err.println("Error al eliminar el archivo PDF: " + pdfFilePath);
                    }
                })
                .doOnError(e -> {
                    System.err.println("Error al enviar el correo a " + email + ": " + e.getMessage());
                });
    }

    private String generateSuccessEmailBody() {
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
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1 style='text-align: center;'>Pago exitoso</h1>\n" +
                "            <p>Estimado cliente,</p>\n" +
                "            <p>Su pago ha sido procesado con exito.</p>\n" +
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
    public Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactionCatId, BigDecimal amount) {
        return walletRepository.findById(walletId)
                .flatMap(walletEntity -> {
                    if (walletEntity.getBalance().compareTo(amount) >= 0) {
                        walletEntity.setBalance(walletEntity.getBalance().subtract(amount));
                        return walletRepository.save(walletEntity)
                                .flatMap(savedWallet -> generateUniqueOperationCode()
                                        .flatMap(operationCode -> {
                                            WalletTransactionEntity transaction = WalletTransactionEntity.builder()
                                                    .walletId(walletId)
                                                    .currencyTypeId(walletEntity.getCurrencyTypeId())
                                                    .transactionCategoryId(transactionCatId)
                                                    .amount(amount)
                                                    .operationCode(operationCode)
                                                    .description("Retiro de efectivo")
                                                    .inicialDate(Timestamp.valueOf(LocalDateTime.now()))
                                                    .avalibleDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                                                    .build();
                                            return walletTransactionRepository.save(transaction);
                                        }));
                    } else {
                        return Mono.error(new Exception("Saldo insuficiente"));
                    }
                });
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

        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Transferencia Exitosa</title>\n" +
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
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1>Transferencia Exitosa</h1>\n" +
                "            <h3>Estimado cliente,</h3>\n" +
                "            <p>Su transferencia ha sido realizada con éxito.</p>\n" +
                "            <div style=\"background-color: #e0e0e0; padding: 10px; border-radius: 5px;\">\n" +
                "                <p><strong>Detalles de la Transferencia</strong></p>\n" +
                "                <p><strong>Monto Transferido:</strong> S/." + amount + "</p>\n" +
                "                <p><strong>Tarjeta Origen:</strong> " + walletEntityOrigin.getCardNumber() + "</p>\n" +
                "                <p><strong>Tarjeta Destino:</strong> " + walletEntityDestiny.getCardNumber() + "</p>\n" +
                "                <p><strong>Código de Operación:</strong> " + operationCode + "</p>\n" +
                "                <p><strong>Fecha de Transacción:</strong> " + formattedDate + "</p>\n" +
                "            </div>\n" +
                "            <p>¡Gracias por usar nuestros servicios! Si tienes alguna pregunta, no dudes en contactarnos.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        <p>&copy; 2024 Ciéneguilla Ribera del Río</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return Mono.just(body);
    }


    private Mono<String> generateSuccessEmailBodyForDestination(String walletEntityOrigin, BigDecimal amount, Timestamp transactionDate) {
        String formattedDate = transactionDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String body = "<html>\n" +
                "<head>\n" +
                "    <title>Recibiste Fondos</title>\n" +
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
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1>Recibiste Fondos</h1>\n" +
                "            <h3>Estimado cliente,</h3>\n" +
                "            <p>¡Felicidades! Has recibido los fondos de una transferencia exitosa.</p>\n" +
                "            <div style=\"background-color: #e0e0e0; padding: 10px; border-radius: 5px;\">\n" +
                "                <p><strong>Detalles de la Transferencia</strong></p>\n" +
                "                <p><strong>Nombre de quien realizó la transferencia:</strong> " + walletEntityOrigin + "</p>\n" +
                "                <p><strong>Monto Recibido:</strong> S/." + amount + "</p>\n" +
                "                <p><strong>Fecha de Transacción:</strong> " + formattedDate + "</p>\n" +
                "            </div>\n" +
                "            <p>¡Gracias por usar nuestros servicios! Si tienes alguna pregunta, no dudes en contactarnos.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        <p>&copy; 2024 Ciéneguilla Ribera del Río</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return Mono.just(body);
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
