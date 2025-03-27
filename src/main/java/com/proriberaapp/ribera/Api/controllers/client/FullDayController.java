package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.FullDayRequest;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.dto.*;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.services.client.CompanionsService;
import com.proriberaapp.ribera.services.client.FullDayService;
import com.proriberaapp.ribera.services.client.FullDayTypeFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fullday")
@RequiredArgsConstructor
public class FullDayController {

    private final FullDayService fullDayService;
    private final FullDayTypeFoodService fullDayTypeFoodService;
    private final CompanionsService companionsService;
    private final JwtProvider jtp;

    //FULLDAY CONTROLLER
    @PostMapping("/register")
    public Mono<ResponseEntity<FullDayEntity>> registerFullDay(@RequestBody FullDayRequest request, @RequestHeader("Authorization") String token) {
        return Mono.fromCallable(() -> {
                    Integer authenticatedUserId = jtp.getIdFromToken(token);
                    String authority = jtp.getAuthorityFromToken(token);
                    Integer userClientId = request.getUserClientId();
                    Integer userPromoterId = Role.ROLE_PROMOTER.name().equalsIgnoreCase(authority) ? authenticatedUserId : null;
                    Integer receptionistId = (Role.ROLE_RECEPTIONIST.name().equalsIgnoreCase(authority) ||
                            Role.ROLE_SUPER_ADMIN.name().equalsIgnoreCase(authority)) ? authenticatedUserId : null;
                    return new Object[]{userClientId, userPromoterId, receptionistId};
                })
                .flatMap(userInfo -> {
                    Integer userClientId = (Integer) userInfo[0];
                    Integer userPromoterId = (Integer) userInfo[1];
                    Integer receptionistId = (Integer) userInfo[2];

                    return fullDayService.registerFullDay(receptionistId, userPromoterId, userClientId, request.getType(), request.getBookingDate(), request.getDetails(), request.getFoods());
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/reservations/{filterType}")
    public Flux<FullDayEntity> getReservations(@PathVariable String filterType,@RequestParam  Integer bookingStateId, @RequestHeader("Authorization") String token) {
        Integer userId = jtp.getIdFromToken(token);
        return fullDayService.getReservationsByAssociatedId(userId, filterType, bookingStateId);
    }

    @GetMapping("/reservation/{id}")
    public Mono<FullDayEntity> getReservationById(@PathVariable Integer id) {
        return fullDayService.findById(id);
    }

    @GetMapping("/reservations")
    public Flux<PaymentDetailFulldayDTO> getReservationsAll() {
        return fullDayService.getPaymentDetailFullday();
    }

    //FULLDAY TYPE FOOD CONTROLLER

    @GetMapping("/typeFoodall")
    public Flux<FullDayTypeFoodEntity> getAllFullDayTypeFood() {
        return fullDayTypeFoodService.getAllFullDayTypeFood();
    }

    @GetMapping("/typeFoodTypes")
    public Mono<Map<String, Object>> searchByTypeAndName(@RequestParam(required = false) String type, @RequestParam(required = false) String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        Flux<FullDayTypeFoodEntity> dataFlux = fullDayTypeFoodService.getFullDayTypeFoodByType(type, name, page, size);
        Mono<Integer> totalMono = fullDayTypeFoodService.getTotalFullDayTypeFood(type, name);

        return totalMono.zipWith(dataFlux.collectList())
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("total", tuple.getT1());
                    response.put("page", page);
                    response.put("size", size);
                    response.put("data", tuple.getT2());
                    return response;
                });
    }

    @GetMapping("/typeFood/{id}")
    public Mono<FullDayTypeFoodEntity> getFullDayTypeFoodById(@PathVariable Integer id) {
        return fullDayTypeFoodService.getFullDayTypeFoodById(id);
    }

    @PostMapping("/typeFoodSave")
    public Flux<FullDayTypeFoodEntity> createFullDayTypeFood(@RequestPart("file") Mono<FilePart> file, @RequestParam("foodName") String foodName,
                                                             @RequestParam("type") String type, @RequestParam("price") BigDecimal price, @RequestParam("entry") String entry,
                                                             @RequestParam("background") String background, @RequestParam("drink") String drink, @RequestParam("dessert") String dessert,
                                                             @RequestParam("quantity") Integer quantity, @RequestParam("currencyTypeId") Integer currencyTypeId, @RequestParam("folderNumber") Integer folderNumber) {
        FullDayTypeFoodEntity foodEntity = FullDayTypeFoodEntity.builder()
                .FoodName(foodName)
                .type(type)
                .price(price)
                .Entry(entry)
                .Background(background)
                .Drink(drink)
                .Dessert(dessert)
                .quantity(quantity)
                .currencyTypeId(currencyTypeId)
                .build();
        Mono<FullDayTypeFoodEntity> saveOriginal = fullDayTypeFoodService.saveFullDayTypeFood(foodEntity, file, folderNumber);

        if (type.equalsIgnoreCase("NIÑO")) {
            FullDayTypeFoodEntity adultoMayorEntity = FullDayTypeFoodEntity.builder()
                    .FoodName(foodName)
                    .type("ADULTO_MAYOR")
                    .price(price)
                    .Entry(entry)
                    .Background(background)
                    .Drink(drink)
                    .Dessert(dessert)
                    .quantity(quantity)
                    .currencyTypeId(currencyTypeId)
                    .build();
            Mono<FullDayTypeFoodEntity> saveAdultoMayor = fullDayTypeFoodService.saveFullDayTypeFood(adultoMayorEntity, file, folderNumber);
            return Flux.merge(saveOriginal, saveAdultoMayor);
        }
        return saveOriginal.flux();
    }

    @PutMapping("/typeFoodUp/{fullDayTypeFoodId}")
    public Mono<ResponseEntity<FullDayTypeFoodEntity>> updateFullDayTypeFood(@PathVariable Integer fullDayTypeFoodId,
            @RequestParam String foodName,
            @RequestParam String background,
            @RequestParam String drink,
            @RequestParam String dessert,
            @RequestParam String entry,
            @RequestParam BigDecimal price,
            @RequestParam Integer quantity,
            @RequestParam Integer currencyTypeId,
            @RequestPart(value = "file") Mono<FilePart> file,
            @RequestParam Integer folderNumber) {

        FullDayTypeFoodEntity fullDayTypeFoodEntity = new FullDayTypeFoodEntity();
        fullDayTypeFoodEntity.setFoodName(foodName);
        fullDayTypeFoodEntity.setBackground(background);
        fullDayTypeFoodEntity.setDrink(drink);
        fullDayTypeFoodEntity.setDessert(dessert);
        fullDayTypeFoodEntity.setEntry(entry);
        fullDayTypeFoodEntity.setPrice(price);
        fullDayTypeFoodEntity.setQuantity(quantity);
        fullDayTypeFoodEntity.setCurrencyTypeId(currencyTypeId);

        return fullDayTypeFoodService.updateFullDayTypeFood(fullDayTypeFoodId, fullDayTypeFoodEntity, file, folderNumber)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/typeFoodDl/{id}")
    public Mono<Void> deleteFullDayTypeFood(@PathVariable("id") Integer fullDayTypeFoodId) {
        return fullDayTypeFoodService.deleteFullDayTypeFood(fullDayTypeFoodId);
    }

    //Companions
    @GetMapping("/by-fullday/{fulldayid}")
    public Flux<CompanionsEntity> getCompanionsByFullDayId(@PathVariable Integer fulldayid) {
        return companionsService.getCompanionsByFulldayId(fulldayid);
    }

    @PostMapping("/addCompanion")
    public Flux<CompanionsEntity> addCompanionToFullDay(@RequestBody List<Map<String, Object>> companionsData) {
        List<CompanionsEntity> companionsEntities = companionsData.stream()
                .map(this::convertToCompanionEntity)
                .collect(Collectors.toList());

        return companionsService.calculateAgeAndSaveFullDay(companionsEntities);
    }

    private CompanionsEntity convertToCompanionEntity(Map<String, Object> data) {
        CompanionsEntity companion = new CompanionsEntity();
        companion.setFirstname((String) data.getOrDefault("firstname", data.get("nombres")));
        companion.setLastname((String) data.getOrDefault("lastname", data.get("apellidos")));
        companion.setTypeDocumentId((Integer) data.getOrDefault("typeDocumentId", data.get("typeDocument")));
        companion.setDocumentNumber((String) data.getOrDefault("documentNumber", data.get("document")));
        companion.setCountryId((Integer) data.getOrDefault("countryId", data.get("areaZone")));
        companion.setCellphone((String) data.getOrDefault("cellphone", data.get("celphone")));
        companion.setEmail((String) data.getOrDefault("email", data.get("correo")));
        companion.setTitular((Boolean) data.get("titular"));
        companion.setCategory((String) data.get("category"));
        companion.setFulldayid((Integer) data.get("fulldayid"));
        Object genderValue = data.getOrDefault("genderId", data.get("genero"));
        if (genderValue instanceof String) {
            String genderStr = ((String) genderValue).trim().toLowerCase();
            if (genderStr.equals("masculino")) {
                companion.setGenderId(1);
            } else if (genderStr.equals("femenino")) {
                companion.setGenderId(2);
            } else {
                companion.setGenderId(null);
            }
        } else if (genderValue instanceof Integer) {
            companion.setGenderId((Integer) genderValue);
        }
        if (data.containsKey("birthdate") || data.containsKey("fechaNacimiento")) {
            Object fechaNacimiento = data.getOrDefault("birthdate", data.get("fechaNacimiento"));
            if (fechaNacimiento instanceof String) {
                try {
                    companion.setBirthdate(Timestamp.valueOf(LocalDateTime.parse((String) fechaNacimiento, DateTimeFormatter.ISO_DATE_TIME)));
                } catch (Exception e) {
                    System.out.println("Error al parsear fecha de nacimiento: " + fechaNacimiento);
                }
            }
        }
        return companion;
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<CompanionsDto> fetchCompanionByDni(@PathVariable String dni) {
        try {
            com.proriberaapp.ribera.Domain.dto.CompanionsDto companion = companionsService.fetchCompanionByDni(dni).block();
            return ResponseEntity.ok(companion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/userclient/{userId}")
    public Mono<UserClientEntity> getUserclientFullday(@PathVariable Integer userId) {
        return fullDayService.getUserclientFullday(userId);
    }
    @GetMapping("/payment-details/{bookingId}")
    public Flux<FoodDetailVisualCountDto> getPaymentDetails(@PathVariable Integer bookingId) {
        return fullDayService.getPaymentDetails(bookingId);
    }

    @GetMapping("/visual-count-details/{bookingId}")
    public Mono<VisualCountDetailsDTO> getVisualCountDetails(@PathVariable Integer bookingId) {
        return fullDayService.getVisualCountDetails(bookingId);
    }
}