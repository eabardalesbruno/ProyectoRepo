package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ResponseFileDto;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.io.File;

public interface GenerateReportService {

  Mono<ResponseEntity<ResponseFileDto>> generateReportReservation(int idReservation);

}
