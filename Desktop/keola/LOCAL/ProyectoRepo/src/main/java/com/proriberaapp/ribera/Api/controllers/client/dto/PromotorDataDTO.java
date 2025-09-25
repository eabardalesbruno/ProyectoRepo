package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Data
public class PromotorDataDTO {
    private Integer userPromotorId;
    private String username;
    private String documentNumber;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String address;
    private Integer walletId;
    private Timestamp createdAt;

    public Mono<PromotorDataDTO> convertTo(UserPromoterEntity promotor) {
         PromotorDataDTO promotorDTO = new PromotorDataDTO();
        promotorDTO.setUserPromotorId(promotor.getUserPromoterId());
        promotorDTO.setPhone(promotor.getPhone());
        promotorDTO.setUsername(promotor.getUsername());
        promotorDTO.setFirstName(promotor.getFirstName());
        promotorDTO.setLastName(promotor.getLastName());
        promotorDTO.setDocumentNumber(promotor.getDocumentNumber());
        promotorDTO.setEmail(promotor.getEmail());
        promotorDTO.setAddress(promotor.getAddress());
        promotorDTO.setWalletId(promotor.getWalletId());
        promotorDTO.setCreatedAt(promotor.getCreatedAt());
        return Mono.just(promotorDTO);
    }
}
