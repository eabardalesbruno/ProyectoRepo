package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.WalletPointHistoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletPointHistoryRepository extends ReactiveCrudRepository<WalletPointHistoryEntity, Integer> {
}
