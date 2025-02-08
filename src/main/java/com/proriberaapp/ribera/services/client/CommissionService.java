package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.dto.CommissionAdminDto;
import com.proriberaapp.ribera.Domain.dto.CommissionDTO;
import com.proriberaapp.ribera.Domain.dto.CommissionPromoterDto;
import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CommissionService {

    Mono<CommissionEntity> calculateAndSaveCommission(PaymentBookEntity paymentBook, Integer caseType);

    Mono<BigDecimal> getTotalCommissionByPromoterId(Integer promoterId);

    Flux<CommissionEntity> getCommissionByPromoterId(Integer promoterId);

    Mono<CommissionPromoterDto> getCommissionById(Integer commissionId);

    Mono<CommissionEntity> updateCommission(Integer commissionId, Integer currencyTypeId, BigDecimal userAmount, String rucNumber, Mono<FilePart> file, Integer folderNumber);

    Flux<CommissionEntity> getAllCommission();

    Flux<CommissionDTO> getCommissionsPaged(int page, int size);

    Mono<CommissionEntity> updateStatusByCommissionId(Integer commissionId, String status);

    Mono<CommissionAdminDto> getPaymentBookDetails(Integer paymentBookId);
}
