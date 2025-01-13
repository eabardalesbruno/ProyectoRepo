package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalUsersDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.ContactInfo;
import com.proriberaapp.ribera.Api.controllers.client.dto.EventContactInfo;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenResult;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.dto.CompanyDataDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserClientService {
    Mono<UserClientEntity> registerUser(UserClientEntity user, String randomPassword);

    Mono<String> login(String email, String password);

    Mono<UserClientEntity> registerWithGoogle(String googleId, String email, String name);

    Mono<String> loginWithGoogle(String googleId);

    Mono<UserClientEntity> saveUser(UserClientEntity user);

    UserDataDTO searchUser(String username);

    UserDataDTO registerUser(UserDataDTO userDataDTO);

    String loginUser(String username, String password);

    Mono<UserClientEntity> findByEmail(String email);

    Flux<UserClientEntity> findAll();

    Mono<UserClientEntity> findById(Integer id);

    Mono<UserDataDTO> findUserDTOById(Integer id);

    Mono<Void> deleteById(Integer id);

    boolean existsByEmail(String email);

    Flux<UserClientEntity> findByUserPromotorId(Integer id);

    Mono<UserClientEntity> updatePassword(UserClientEntity user, String newPassword);

    Mono<TokenResult> checkAndGenerateToken(String email);

    Mono<Void> sendContactInfo(ContactInfo contactInfo);

    Mono<Void> sendEventContactInfo(EventContactInfo eventContactInfo);

    Mono<UserNameAndDiscountDto> getPercentageDiscount(Integer userId);

    Mono<CompanyDataDto> loadDataRuc(String ruc);

    Mono<TotalUsersDTO> countUsers(Integer month);

    Mono<Void> updateUser(UserClientDto userClientEntity);

}
