package com.proriberaapp.ribera.Api.controllers.dto;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

@Data
public class PaymentBookUploadBoucherDTO {
    private Mono<FilePart> image;
    private Integer folderNumber;
    private PaymentBookEntity paymentBook;
}
