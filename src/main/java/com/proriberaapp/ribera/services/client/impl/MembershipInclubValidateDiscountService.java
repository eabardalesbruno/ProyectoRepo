package com.proriberaapp.ribera.services.client.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.Infraestructure.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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
                    String uri = URL_MEMBERSHIPS.concat("/").concat(String.valueOf(user.getData().getId()));
                    WebClient webClient = WebClient.create(uri);
                    return webClient.get()
                            .retrieve()
                            .bodyToMono(ResponseDataMembershipDto.class)
                            .map(ResponseDataMembershipDto::getData);
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

                    System.out.println("Llamando a la URL: " + uri);
                    System.out.println("Token enviado: Bearer " + token);

                    WebClient webClient = WebClient.builder()
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .build();

                    return webClient.get()
                            .uri(uri)
                            .retrieve()
                            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                                System.out.println("Error en la llamada a la API: " + response.statusCode());
                                return response.createException();
                            })
                            .bodyToMono(ResponseDataMembershipDto.class)
                            .map(ResponseDataMembershipDto::getData);
                })
                .flatMapMany(Flux::fromIterable)
                .flatMap(membership -> getPromotionalGuestById(membership.getId())
                        .map(promotionalGuest -> {
                            membership.setData(promotionalGuest.getData());
                            return membership;
                        })
                        .defaultIfEmpty(membership))
                .collectList()
                .flatMap(apiMemberships -> membershipRepository.findAllByUserclientId(userId)
                        .collectList()
                        .flatMap(dbMemberships -> {
                            Timestamp now = new Timestamp(System.currentTimeMillis());
                            List<MembershipDto> newMemberships = apiMemberships.stream()
                                    .filter(apiMembership -> dbMemberships.stream()
                                            .noneMatch(dbMembership -> dbMembership.getId() == apiMembership.getId()))
                                    .map(apiMembership -> {
                                        apiMembership.setUserclientId(userId);
                                        apiMembership.setDatacreate(now);
                                        return apiMembership;
                                    })
                                    .collect(Collectors.toList());
                            return membershipRepository.saveAll(newMemberships)
                                    .collectList()
                                    .then(membershipRepository.findAllByUserclientId(userId).collectList());
                        }));
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
}