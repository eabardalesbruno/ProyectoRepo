package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletPointRepository extends R2dbcRepository<WalletPointEntity, Integer> {
}
