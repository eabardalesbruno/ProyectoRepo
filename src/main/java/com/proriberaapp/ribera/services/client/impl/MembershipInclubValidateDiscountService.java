package com.proriberaapp.ribera.services.client.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ExternalAuthService;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseDataMembershipDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.ResponseInclubLoginDto;
import com.proriberaapp.ribera.Domain.dto.DiscountDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.Infraestructure.repository.DiscountRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.MembershipsService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MembershipInclubValidateDiscountService implements VerifiedDiscountService, MembershipsService {

    @Value("${inclub.api.url.subscriptions}")
    private String URL_MEMBERSHIPS;
    @Value("${inclub.api.url.user}")
    private String URL_DATA_USER;
    @Value("${inclub.api.url.promotialGuest}")
    private String URL_PROMOTIONALGUESTS;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserClientRepository userClientRepository;

    @Autowired
    private  MembershipRepository membershipRepository;

    private final ExternalAuthService externalAuthService;

    public MembershipInclubValidateDiscountService(ExternalAuthService externalAuthService) {
        this.externalAuthService = externalAuthService;
    }

    @Override
    public Mono<List<MembershipDto>> loadMembershipsActives(String username) {
        return this.loadMembershipsInsortInclub(username)
                .flatMap(s -> {
                    List<MembershipDto> activeMemberships = s.stream().filter(p -> p.getIdStatus() == 1).toList();
                    return activeMemberships.isEmpty() ? Mono.just(List.of()) : Mono.just(activeMemberships);
                });
    }

    @Override
    public Mono<List<MembershipDto>> loadMembershipsInsortInclub(String username) {
        return this.loadDataUserRiber(username)
                .flatMap(user -> {
                    return externalAuthService.getExternalToken()
                            .flatMap(externalToken -> {
                                String uri = URL_MEMBERSHIPS.concat("/").concat(String.valueOf(user.getData().getId()));
                                WebClient webClient = WebClient.builder()
                                        .baseUrl(uri)
                                        .defaultHeader("Authorization", "Bearer " + externalToken)
                                        .build();
                                return webClient.get()
                                        .retrieve()
                                        .bodyToMono(ResponseDataMembershipDto.class)
                                        .map(ResponseDataMembershipDto::getData);
                            });
                })
                .flatMapMany(Flux::fromIterable)
                .flatMap(membership -> getPromotionalGuestById(membership.getId())
                        .map(promotionalGuest -> {
                            membership.setData(promotionalGuest.getData());
                            return membership;
                        })
                        .defaultIfEmpty(membership))
                .collectList();
    }

    @Override
    public Mono<List<MembershipDto>> loadMembershipsInsortInclubv1(String username, int userId, String token) {
        return this.loadDataUserRiber(username)
                .flatMap(user -> {
                    String uri = URL_MEMBERSHIPS + "/" + user.getData().getId();
                    WebClient webClient = WebClient.builder()
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .build();

                    return webClient.get()
                            .uri(uri)
                            .retrieve()
                            .bodyToMono(ResponseDataMembershipDto.class)
                            .map(ResponseDataMembershipDto::getData);
                })
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .flatMap(apiMemberships -> membershipRepository.findAllByUserclientId(userId).collectList()
                        .flatMap(dbMemberships -> {
                            Timestamp now = new Timestamp(System.currentTimeMillis());
                            LocalDate currentDate = now.toLocalDateTime().toLocalDate();
                            YearMonth currentMonth = YearMonth.from(currentDate);

                            List<MembershipDto> membershipsToUpdate = new ArrayList<>();
                            List<Mono<MembershipDto>> membershipsToInsert = new ArrayList<>();

                            for (MembershipDto apiMembership : apiMemberships) {
                                MembershipDto dbMembership = dbMemberships.stream()
                                        .filter(m -> m.getId() == apiMembership.getId())
                                        .findFirst()
                                        .orElse(null);

                                if (dbMembership != null) {
                                    dbMembership.setStatus(apiMembership.getStatus());
                                    dbMembership.setIdFamilyPackage(apiMembership.getIdFamilyPackage());
                                    dbMembership.setIdStatus(apiMembership.getIdStatus());
                                    dbMembership.setNumberQuotas(apiMembership.getNumberQuotas());
                                    dbMembership.setIdPackage(apiMembership.getIdPackage());
                                    dbMembership.setCreationDate(apiMembership.getCreationDate());
                                    dbMembership.setVolumen(apiMembership.getVolumen());
                                    LocalDateTime lastUpdate = dbMembership.getDataupdate().toLocalDateTime();
                                    YearMonth lastUpdatedMonth = YearMonth.from(lastUpdate);

                                    if (!lastUpdatedMonth.equals(currentMonth)) {
                                        membershipsToUpdate.add(dbMembership);
                                    }
                                } else {
                                    apiMembership.setUserclientId(userId);
                                    apiMembership.setDatacreate(now);
                                    apiMembership.setDataupdate(now);

                                    Mono<MembershipDto> newMembershipMono = getPromotionalGuestById(apiMembership.getId())
                                            .map(promotionalGuest -> {
                                                apiMembership.setData(promotionalGuest.getData());
                                                return apiMembership;
                                            })
                                            .defaultIfEmpty(apiMembership);
                                    membershipsToInsert.add(newMembershipMono);
                                }
                            }
                            return Flux.concat(membershipsToInsert)
                                    .collectList()
                                    .flatMap(newMemberships -> membershipRepository.saveAll(newMemberships).collectList())
                                    .thenMany(Flux.fromIterable(membershipsToUpdate))
                                    .flatMap(membership -> getPromotionalGuestById(membership.getId())
                                            .map(promotionalGuest -> {
                                                membership.setData(promotionalGuest.getData());
                                                return membership;
                                            })
                                            .defaultIfEmpty(membership)
                                    )
                                    .collectList()
                                    .flatMap(updated -> membershipRepository.saveAll(updated).collectList())
                                    .then(membershipRepository.findAllByUserclientId(userId).collectList());
                        })
                );
    }


    private Mono<ResponseInclubLoginDto> loadDataUserRiber(String username) {
        String uri = URL_DATA_USER.concat("/").concat(username);
        WebClient webClient = WebClient.create(uri);
        return webClient.get()
                .retrieve()
                .bodyToMono(
                        ResponseInclubLoginDto.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }

    @Override
    public Mono<UserNameAndDiscountDto> verifiedPercentajeDiscount(int userId, Integer bookingId) {
        return Mono.zip(this.userClientRepository.findById(userId),
                        this.bookingRepository.findByBookingId(bookingId))
                .flatMap(data -> {
                    UserClientEntity userData = data.getT1();
                    BookingEntity booking= data.getT2();
                    return this.loadMembershipsInsortInclub(
                            userData.getUsername())
                            .flatMap(memberships -> {
                                if(memberships.size()==0){
                                    return Mono.just(UserNameAndDiscountDto.empty());
                                }
                                return this.discountRepository
                                        .getDiscountWithItemsAndCurrentYear(userId,
                                                memberships.stream().filter(d->d.getIdFamilyPackage()==2).map(d -> d.getIdPackage()).toList())
                                        .collectList().flatMap(listPercentage -> {
                                            return Mono.just(UserNameAndDiscountDto.builder()
                                                    .username(userData.getUsername())
                                                    .percentage(listPercentage.stream().map(p -> p.getPercentage()).reduce(0.0f,
                                                            Float::sum))
                                                    .discounts(listPercentage.stream()
                                                            .map(p -> new DiscountDto(p.getId(),  p.getName(),p.getPercentage()*booking.getCostFinal().floatValue()/100,  p.getPercentage(),
                                                                    p.isApplyToReservation(), p.isApplyToFood()))
                                                            .toList())
                                                    .build());
                                        });
                            });
                           

                });
    }

    @Override
    public Mono<List<MembershipDto>> loadAllMemberships(int userId) {
        return this.userClientRepository.findById(userId)
                .flatMap(userData -> this.loadMembershipsInsortInclub(userData.getUsername()));

    }

    @Override
    public Mono<MembershipDto> getPromotionalGuestById(Integer id) {
        String uri = URL_PROMOTIONALGUESTS.concat("/").concat(String.valueOf(id));
        WebClient webClient = WebClient.create(uri);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(MembershipDto.class)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }

    //Se ejecuta para limpiar la data antigua con la nueva por cada vez que el socio se invoque
    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteAllMembershipsAtMonthStart() {
        membershipRepository.deleteAll().subscribe();
    }
}