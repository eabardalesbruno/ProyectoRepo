package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FullDayTypeFoodRepository;
import com.proriberaapp.ribera.services.client.FullDayTypeFoodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FullDayTypeFoodServiceImpl implements FullDayTypeFoodService {

    public final FullDayTypeFoodRepository fullDayTypeFoodRepository;

    @Override
    public Flux<FullDayTypeFoodEntity> getAllFullDayTypeFood() {
        return fullDayTypeFoodRepository.findAll();
    }

    @Override
    public Mono<FullDayTypeFoodEntity> getFullDayTypeFoodById(Integer fullDayTypeFoodId) {
        return  fullDayTypeFoodRepository.findById(fullDayTypeFoodId);
    }

    @Override
    public Mono<FullDayTypeFoodEntity> saveFullDayTypeFood(FullDayTypeFoodEntity fullDayTypeFoodEntity) {
        return  fullDayTypeFoodRepository.save(fullDayTypeFoodEntity);
    }

    @Override
    public Mono<FullDayTypeFoodEntity> updateFullDayTypeFood(Integer fullDayTypeFoodId, FullDayTypeFoodEntity fullDayTypeFoodEntity) {
        return fullDayTypeFoodRepository.findById(fullDayTypeFoodId)
                .flatMap(fullDayTypeFood -> {
                    fullDayTypeFood.setFoodName(fullDayTypeFoodEntity.getFoodName());
                    fullDayTypeFood.setFoodDescription(fullDayTypeFoodEntity.getFoodDescription());
                    fullDayTypeFood.setType(fullDayTypeFoodEntity.getType());
                    fullDayTypeFood.setPrice(fullDayTypeFoodEntity.getPrice());
                    fullDayTypeFood.setUrlImage(fullDayTypeFoodEntity.getUrlImage());
                    fullDayTypeFood.setEntry(fullDayTypeFoodEntity.getEntry());
                    fullDayTypeFood.setBackground(fullDayTypeFoodEntity.getBackground());
                    fullDayTypeFood.setDrink(fullDayTypeFoodEntity.getDrink());
                    fullDayTypeFood.setDessert(fullDayTypeFoodEntity.getDessert());
                    fullDayTypeFood.setQuantity(fullDayTypeFoodEntity.getQuantity());
                    fullDayTypeFood.setCurrencyTypeId(fullDayTypeFoodEntity.getCurrencyTypeId());
                    return fullDayTypeFoodRepository.save(fullDayTypeFood);
                });
    }

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
}
