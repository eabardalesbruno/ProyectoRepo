package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.enums.StatesUser;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UserClientPageDto {

    private List<UserClientDto> usersClients;

    private Long totalClients;


}
