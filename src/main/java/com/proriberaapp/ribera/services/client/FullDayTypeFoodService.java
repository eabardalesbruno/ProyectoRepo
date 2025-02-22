package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FullDayTypeFoodService {

    Flux<FullDayTypeFoodEntity> getAllFullDayTypeFood();

    Mono<FullDayTypeFoodEntity> getFullDayTypeFoodById(Integer fullDayTypeFoodId);

    Mono<FullDayTypeFoodEntity> saveFullDayTypeFood(FullDayTypeFoodEntity fullDayTypeFoodEntity, Mono<FilePart> file, Integer folderNumber);

    Mono<FullDayTypeFoodEntity> updateFullDayTypeFood(Integer fullDayTypeFoodId, FullDayTypeFoodEntity fullDayTypeFoodEntity, Mono<FilePart> file, Integer folderNumber);

    Mono<Void> deleteFullDayTypeFood(Integer fullDayTypeFoodId);

    Mono<Integer> getTotalFullDayTypeFood(String type, String name);

    Flux<FullDayTypeFoodEntity> getFullDayTypeFoodByType(String type, String name, int page, int size);
}
