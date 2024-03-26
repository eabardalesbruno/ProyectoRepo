package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BookingRepository;
import com.proriberaapp.ribera.services.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final WebClient webClient;
    @Value("${app.upload.dir}")
    private String uploadDir;
    @Value("${app.upload.folderDir}")
    private String folderDir;


    public BookingServiceImpl(BookingRepository bookingRepository, WebClient.Builder webClientBuilder) {
        this.bookingRepository = bookingRepository;
        this.webClient = webClientBuilder.baseUrl(uploadDir)
                .build();
    }
    @Override
    public Mono<BookingEntity> save(BookingEntity bookingEntity) {
        bookingEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return bookingRepository.findByBookingStateId(bookingEntity
                ).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Booking already exists"))
                        : bookingRepository.save(bookingEntity));
    }

    @Override
    public Flux<BookingEntity> saveAll(List<BookingEntity> bookingEntity) {
        return bookingRepository.findAllByBookingStateIdIn(bookingEntity)
                .collectList()
                .flatMapMany(bookingEntities -> bookingRepository.saveAll(
                        bookingEntity.stream().filter(
                                bookingEntity1 -> {
                                    bookingEntity1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                                    return !bookingEntities.contains(bookingEntity1);
                                }
                        ).toList()
                ));
    }

    @Override
    public Mono<BookingEntity> findById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Flux<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bookingRepository.deleteById(id);
    }

    @Override
    public Mono<BookingEntity> update(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }

    @Override
    public Mono<S3UploadResponse> loadBoucher(Resource file, String token) throws IOException {
        byte[] imageBytes = Files.readAllBytes(file.getFile().toPath());

        return webClient.post()
                .uri("/s3")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(buildMultipartData(folderDir, imageBytes)))
                .retrieve()
                .bodyToMono(S3UploadResponse.class)
                .map(S3UploadResponse::responseToEntity);
    }

    private MultipartBodyBuilder buildMultipartData(String folderNumber, byte[] imageBytes) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", imageBytes)
                .filename("image.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-Disposition", "form-data; name=\"file\"; filename=\"image.jpg\"");
        builder.part("folderNumber", folderNumber);
        return builder;
    }

}
