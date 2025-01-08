package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import reactor.core.publisher.Mono;

import java.io.File;

public interface GenerateReportService {

  Mono<byte[]> generateReportReservation(int idReservation);

}
