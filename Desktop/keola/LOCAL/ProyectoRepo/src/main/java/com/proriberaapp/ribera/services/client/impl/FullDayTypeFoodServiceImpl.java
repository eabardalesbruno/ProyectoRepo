package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.request.FullDayTypeFoodUpdateDto;
import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayTypeFoodRepository;
import com.proriberaapp.ribera.services.admin.impl.S3ClientService;
import com.proriberaapp.ribera.services.client.FullDayTypeFoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FullDayTypeFoodServiceImpl implements FullDayTypeFoodService {

    private final FullDayTypeFoodRepository fullDayTypeFoodRepository;

    private final S3ClientService s3ClientService;



    @Override
    public Flux<FullDayTypeFoodEntity> getAllFullDayTypeFood() {
        return fullDayTypeFoodRepository.findAll();
    }

    @Override
    public Mono<FullDayTypeFoodEntity> getFullDayTypeFoodById(Integer fullDayTypeFoodId) {
        return  fullDayTypeFoodRepository.findById(fullDayTypeFoodId);
    }

    @Override
    public Mono<FullDayTypeFoodEntity> saveFullDayTypeFood(FullDayTypeFoodEntity fullDayTypeFoodEntity, Mono<FilePart> file, Integer folderNumber) {
        return file.flatMap(f -> s3ClientService.uploadFile(file, folderNumber))
                .flatMap(imageUrl -> {
                    fullDayTypeFoodEntity.setUrlImage(imageUrl);
                    return fullDayTypeFoodRepository.save(fullDayTypeFoodEntity);
                });
    }
/*
    @Override
    public Mono<FullDayTypeFoodEntity> updateFullDayTypeFood(Integer fullDayTypeFoodId, FullDayTypeFoodEntity fullDayTypeFoodEntity, Mono<FilePart> file, Integer folderNumber) {
        return file.flatMap(f -> s3ClientService.uploadFile(file, folderNumber))
                .flatMap(imageUrl -> fullDayTypeFoodRepository.findById(fullDayTypeFoodId)
                        .flatMap(existingFood -> {
                            existingFood.setFoodName(fullDayTypeFoodEntity.getFoodName());
                            existingFood.setFoodDescription(fullDayTypeFoodEntity.getFoodDescription());
                            existingFood.setPrice(fullDayTypeFoodEntity.getPrice());
                            existingFood.setEntry(fullDayTypeFoodEntity.getEntry());
                            existingFood.setBackground(fullDayTypeFoodEntity.getBackground());
                            existingFood.setDrink(fullDayTypeFoodEntity.getDrink());
                            existingFood.setDessert(fullDayTypeFoodEntity.getDessert());
                            existingFood.setQuantity(fullDayTypeFoodEntity.getQuantity());
                            existingFood.setCurrencyTypeId(fullDayTypeFoodEntity.getCurrencyTypeId());
                            existingFood.setUrlImage(imageUrl);

                            return fullDayTypeFoodRepository.save(existingFood);
                        })
                ).switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró el registro con el ID proporcionado.")));
    }
*/

    @Override
    public Mono<FullDayTypeFoodEntity> updateFullDayTypeFood(Integer fullDayTypeFoodId,
                                                             FullDayTypeFoodEntity fullDayTypeFoodEntity,
                                                             Mono<FilePart> file,
                                                             Integer folderNumber) {
        if (file != null) {
            return file.flatMap(f -> s3ClientService.uploadFile(file, folderNumber))
                    .flatMap(imageUrl -> updateEntity(fullDayTypeFoodId, fullDayTypeFoodEntity, imageUrl));
        } else {
            return fullDayTypeFoodRepository.findById(fullDayTypeFoodId)
                    .flatMap(existing -> {
                        // Mantener la URL de imagen existente
                        return updateEntity(fullDayTypeFoodId, fullDayTypeFoodEntity, existing.getUrlImage());
                    });
        }
    }

    private Mono<FullDayTypeFoodEntity> updateEntity(Integer id, FullDayTypeFoodEntity fullDayTypeFoodEntity, String imageUrl) {
        return fullDayTypeFoodRepository.findById(id)
                .flatMap(existingFood -> {
                    existingFood.setFoodName(fullDayTypeFoodEntity.getFoodName());
                    existingFood.setFoodDescription(fullDayTypeFoodEntity.getFoodDescription());
                    existingFood.setPrice(fullDayTypeFoodEntity.getPrice());
                    existingFood.setEntry(fullDayTypeFoodEntity.getEntry());
                    existingFood.setBackground(fullDayTypeFoodEntity.getBackground());
                    existingFood.setDrink(fullDayTypeFoodEntity.getDrink());
                    existingFood.setDessert(fullDayTypeFoodEntity.getDessert());
                    existingFood.setQuantity(fullDayTypeFoodEntity.getQuantity());
                    existingFood.setCurrencyTypeId(fullDayTypeFoodEntity.getCurrencyTypeId());
                    existingFood.setUrlImage(imageUrl);
                    return fullDayTypeFoodRepository.save(existingFood);
                });
    }

/*
    @Override
    public Mono<FullDayTypeFoodEntity> updateFullDayTypeFood(Integer fullDayTypeFoodId, FullDayTypeFoodEntity fullDayTypeFoodEntity, Mono<FilePart> file, Integer folderNumber) {
        Mono<String> imageUrlMono;

        if (file != null) {
            imageUrlMono = file.flatMap(f -> s3ClientService.uploadFile(file, folderNumber));
        } else {
            imageUrlMono = Mono.just(fullDayTypeFoodEntity.getUrlImage());
        }

        return imageUrlMono.flatMap(imageUrl -> fullDayTypeFoodRepository.findById(fullDayTypeFoodId)
                .flatMap(existingFood -> {
                    existingFood.setFoodName(fullDayTypeFoodEntity.getFoodName());
                    existingFood.setFoodDescription(fullDayTypeFoodEntity.getFoodDescription());
                    existingFood.setPrice(fullDayTypeFoodEntity.getPrice());
                    existingFood.setEntry(fullDayTypeFoodEntity.getEntry());
                    existingFood.setBackground(fullDayTypeFoodEntity.getBackground());
                    existingFood.setDrink(fullDayTypeFoodEntity.getDrink());
                    existingFood.setDessert(fullDayTypeFoodEntity.getDessert());
                    existingFood.setQuantity(fullDayTypeFoodEntity.getQuantity());
                    existingFood.setCurrencyTypeId(fullDayTypeFoodEntity.getCurrencyTypeId());
                    existingFood.setUrlImage(imageUrl);
                    /*
                    if (imageUrl != null) {
                        existingFood.setUrlImage(imageUrl);
                    }

                    return fullDayTypeFoodRepository.save(existingFood);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró el registro con el ID proporcionado.")))
        );
    }
*/
    @Override
    public Mono<Void> deleteFullDayTypeFood(Integer fullDayTypeFoodId) {
        return fullDayTypeFoodRepository.deleteById(fullDayTypeFoodId);
    }

    @Override
    public Mono<Integer> getTotalFullDayTypeFood(String type,String name) {
        return fullDayTypeFoodRepository.countByType(type,name);
    }

    @Override
    public Flux<FullDayTypeFoodEntity> getFullDayTypeFoodByType(String type,String name, int page, int size) {
        int DEFAULT_PAGE_SIZE = 5;
        int limit = size > 0 ? size : DEFAULT_PAGE_SIZE;
        int offset = page > 0 ? (page - 1) * limit : 0;
        return fullDayTypeFoodRepository.findByTypePaged(type,name, limit, offset);
    }

    @Override
    public Mono<FullDayTypeFoodEntity> patchFullDayTypeFood(Integer fullDayTypeFoodId, FullDayTypeFoodUpdateDto patchDto) {
        return fullDayTypeFoodRepository.findById(fullDayTypeFoodId)
                .flatMap(existingFood -> {
                    Optional.ofNullable(patchDto.getBackground()).ifPresent(existingFood::setBackground);
                    Optional.ofNullable(patchDto.getCurrencyTypeId()).ifPresent(existingFood::setCurrencyTypeId);
                    Optional.ofNullable(patchDto.getDessert()).ifPresent(existingFood::setDessert);
                    Optional.ofNullable(patchDto.getDrink()).ifPresent(existingFood::setDrink);
                    Optional.ofNullable(patchDto.getEntry()).ifPresent(existingFood::setEntry);
                    Optional.ofNullable(patchDto.getFoodDescription()).ifPresent(existingFood::setFoodDescription);
                    Optional.ofNullable(patchDto.getFoodName()).ifPresent(existingFood::setFoodName);
                    Optional.ofNullable(patchDto.getPrice()).ifPresent(existingFood::setPrice);
                    Optional.ofNullable(patchDto.getQuantity()).ifPresent(existingFood::setQuantity);
                    Optional.ofNullable(patchDto.getUrlImage()).ifPresent(existingFood::setUrlImage);
                    return fullDayTypeFoodRepository.save(existingFood);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Registro no encontrado")));
    }
}
