package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.DiscountToRewardEntity;
import com.proriberaapp.ribera.Infraestructure.repository.DiscountToRewardRepository;
import com.proriberaapp.ribera.services.client.DiscountToRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscountToRewardServiceImpl implements DiscountToRewardService {

    private final DiscountToRewardRepository discountToRewardRepository;

    @Override
    public Mono<DiscountToRewardEntity> getDiscountById(Integer id) {
        log.info("Start the method getDiscountById");
        return discountToRewardRepository.findById(id)
                .doOnError(e -> log.error("Error in getDiscountById: " + e.getMessage()))
                .doOnSuccess(v -> log.info("End the method getDiscountById"));
    }

    @Override
    public Mono<DiscountToRewardEntity> getDiscountByName(String name) {
        log.info("Start the method getDiscountByName");
        return discountToRewardRepository.findByName(name)
                .doOnError(e -> log.error("Error in getDiscountByName: " + e.getMessage()))
                .doOnSuccess(v -> log.info("End the method getDiscountByName"));
    }

    @Override
    public Flux<DiscountToRewardEntity> getAllDiscount() {
        log.info("Start the method getAllDiscount");
        return discountToRewardRepository.findAll()
                .doOnError(e -> log.error("Error in getAllDiscount: " + e.getMessage()))
                .doOnComplete(() -> log.info("End the method getAllDiscount"));
    }

    @Override
    public Mono<DiscountToRewardEntity> saveDiscount(DiscountToRewardEntity discountToReward) {
        log.info("Start the method saveDiscount");
        return discountToRewardRepository.save(discountToReward)
                .doOnError(e -> log.error("Error in saveDiscount: " + e.getMessage()))
                .doOnSuccess(v -> log.info("End the method saveDiscount"));
    }

    @Override
    public Mono<DiscountToRewardEntity> updateDiscount(Integer id, DiscountToRewardEntity discountToRewardRequest) {
        return discountToRewardRepository.findById(id)
                .flatMap(existingDiscount -> {
                            Optional.ofNullable(discountToRewardRequest.getName())
                                    .ifPresent(existingDiscount::setName);
                            Optional.ofNullable(discountToRewardRequest.getDiscountValue())
                                    .ifPresent(existingDiscount::setDiscountValue);
                            Optional.ofNullable(discountToRewardRequest.getStatus())
                                    .ifPresent(existingDiscount::setStatus);
                            return discountToRewardRepository.save(existingDiscount);
                        }
                )
                .doOnError(e -> log.error("Error in updateDiscount: " + e.getMessage()))
                .doOnSuccess(v -> log.info("End the method updateDiscount"));
    }
}
