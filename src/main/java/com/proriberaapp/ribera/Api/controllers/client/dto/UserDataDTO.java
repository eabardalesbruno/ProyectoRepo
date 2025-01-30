package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
@Data
public class UserDataDTO {
    private Integer userClientId;
    private String username;
    private String documentNumber;
    private String civilStatus;
    private String email;
    private String city;
    private String cellNumber;
    private Timestamp birthDate;
    private String firstName;
    private String lastName;
    private Integer countryId;
    private Integer walletId;
    private boolean isuserinclub;

    public Mono<UserDataDTO> convertTo(UserClientEntity user) {
         UserDataDTO userDTO = new UserDataDTO();
         userDTO.setUserClientId(user.getUserClientId());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setCity(user.getCity());
        userDTO.setCellNumber(user.getCellNumber());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDocumentNumber(user.getDocumentNumber());
        userDTO.setCivilStatus(user.getCivilStatus());
        userDTO.setEmail(user.getEmail());
        userDTO.setCountryId(user.getCountryId());
        userDTO.setWalletId(user.getWalletId());
        userDTO.setIsuserinclub(user.isUserInclub());
        return Mono.just(userDTO);
    }
}
