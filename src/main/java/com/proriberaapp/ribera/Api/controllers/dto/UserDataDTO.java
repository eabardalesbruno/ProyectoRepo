package com.proriberaapp.ribera.Api.controllers.dto;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
@Data
public class UserDataDTO {
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

    public Mono<UserDataDTO> convertTo(UserClientEntity user) {
         UserDataDTO userDTO = new UserDataDTO();
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

        return Mono.just(userDTO);
    }
}
