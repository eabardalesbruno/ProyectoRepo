package com.proriberaapp.ribera.services.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proriberaapp.ribera.Api.controllers.client.dto.DetailBookInvoiceDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.ItemPersonQuantityDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.UserRewardRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.request.WalletPointRequest;
import com.proriberaapp.ribera.Api.controllers.payme.dto.TransactionNecessaryResponse;
import com.proriberaapp.ribera.Domain.dto.BookingAndRoomNameDto;
import com.proriberaapp.ribera.Domain.dto.DetailEmailFulldayDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Domain.invoice.ProductSunatDomain;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.ProductSunatRepository;
import com.proriberaapp.ribera.services.client.*;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;
import com.proriberaapp.ribera.utils.constants.ExchangeRateCode;
import com.proriberaapp.ribera.utils.constants.DiscountTypeCode;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.BookingEmailDto;
import com.proriberaapp.ribera.utils.emails.ConfirmPaymentByBankTransferAndCardTemplateEmail;
import com.proriberaapp.ribera.utils.emails.EmailTemplateFullday;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class NiubizServiceImpl implements NiubizService {

    @Autowired
    private WebClient nibuizClient;

    @Value("${niubiz.user}")
    private String user;

    @Value("${niubiz.password}")
    private String password;

    @Value("${frontend.webapi.admin-url}")
    private String urlAdminFrontEnd;

    @Value("${frontend.webapi.client-url}")
    private String urlClientFrontEnd;

    @Value("${niubiz.merchantId}")
    private Integer merchantId;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserClientRepository userClientRepository;

    @Autowired
    PaymentBookRepository paymentBookRepository;

    @Autowired
    InvoiceServiceI invoiceService;

    @Autowired
    EmailService emailService;

    @Autowired
    RefusePaymentService refusePaymentService;

    @Autowired
    FullDayRepository fullDayRepository;

    @Autowired
    ProductSunatRepository productSunatRepository;

    RewardPurchaseRepository rewardPurchaseRepository;
    private final WalletPointService walletPointService;
    private final UserRewardService userRewardService;
    private final UserClientService userClientService;
    private final BookingFeedingRepository bookingFeedingRepository;
    private final FamilyGroupRepository familyGroupRepository;
    private final ExchangeRateService exchangeRateService;

    @Override
    public Mono<String> getSecurityToken() {
        //aW50ZWdyYWNpb25lc0BuaXViaXouY29tLnBlOl83ejNAOGZG
        String credentials = user + ":" + password;
        String basic = "Basic " + Base64.encodeBase64String(credentials.getBytes());
        return nibuizClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api.security/v1/security")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", basic)
                .exchangeToMono(response -> {
                    HttpStatus httpStatus = (HttpStatus) response.statusCode();
                    if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                        return response.createException().flatMap(Mono::error);
                    }
                    return response.bodyToMono(String.class);
                });
    }

    @Override
    public Mono<Object> getTokenSession(String token, Object body) {
        return nibuizClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api.ecommerce/v2/ecommerce/token/session/" + merchantId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(response -> {
                    HttpStatus httpStatus = (HttpStatus) response.statusCode();
                    if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                        return response.createException().flatMap(Mono::error);
                    }
                    return response.bodyToMono(Object.class);
                });
    }

    @Override
    public Mono<String> tofinalize(NiubizAutorizationEntity niubizEntity, String token, Long purchaseNumber, Double amount, Integer type, Integer userId) {
        String urlWeb = "";
        if (type == 1) {
            urlWeb += urlAdminFrontEnd + "/manager/bookings-manager";
        } else if (type == 2) {
            urlWeb += urlClientFrontEnd + "/promotor/dashboard/reservas";
        } else if (type == 3) {
            urlWeb += urlClientFrontEnd + "/bookings/reservados";
        } else if (type == 4) {
            urlWeb += urlAdminFrontEnd + "/manager/booking-fullday-manager";
        }

        NiubizAutorizationBodyEntity body = new NiubizAutorizationBodyEntity();
        body.setChannel("web");
        body.setCaptureType("manual");
        body.setCountable(true);
        NiubizAuthorizationOrderEntity order = new NiubizAuthorizationOrderEntity();
        order.setTokenId(niubizEntity.getTransactionToken());
        order.setPurchaseNumber(purchaseNumber.toString());
        order.setAmount(amount);
        order.setCurrency("PEN");
        body.setOrder(order);
        NiubizAuthorizationDataMapEntity datamap = new NiubizAuthorizationDataMapEntity();
        datamap.setUrlAddress(urlClientFrontEnd);
        datamap.setServiceLocationCityName("Lima");
        datamap.setServiceLocationCountrySubdivisionCode("LIM");
        datamap.setServiceLocationCountryCode("PER");
        datamap.setServiceLocationPostalCode("15046");
        body.setDataMap(datamap);
        String finalUrlWeb = urlWeb;
        return nibuizClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api.authorization/v3/authorization/ecommerce/" + merchantId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(response -> {
                    HttpStatus httpStatus = (HttpStatus) response.statusCode();
                    ObjectMapper mapper = new ObjectMapper();
                    if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                        //return response.createException().flatMap(Mono::error);
                        return response.bodyToMono(Object.class).flatMap(object -> {
                            String transactionId = null, dateTransaction = null, message = null, status = null;
                            try {
                                String json = mapper.writeValueAsString(object);
                                JsonNode jsonNode = mapper.readTree(json);
                                JsonNode jsonData = jsonNode.get("data");
                                transactionId = jsonData.get("TRANSACTION_ID") != null ? jsonData.get("TRANSACTION_ID").asText() : "1";
                                dateTransaction = jsonData.get("TRANSACTION_DATE").asText();
                                status = jsonData.get("STATUS").asText();
                                var messageCard = jsonData.get("ACTION_DESCRIPTION").asText();
                                message = Base64.encodeBase64String(messageCard.getBytes());
                            } catch (JsonProcessingException e) {
                                message = "";
                            }
                            return Mono.just(finalUrlWeb + "?transactionId=" + transactionId + "&dateTransaction=" + dateTransaction + "&message=" + message + "&status=" + status);
                        });
                    }
                    return response.bodyToMono(Object.class).flatMap(object -> {
                        String currency = null, transactionId = null, dateTransaction = null, amountTransaction = null, cardTransaction = null, brand = null, status = null;
                        try {
                            String json = mapper.writeValueAsString(object);
                            JsonNode jsonNode = mapper.readTree(json);
                            JsonNode jsonDataMap = jsonNode.get("dataMap");
                            transactionId = jsonDataMap.get("TRANSACTION_ID") != null ? jsonDataMap.get("TRANSACTION_ID").asText() : "";
                            dateTransaction = jsonDataMap.get("TRANSACTION_DATE").asText();
                            amountTransaction = jsonDataMap.get("AMOUNT").asText();
                            cardTransaction = jsonDataMap.get("CARD").asText();
                            cardTransaction = Base64.encodeBase64String(cardTransaction.getBytes());
                            brand = jsonDataMap.get("BRAND").asText();
                            status = jsonDataMap.get("STATUS").asText();
                            JsonNode jsonOrder = jsonNode.get("order");
                            currency = jsonOrder.get("currency").asText();
                        } catch (JsonProcessingException e) {
                            transactionId = "-1";
                        }
                        String urlRedirect = String.format(
                                "%s?transactionId=%s&dateTransaction=%s&amountTransaction=%s" +
                                        "&cardTransaction=%s&brand=%s&currency=%s&status=%s",
                                finalUrlWeb,
                                transactionId,
                                dateTransaction,
                                amountTransaction,
                                cardTransaction,
                                brand,
                                currency,
                                status
                        );
                        if (userId == null) {
                            return Mono.just(urlRedirect);
                        }

                        int rewardPoints = (int) Math.round(amount * 0.1);
                        UserRewardRequest rewardReq = UserRewardRequest.builder()
                                .userId(userId)
                                .points(rewardPoints)
                                .expirationDate(LocalDateTime.now().plusYears(1))
                                .type(RewardType.BOOKING)
                                .build();

                        return userRewardService.create(rewardReq)
                                .then(Mono.just(urlRedirect));
                    });
                });
    }

    @Override
    public Mono<Object> savePayNiubiz(Integer bookingId, String invoiceType, String invoiceDocumentNumber, Double totalDiscount, Double percentageDiscount, Double totalCostWithOutDiscount, Double amount, String transactionId) {
        return bookingRepository.findByBookingId(bookingId)
                .flatMap(booking -> {
                    booking.setBookingStateId(3);
                    return Mono.zip(bookingRepository.save(booking),
                            this.bookingRepository.getRoomNameAndDescriptionfindByBookingId(booking.getBookingId()),
                            userClientRepository.findByUserClientId(booking.getUserClientId()),
                            bookingRepository.getDetailBookInvoice(bookingId));
                })
                .publishOn(Schedulers.boundedElastic()).flatMap(tuple -> {
                    BookingEntity updatedBooking = tuple.getT1();
                    BookingAndRoomNameDto bookingAndRoomNameDto = tuple.getT2();
                    UserClientEntity userClient = tuple.getT3();
                    DetailBookInvoiceDto detailBookInvoice = tuple.getT4();
                    BigDecimal totalCost = new BigDecimal(amount);// monto.divide(BigDecimal.valueOf(100000));
                    InvoiceType type = InvoiceType.getInvoiceTypeByName(invoiceType.toUpperCase());
                    InvoiceClientDomain invoiceClientDomain = new InvoiceClientDomain(
                            userClient.getFirstName(),
                            invoiceDocumentNumber,
                            userClient.getAddress(),
                            userClient.getCellNumber(),
                            userClient.getEmail(),
                            updatedBooking.getUserClientId());
                    InvoiceDomain invoice = new InvoiceDomain(
                            invoiceClientDomain,
                            updatedBooking.getBookingId(),
                            10.0,
                            InvoiceCurrency.PEN,
                            type,
                            percentageDiscount);
                    //invoice.setOperationCode(authorizationResponse.getId());
                    List<String> invoice_notes = new ArrayList<>();
                    invoice_notes.add("ALOJAMIENTO: "+ (updatedBooking.getNumberAdults() + updatedBooking.getNumberAdultsMayor() + updatedBooking.getNumberAdultsExtra())+" ADULTOS + " + (updatedBooking.getNumberChildren())+" NIÑOS");
                    invoice_notes.add(detailBookInvoice.getCheckin()+" "+detailBookInvoice.getCheckout());
                    invoice_notes.add("INCLUYE DESAYUNO");
                    invoice.setInvoice_notes(invoice_notes);

                    String roomDescription = bookingAndRoomNameDto.getRoomDescription();
                    String normalizedRoomDescription = normalizeText(roomDescription);
                    BookingFeedingEntity bookingFeeding = bookingFeedingRepository.findByBookingId(bookingId).block();
                    List<Integer> typesPerson = new ArrayList<>();
                    List<ItemPersonQuantityDto> qitem = new ArrayList<>();
                    if (bookingFeeding != null && bookingFeeding.getBookingFeedingId() != null) {
                        if ((updatedBooking.getNumberChildren() + updatedBooking.getNumberBabies()) > 0) {
                            typesPerson.add(1);
                            //qitem.add(new ItemPersonQuantityDto(1, updatedBooking.getNumberChildren() + updatedBooking.getNumberBabies()));
                            qitem.add(new ItemPersonQuantityDto(1, updatedBooking.getNumberChildren()));
                        }
                        if (updatedBooking.getNumberAdults() > 0) {
                            typesPerson.add(2);
                            qitem.add(new ItemPersonQuantityDto(2, updatedBooking.getNumberAdults()));
                        }
                        if (updatedBooking.getNumberAdultsMayor() > 0) {
                            typesPerson.add(3);
                            qitem.add(new ItemPersonQuantityDto(3, updatedBooking.getNumberAdultsMayor()));
                        }
                        if (updatedBooking.getNumberAdultsExtra() > 0) {
                            typesPerson.add(4);
                            qitem.add(new ItemPersonQuantityDto(4, updatedBooking.getNumberAdultsExtra()));
                        }
                    }

                    Mono<String> codSunatMono = this.productSunatRepository.findAll()
                            .filter(product -> normalizeText(product.getDescription()).equals(normalizedRoomDescription))
                            .map(ProductSunatDomain::getCodSunat)
                            .defaultIfEmpty("631210")
                            .next();

                    return codSunatMono.flatMap(codSunat -> {
                        /*if (userClient.isUserInclub()) {
                            UserNameAndDiscountDto userNameAndDiscount = userClientService.getPercentageDiscount(userClient.getUserClientId(), bookingId, DiscountTypeCode.DISCOUNT_MEMBER).block();
                            invoice.setPercentajeDiscount(userNameAndDiscount.getTotalPercentageDiscountAccommodation());
                        }*/
                        PaymentBookEntity paymentBook = PaymentBookEntity
                                .builder()
                                .bookingId(updatedBooking
                                        .getBookingId())
                                .userClientId(updatedBooking
                                        .getUserClientId())
                                .refuseReasonId(1)
                                .paymentMethodId(6)//1
                                .paymentStateId(2)
                                .paymentTypeId(3)
                                .paymentSubTypeId(6)
                                .currencyTypeId(1)
                                .amount(totalCost)
                                .description("Pago exitoso")
                                .paymentDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima"))))
                                .operationCode(transactionId)
                                .note("Nota de pago")
                                .totalCost(totalCost)
                                .invoiceDocumentNumber(invoiceDocumentNumber)
                                .invoiceType(invoiceType)
                                .imageVoucher("Pago con Tarjeta")
                                .totalPoints(0)
                                .paymentComplete(true)
                                .pendingpay(1)
                                .totalDiscount(totalDiscount)
                                .percentageDiscount(percentageDiscount)
                                .totalCostWithOutDiscount(totalCostWithOutDiscount)
                                .build();
                        if (typesPerson.size() > 0) {
                            return getInvoiceItems(typesPerson, qitem)
                                .flatMap(invoiceItems -> {
                                    List<InvoiceItemDomain> invoiceItemDomains = new ArrayList<>();
                                    AtomicReference<Double> total = new AtomicReference<>(totalCost.doubleValue());
                                    invoiceItems.forEach(item -> {
                                        total.set(total.get() - (item.getPriceUnit().doubleValue() * item.getQuantity()));
                                        invoiceItemDomains.add(item);
                                    });
                                    BigDecimal newTotal = new BigDecimal(String.valueOf(total)).setScale(2, RoundingMode.HALF_UP);
                                    InvoiceItemDomain item = new InvoiceItemDomain(
                                            bookingAndRoomNameDto.getRoomName(),
                                            codSunat,
                                            bookingAndRoomNameDto.getRoomDescription(),
                                            1,
                                            newTotal, "");
                                    invoice.addItemWithIncludedIgv(item);
                                    invoice.addItemsWithIncludedIgv(invoiceItemDomains);
                                    return paymentBookRepository.save(paymentBook)
                                        .flatMap(paymentBookR -> {
                                            invoice.setPaymentBookId(paymentBookR.getPaymentBookId());
                                            return this.invoiceService
                                                .save(invoice)
                                                .then(sendSuccessEmail(
                                                    userClient.getEmail(),
                                                    paymentBookR.getPaymentBookId()));
                                        });
                                })
                                .thenReturn(new TransactionNecessaryResponse(true))
                                .onErrorResume(e -> bookingRepository.findByBookingId(bookingId)
                                    .flatMap(booking -> {
                                        booking.setBookingStateId(3);
                                        return bookingRepository.save(booking);
                                    })
                                    .flatMap(booking -> userClientRepository.findById(userClient.getUserClientId())
                                        .flatMap(userCliente -> sendErrorEmail(userCliente.getEmail(), e.getMessage()))
                                        .then(Mono.error(e))));
                        } else {
                            InvoiceItemDomain item = new InvoiceItemDomain(
                                    bookingAndRoomNameDto.getRoomName(),
                                    codSunat,
                                    bookingAndRoomNameDto.getRoomDescription(),
                                    1,
                                    totalCost, "");
                            invoice.addItemWithIncludedIgv(item);
                            return paymentBookRepository.save(paymentBook)
                                    .flatMap(paymentBookR -> {
                                        invoice.setPaymentBookId(paymentBookR.getPaymentBookId());
                                        return this.invoiceService
                                                .save(invoice)
                                                .then(sendSuccessEmail(
                                                        userClient.getEmail(),
                                                        paymentBookR.getPaymentBookId()));
                                    }).thenReturn(new TransactionNecessaryResponse(true))
                                    .onErrorResume(e -> bookingRepository
                                            .findByBookingId(bookingId)
                                            .flatMap(booking -> {
                                                booking.setBookingStateId(3);
                                                return bookingRepository.save(booking);
                                            })
                                            .flatMap(booking -> userClientRepository.findById(userClient.getUserClientId())
                                                    .flatMap(userCliente -> sendErrorEmail(userCliente.getEmail(), e.getMessage()))
                                                    .then(Mono.error(e))));
                        }
                    });
                });
    }

    public Mono<List<InvoiceItemDomain>> getInvoiceItems(List<Integer> typesPerson, List<ItemPersonQuantityDto> qitem) {
        return familyGroupRepository.getDetailFood(typesPerson)
            .flatMap(detail -> {
                String normalized = normalizeText(detail.getFeeding()).toUpperCase();
                return productSunatRepository.findAll()
                    .filter(product -> normalized.equals(normalizeText(product.getDescription().toUpperCase())))
                    .map(ProductSunatDomain::getCodSunat)
                    .next()
                    .defaultIfEmpty("631210")
                    .map(codItemSunat -> {
                        Integer quantity = qitem.stream()
                                .filter(item -> item.getFamilyGroupId().equals(detail.getFamilygroupid()))
                                .map(ItemPersonQuantityDto::getQuantity)
                                .findFirst()
                                .orElse(0);
                        return new InvoiceItemDomain(
                                detail.getFeeding(),
                                codItemSunat,
                                detail.getFeeding(),
                                quantity,
                                detail.getCost(),
                                ""
                        );
                    });
            })
            .collectList();
    }

    @Override
    public Mono<Object> savePayNiubizFullDay(Integer fullDayId, String invoiceType, String invoiceDocumentNumber,
                                             Double totalDiscount, Double percentageDiscount,
                                             Double totalCostWithOutDiscount, Double amount, String transactionId) {
        return fullDayRepository.findByFulldayid(fullDayId)
                .flatMap(fullDay -> {
                    fullDay.setBookingstateid(3);
                    return Mono.zip(fullDayRepository.save(fullDay),
                            userClientRepository.findByUserClientId(fullDay.getUserClientId()));
                })
                .flatMap(tuple -> {
                    FullDayEntity updatedFullDay = tuple.getT1();
                    UserClientEntity userClient = tuple.getT2();
                    BigDecimal totalCost = BigDecimal.valueOf(amount);

                    InvoiceType type = InvoiceType.getInvoiceTypeByName(invoiceType.toUpperCase());
                    InvoiceClientDomain invoiceClientDomain = new InvoiceClientDomain(
                            userClient.getFirstName(),
                            invoiceDocumentNumber,
                            userClient.getAddress(),
                            userClient.getCellNumber(),
                            userClient.getEmail(),
                            updatedFullDay.getUserClientId());

                    InvoiceDomain invoice = new InvoiceDomain(
                            invoiceClientDomain,
                            updatedFullDay.getFulldayid(),
                            10.0,
                            InvoiceCurrency.PEN,
                            type,
                            percentageDiscount);

                    InvoiceItemDomain item = new InvoiceItemDomain(
                            "FullDay Service",
                            "631210",
                            "Pago de servicio FullDay",
                            1,
                            totalCost, "");

                    invoice.addItemWithIncludedIgv(item);

                    PaymentBookEntity paymentBook = PaymentBookEntity.builder()
                            .fullDayId(updatedFullDay.getFulldayid())
                            .userClientId(updatedFullDay.getUserClientId())
                            .refuseReasonId(1)
                            .paymentMethodId(6)
                            .paymentStateId(2)
                            .paymentTypeId(3)
                            .paymentSubTypeId(6)
                            .currencyTypeId(1)
                            .amount(totalCost)
                            .description("Pago exitoso")
                            .paymentDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima"))))
                            .operationCode(transactionId)
                            .note("Nota de pago")
                            .totalCost(totalCost)
                            .invoiceDocumentNumber(invoiceDocumentNumber)
                            .invoiceType(invoiceType)
                            .imageVoucher("Pago con Tarjeta")
                            .totalPoints(0)
                            .paymentComplete(true)
                            .pendingpay(1)
                            .totalDiscount(totalDiscount)
                            .percentageDiscount(percentageDiscount)
                            .totalCostWithOutDiscount(totalCostWithOutDiscount)
                            .build();

                    return paymentBookRepository.save(paymentBook)
                            .flatMap(paymentBookR -> {
                                invoice.setPaymentBookId(paymentBookR.getPaymentBookId());
                                return invoiceService.save(invoice)
                                        .then(fullDayRepository.findByFulldayid(fullDayId)
                                                .flatMap(fullDay -> {
                                                    fullDay.setBookingstateid(2);
                                                    return fullDayRepository.save(fullDay);
                                                })
                                        )
                                        .then(fetchPaymentDetails(paymentBookR.getPaymentBookId()))
                                        .flatMap(paymentDetails -> {
                                            String recipientName = paymentDetails.getName();
                                            String typeEmail = paymentDetails.getTypefullday();
                                            int reservationCode = paymentDetails.getFulldayid();
                                            String checkInDate = paymentDetails.getCheckinEntry();
                                            int adults = paymentDetails.getAdults();
                                            int children = paymentDetails.getChildren();

                                            String emailBody = EmailTemplateFullday.getAcceptanceTemplate(
                                                    recipientName, typeEmail, reservationCode, checkInDate, adults, children
                                            );
                                            return emailService.sendEmail(
                                                    userClient.getEmail(),
                                                    "Confirmación de Pago Aceptado",
                                                    emailBody
                                            );
                                        });
                            })
                            .thenReturn(new TransactionNecessaryResponse(true))
                            .onErrorResume(e -> {
                                return fullDayRepository.findByFulldayid(fullDayId)
                                        .flatMap(fullDay -> {
                                            fullDay.setBookingstateid(3);
                                            return fullDayRepository.save(fullDay);
                                        })
                                        .then(sendErrorEmail(userClient.getEmail(), e.getMessage()))
                                        .then(Mono.error(e));
                            });
                });
    }


    @Override
    public Mono<String> purchaseRewards(String securityToken,
                                        String transactionToken,
                                        Integer userId,
                                        int rewards,
                                        Integer bookingId, String purchaseNumber) {

        return exchangeRateService.getExchangeRateByCode(ExchangeRateCode.USD)
                .flatMap(exchangeRate -> {
                    double amount = rewards * exchangeRate.getSale();

                    NiubizAutorizationBodyEntity body = new NiubizAutorizationBodyEntity();
                    body.setChannel("web");
                    body.setCaptureType("manual");
                    body.setCountable(true);

                    NiubizAuthorizationOrderEntity order = new NiubizAuthorizationOrderEntity();
                    order.setTokenId(transactionToken);
                    order.setPurchaseNumber(purchaseNumber);
                    order.setAmount(amount);
                    order.setCurrency("PEN");
                    body.setOrder(order);

                    NiubizAuthorizationDataMapEntity dataMap = new NiubizAuthorizationDataMapEntity();
                    dataMap.setUrlAddress(urlClientFrontEnd);
                    dataMap.setServiceLocationCityName("Lima");
                    dataMap.setServiceLocationCountrySubdivisionCode("LIM");
                    dataMap.setServiceLocationCountryCode("PER");
                    dataMap.setServiceLocationPostalCode("15046");
                    body.setDataMap(dataMap);

                    final String finalBookingUrl = (bookingId == -1)
                            ? urlClientFrontEnd + "/exchange-zone"
                            : urlClientFrontEnd + "/payment-method/" + bookingId;
                    return nibuizClient.post()
                            .uri("/api.authorization/v3/authorization/ecommerce/{merchantId}", merchantId)
                            .header(HttpHeaders.AUTHORIZATION, securityToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body)
                            .exchangeToMono(response -> {
                                if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                                    return response.bodyToMono(Object.class)
                                            .flatMap(objError -> {
                                                JsonNode jsonNode = MAPPER.convertValue(objError, JsonNode.class);
                                                log.error("Error en transacción con Niubiz: {}", jsonNode.toPrettyString());

                                                String transactionId = getSafeString(jsonNode.at("/data/TRANSACTION_ID"));
                                                String transactionDate = getSafeString(jsonNode.at("/data/TRANSACTION_DATE"));
                                                String status = getSafeString(jsonNode.at("/data/STATUS"));
                                                String actionDescription = getSafeString(jsonNode.at("/data/ACTION_DESCRIPTION"));

                                                String msgEncoded = Base64.encodeBase64String(
                                                        actionDescription.getBytes(StandardCharsets.UTF_8)
                                                );

                                                String finalUrlWeb = finalBookingUrl + "?" +
                                                        "transactionId=" + transactionId +
                                                        "&dateTransaction=" + transactionDate +
                                                        "&message=" + msgEncoded +
                                                        "&status=" + status +
                                                        "&amount=" + amount +
                                                        "&purchaseNumber=" + purchaseNumber +
                                                        "&action=fail";

                                                return Mono.just(finalUrlWeb);
                                            });

                                } else {
                                    // Exito
                                    return response.bodyToMono(Object.class)
                                            .flatMap(objOk -> {
                                                JsonNode jsonNode = MAPPER.convertValue(objOk, JsonNode.class);

                                                String transactionId = getSafeString(jsonNode.at("/dataMap/TRANSACTION_ID"));
                                                String transactionDate = getSafeString(jsonNode.at("/dataMap/TRANSACTION_DATE"));
                                                String amountStr = getSafeString(jsonNode.at("/dataMap/AMOUNT"));
                                                String status = getSafeString(jsonNode.at("/dataMap/STATUS"));
                                                String brand = getSafeString(jsonNode.at("/dataMap/BRAND"));
                                                String card = getSafeString(jsonNode.at("/dataMap/CARD"));

                                                String currency = getSafeString(jsonNode.at("/order/currency"));

                                                String cardEncoded = Base64.encodeBase64String(
                                                        card.getBytes(StandardCharsets.UTF_8)
                                                );

                                                String finalUrlWeb = finalBookingUrl + "?" +
                                                        "transactionId=" + transactionId +
                                                        "&dateTransaction=" + transactionDate +
                                                        "&status=" + status +
                                                        "&currency=" + currency +
                                                        "&amount=" + amountStr +
                                                        "&card=" + cardEncoded +
                                                        "&brand=" + brand +
                                                        "&purchaseNumber=" + purchaseNumber +
                                                        "&action=success";

                                                return walletPointService.updateWalletPoints(userId,
                                                                WalletPointRequest.builder()
                                                                        .userId(userId)
                                                                        .rewardPoints((double) (rewards))
                                                                        .build())
                                                        .thenReturn(finalUrlWeb);

                                            });
                                }
                            });
                });
    }

    /**
     * Guardar en BD la compra de rewards (similar a savePayNiubiz).
     */
    @Override
    public Mono<RewardPurchase> saveRewardPurchase(Long userId,
                                                   int quantity,
                                                   String transactionId,
                                                   String purchaseNumber,
                                                   double amount,
                                                   String status) {
        RewardPurchase rp = new RewardPurchase();
        rp.setUserId(userId);
        rp.setQuantity(quantity);
        rp.setTotalAmount(amount);
        rp.setTransactionId(transactionId);
        rp.setPurchaseNumber(purchaseNumber);
        rp.setStatus(status);
        rp.setCreatedAt(LocalDateTime.now());

        // Guardar y, si corresponde, actualizar la cantidad de rewards en la tabla "users"
        return rewardPurchaseRepository.save(rp)
                .flatMap(saved -> {
                    // Lógica para sumar Rewards al usuario, si lo deseas
                    // Por ej: userService.addRewardsToUser(userId, quantity);
                    return Mono.just(saved);
                });
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String getSafeString(JsonNode node) {
        return (node.isMissingNode() || node.isNull()) ? "" : node.asText("");
    }


    private Mono<Void> sendSuccessEmail(String email, int paymentBookId) {
        System.out.println("Enviando correo de pago exitoso" + paymentBookId);
        return this.refusePaymentService.getPaymentDetails(paymentBookId)
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
                    System.out.println("Email body: " + baseEmailReserve.execute());
                    return baseEmailReserve.execute();

                }).flatMap(emailBody -> emailService.sendEmail(email, "Pago Exitoso", emailBody));
    }

    private Mono<Void> sendErrorEmail(String email, String errorMessage) {
        String emailBody = generateErrorEmailBody(errorMessage);
        return emailService.sendEmail(email, "Error en el Pago", emailBody);
    }

    private String normalizeText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }

    private String generateErrorEmailBody(String errorMessage) {
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
                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n"
                +
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
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n"
                +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <p>Estimado cliente,</p>\n" +
                "            <p>Ha ocurrido un error durante el procesamiento de su pago.</p>\n" +
                "            <p>Detalles del error:</p>\n" +
                "            <p>Error en el pago por motivos de su entidad bancaria</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Sección de ayuda -->\n" +
                "    <div class=\"help-section\">\n" +
                "        <h3>¿Necesitas ayuda?</h3>\n" +
                "        <p>Comunicate con nosotros a través de los siguientes medios:</p>\n" +
                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return body;
    }

    public Mono<DetailEmailFulldayDto> fetchPaymentDetails(Integer paymentBookId) {
        return paymentBookRepository.getPaymentDetails(paymentBookId);
    }
}
